import { carregarVariaveisDeAmbiente } from "./utils/loadEnvs.js";
import { ApiClient } from "./utils/apiClient.js";

export let urlBackend;

export const apiClient = new ApiClient();

document.addEventListener('DOMContentLoaded', async () => {
    urlBackend = await carregarVariaveisDeAmbiente();
});

export const token = localStorage.getItem('token');
export const barbeariaId = localStorage.getItem('barbearia');


function verAgendamento(agendamentoId) {
    fetch(`${urlBackend}/agendamentos/${agendamentoId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => {
        if (response.ok) {
            
        }
    })
}       

async function mostrarModalDoAgendamento(agendamentoId) {
    const modal = new bootstrap.Modal(document.getElementById('verAgendamentoModal'));
    modal.show();

    const agendamento = await buscarInformacoesDoAgendamento(agendamentoId);
    document.getElementById('idAgendamento').value = agendamento.id;
    document.getElementById('dataHoraAgendamento').value = `${agendamento.data} - ${agendamento.hora}`;
    document.getElementById('valorTotalAgendamento').value = formatarPreco(agendamento.valorTotal);
    document.getElementById('tempoDuracaoAgendamento').value = `${agendamento.tempoDuracaoEmMinutos} minutos`;

    const cliente = await buscarClienteDoAgendamento(agendamento);
    document.getElementById('idCliente').value = cliente.id;
    document.getElementById('nomeCliente').value = cliente.nome;
    document.getElementById('celularCliente').value = cliente.celular;
    document.getElementById('emailCliente').value = cliente.email;
}

async function buscarClienteDoAgendamento(agendamento) {
    const idUsuario = agendamento.idUsuario;
    const url = `${urlBackend}/usuarios/${idUsuario}`
    const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
    }
    const response = await apiClient.get(url,headers)
    if (response.ok) {
        return await response.json();
    } else {
        throw new Error(`Erro ao buscar usuário: ${idUsuario}.`);
    }
    
}

function formatarPreco(preco) {
    return preco.toString().replace(/\./g, ",");
}

export async function renderizarCalendarioDeAgendamentos(agendamentos) {
    const calendarioEl = document.getElementById('calendario-agendamentos');
    const agendamentosParaCalendario = [];
    agendamentos.forEach(agendamento => {
        const agendamentoParaCalendario = {
            id: agendamento.id,
            title: agendamento.usuario.nome,
            start: `${agendamento.dataHoraAgendamentoInicio}`,
            end: `${agendamento.dataHoraAgendamentoFim}`
        };

        agendamentosParaCalendario.push(agendamentoParaCalendario);
    });

    const calendario = new FullCalendar.Calendar(calendarioEl, {
        initialView: 'dayGridMonth', 
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay' 
        },
        events: agendamentosParaCalendario,
        eventClick: function(info) {
            mostrarModalDoAgendamento(info.event.id);
        }
    });

    calendario.setOption('locale', 'br');

    calendario.render(); // Renderizar o calendário
}

async function buscarInformacoesDoAgendamento(agendamentoId) {
    const url = `${urlBackend}/agendamentos/${agendamentoId}`
    const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
    }
    const response = await apiClient.get(url,headers)
    const data = response.json()
    if (response.ok) {
        return await response.json();
    } else {
        throw new Error(`Erro ao buscar agendamento: ${agendamentoId}.`);
    }
}

function limparCamposModal() {
    document.getElementById('idAgendamento').value = '';
    document.getElementById('dataHoraAgendamento').value = '';
    document.getElementById('valorTotalAgendamento').value = '';
    document.getElementById('tempoDuracaoAgendamento').value = '';
    document.getElementById('idCliente').value = '';
    document.getElementById('nomeCliente').value = '';
    document.getElementById('celularCliente').value = '';
    document.getElementById('emailCliente').value = '';
}