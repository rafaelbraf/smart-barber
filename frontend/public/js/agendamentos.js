let urlBackend;

document.addEventListener('DOMContentLoaded', carregarVariaveisDeAmbiente);

const token = localStorage.getItem('token');
const barbeariaId = localStorage.getItem('barbearia');


async function buscarAgendamentosDaBarbearia() {
    try {
        const response = await fetch(`${urlBackend}/agendamentos/barbearia/${barbeariaId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            const jsonResponse = await response.json();
            await renderizarCalendarioDeAgendamentos(jsonResponse.agendamentos);
        } else {
            throw new Error(`Erro ao buscar usuário: ${idUsuario}.`);
        }
    } catch (error) {
        console.error('Erro: ', error);
    }
}

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

    try {
        const response = await fetch(`${urlBackend}/usuarios/${idUsuario}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            return await response.json();
        } else {
            throw new Error(`Erro ao buscar usuário: ${idUsuario}.`);
        }
    } catch (error) {
        console.error('Erro: ', error);
    }
}

function formatarPreco(preco) {
    return preco.toString().replace(/\./g, ",");
}

async function renderizarCalendarioDeAgendamentos(agendamentos) {
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
    try {
        const response = await fetch(`${urlBackend}/agendamentos/${agendamentoId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            return await response.json();
        } else {
            throw new Error(`Erro ao buscar agendamento: ${agendamentoId}.`);
        }
    } catch (error) {
        console.error('Erro: ', error);
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
function carregarVariaveisDeAmbiente() {
    fetch('/config')
        .then(response => {
            if (!response.ok) {
                throw new Error("Erro ao carregar variáveis de ambiente.");
            }
            
            return response.json();
        })
        .then(config => {
            urlBackend = config.apiUrl
        })
        .catch(error => console.error("Erro ao carregar variáveis de ambiente: ", error));
}