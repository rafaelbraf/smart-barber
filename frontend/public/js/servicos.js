import { carregarVariaveisDeAmbiente } from "./utils/loadEnvs.js";
import { ApiClient } from "./utils/apiClient.js";

let urlBackend;

document.addEventListener('DOMContentLoaded', async () => {
    urlBackend = await carregarVariaveisDeAmbiente();
});

const apiClient = new ApiClient();
const token = localStorage.getItem('token');
const barbeariaId = localStorage.getItem('barbearia');
const buttonSalvarServico = document.getElementById("buttonSalvarServico")
buttonSalvarServico.addEventListener('click',salvarServico)
const buttonEditarServico = document.getElementById("buttonEditarServico")
buttonEditarServico.addEventListener('click',editarServico)

async function buscarServicosDaBarbearia() {
    const url = `${urlBackend}/servicos/barbearia/${barbeariaId}`
    const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
    }
    const response = await apiClient.get(url,headers) 
    const data = await response.json();
    const container = document.getElementById('servicos-container');
    container.innerHTML = '';

    const servicos = data.result;
    popularTabela(servicos);
}
    
function formatarPreco(preco) {
    return preco.toString().replace(/\./g, ",");
}

async function salvarServico() {
    const nomeServico = document.getElementById('nomeServico').value;
    const precoServico = parseFloat(document.getElementById('precoServico').value);
    const duracaoServico = parseInt(document.getElementById('duracaoServico').value);
    const idBarbearia = localStorage.getItem('barbearia');

    if (!isNullOrEmptyOrUndefined(nomeServico) && !isNullOrEmptyOrUndefined(precoServico) && !isNullOrEmptyOrUndefined(duracaoServico) && !isNullOrEmptyOrUndefined(idBarbearia)) {
        const servico = {
            nome: nomeServico,
            preco: precoServico,
            tempoDuracaoEmMinutos: duracaoServico,
            barbearia: {
                id: idBarbearia
            },
            ativo: true
        };
        const url = `${urlBackend}/servicos`    
        const headers = {
            'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
        }
        const response = await apiClient.post(url,headers,servico)
        const data = await response.json()
        if (data.statusCode === 201) {
            limparCamposModalServico();
            var modal = new bootstrap.Modal(document.getElementById('incluirServicoModal'));
            modal.hide();

            exibirAlertaServicos("success",data.message);
            buscarServicosDaBarbearia();
        } else {
            exibirAlertaServicos("error",data.message);
        }
    
        buscarServicosDaBarbearia();
    } else {
        console.error('Por favor, preencha todos os campos corretamente.');
    }
}

async function mostrarServicoPorId(servicoId,modalId) {
    var modal = new bootstrap.Modal(document.getElementById(modalId));
    modal.show();
    const servico = await buscarServico(servicoId);
    const idServico = isMostrarServicoModal(modalId) ? 'idServicoVisualizar' :'idServico';
    const nomeServico = isMostrarServicoModal(modalId) ? 'nomeServicoVisualizar' : 'nomeServicoEditar'
    const precoServico = isMostrarServicoModal(modalId) ? 'precoServicoVisualizar' : 'precoServicoEditar'
    const duracaoServico = isMostrarServicoModal(modalId) ? 'duracaoServicoVisualizar' : 'duracaoServicoEditar'
    document.getElementById(idServico).value = servico.id;
    document.getElementById(nomeServico).value = servico.nome;
    document.getElementById(precoServico).value = servico.preco;
    document.getElementById(duracaoServico).value = servico.tempoDuracaoEmMinutos;
}

function isMostrarServicoModal(modalId) {
    return modalId === 'mostrarServicoModal';
}

async function editarServico() {
    const idServico = document.getElementById('idServico').value;
    const nomeServico = document.getElementById('nomeServicoEditar').value;
    const precoServico = parseFloat(document.getElementById('precoServicoEditar').value);
    const duracaoServico = parseInt(document.getElementById('duracaoServicoEditar').value);

    if (!isNullOrEmptyOrUndefined(nomeServico) && !isNullOrEmptyOrUndefined(precoServico) && !isNullOrEmptyOrUndefined(duracaoServico)) {
        const servico = {
            nome: nomeServico,
            preco: precoServico,
            tempoDuracaoEmMinutos: duracaoServico,
            id: idServico,
            ativo: true,
        };
        const url = `${urlBackend}/servicos`
        const headers = {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
        const response = await apiClient.put(url,headers,servico)
        const data = await response.json()
        if (data.statusCode === 200) {
            limparCamposModalEditarServico();

            var modal = new bootstrap.Modal(document.getElementById('editarServicoModal'));
            modal.hide();

            exibirAlertaServicos("success",data.message);
            buscarServicosDaBarbearia();
        } else {
            exibirAlertaServicos("error",data.message);
        }
    }
}
   

async function buscarServico(servicoId) {
    const url = `${urlBackend}/servicos/${servicoId}`
    const headers = {
        'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
    }
    const response = await apiClient.get(url,headers)
    const resposta = await response.json();
            return resposta.result
}
 
async function deletarServico(servicoId) {
    const url = `${urlBackend}/servicos/${servicoId}`
    const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
    }
    const response = await apiClient.delete(url,headers)
    if (response.ok) {
        exibirAlertaServicos("success",'Serviço excluído com sucesso!');
        buscarServicosDaBarbearia();
    } else {
        exibirAlertaServicos("error",'Erro ao excluir serviço.');
    }

}

function isNullOrEmptyOrUndefined(value) {
    return value === null || value === '' || value === undefined;
}

function limparCamposModalServico() {
    document.getElementById('nomeServico').value = '';
    document.getElementById('precoServico').value = '';
    document.getElementById('duracaoServico').value = '';
}
 
function limparCamposModalEditarServico() {
    document.getElementById('nomeServicoEditar').value = '';
    document.getElementById('precoServicoEditar').value = '';
    document.getElementById('duracaoServicoEditar').value = '';
    document.getElementById('idServico').value = '';

}

function popularTabela(servicos) {
    const tableBody = document.querySelector('.table tbody'); 
    tableBody.innerHTML = '';

    servicos.forEach(servico => {
        const row = document.createElement('tr');
        row.className = 'cursor-pointer'

        const idCell = document.createElement('td');
        idCell.textContent = servico.id;
        row.appendChild(idCell);

        const nomeCell = document.createElement('td');
        nomeCell.textContent = servico.nome;
        row.appendChild(nomeCell);

        const precoCell = document.createElement('td');
        precoCell.textContent = formatarPreco(servico.preco);
        row.appendChild(precoCell);

        const duracaoCell = document.createElement('td');
        duracaoCell.textContent = `${servico.tempoDuracaoEmMinutos} minutos`;
        row.appendChild(duracaoCell);

        const actionsCell = document.createElement('td');
        const editIcon = document.createElement('i');
        editIcon.className = 'bi bi-pencil-square cursor-pointer';
        editIcon.onclick = function() { mostrarServicoPorId(servico.id,'editarServicoModal') };
        const viewIcon = document.createElement('i');
        viewIcon.className = 'bi bi-eye cursor-pointer';
        viewIcon.onclick = function() { mostrarServicoPorId(servico.id,'mostrarServicoModal') };


        const deleteIcon = document.createElement('i');
        deleteIcon.className = 'bi bi-trash cursor-pointer';
        deleteIcon.onclick = function() { deletarServico(servico.id) };

        actionsCell.appendChild(viewIcon);
        actionsCell.appendChild(editIcon);
        actionsCell.appendChild(deleteIcon);
        row.appendChild(actionsCell);

        tableBody.appendChild(row);
    });
}

function exibirAlertaServicos(status, mensagem) {
    var idAlerta = "";
    if (status === "success"){
        idAlerta = "alertSuccessServico";
    } else{
        idAlerta = "alertErrorServico";
    }
    var alert = document.getElementById(idAlerta);
    alert.classList.add("show");
    alert.innerHTML = mensagem;
    alert.innerHTML += '<button type= "button" class= "btn-close" data-bs-dismiss="alert" aria-label="Close"></button>';
    setTimeout(function() {
        alert.classList.remove("show");
    },3000);
}