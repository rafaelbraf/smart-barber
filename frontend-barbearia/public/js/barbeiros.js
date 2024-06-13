import { ApiClient } from "./utils/apiClient.js";
import { carregarVariaveisDeAmbiente } from "./utils/loadEnvs.js";
let urlBackend;

const apiClient = new ApiClient();

document.addEventListener('DOMContentLoaded', async () => {
    urlBackend = await carregarVariaveisDeAmbiente();
    await buscarBarbeirosDaBarbearia();
});

const token = localStorage.getItem('token');
const barbeariaId = localStorage.getItem('barbearia');
const buttonCadastrarBarbeiro = document.getElementById('buttonCadastrarBarbeiro')
buttonCadastrarBarbeiro.addEventListener('click',cadastrarBarbeiro)



async function buscarBarbeirosDaBarbearia() {
    const url = `${urlBackend}/barbeiros/barbearia/${barbeariaId}`
    const headers = {
        'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
    }
    const response = await apiClient.get(url,headers)
    const data = await response.json()
    const container = document.getElementById('barbeiros-container');
    container.innerHTML = '';

    const barbeiros = data.result;
    barbeiros.forEach(barbeiro => {
        const card = document.createElement('div');
        card.innerHTML = `
            <div class="card mt-1" style="cursor: pointer;">
                <div class="card-body justify-content-between">
                    <div class="row">
                        <div class="col">
                            ${barbeiro.id}
                        </div>
                        <div class="col">
                            ${barbeiro.nome}
                        </div>
                        <div class="col">
                            <input type="checkbox" ${barbeiro.ativo ? 'checked' : ''} class="form-check-input" disabled>
                        </div>
                        <div class="col text-end">
                            <button class="btn btn-primary btn-sm" onclick="editarBarbeiro(${barbeiro.id})">
                                <i class="bi bi-pencil-square"></i>
                            </button>
                        </div>
                        <div class="col text-end">
                            <button class="btn btn-danger btn-sm" onclick="deletarBarbeiro(${barbeiro.id})">
                                <i class="bi bi-trash"></i>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        `;

        container.appendChild(card);
    
    })
}

async function cadastrarBarbeiro() {
    const nomeBarbeiro = document.getElementById('nomeBarbeiro').value;
    const cpfBarbeiro = document.getElementById('cpfBarbeiro').value;
    const celularBarbeiro = document.getElementById('celularBarbeiro').value;
    const emailBarbeiro = document.getElementById('emailBarbeiro').value;
    const isBarbeiroAdmin = document.getElementById('checkboxBarbeiroAdmin').checked;
    const isBarbeiroAtivo = document.getElementById('checkboxBarbeiroAtivo').checked;
    const idBarbearia = localStorage.getItem('barbearia');

    const barbeiro = {
        nome: nomeBarbeiro,
        cpf: cpfBarbeiro,
        celular: celularBarbeiro,
        email: emailBarbeiro,
        admin: isBarbeiroAdmin,
        ativo: isBarbeiroAtivo,
        barbearia: {
            id:idBarbearia
        }
    };
    const url = `${urlBackend}/barbeiros`
    const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
    }
    const response = await apiClient.post(url,headers,barbeiro)
    const data = await response.json()
    if( data.statusCode === 201){
        limparCamposModalBarbeiro();

        var modal = new bootstrap.Modal(document.getElementById('cadastrarBarbeiroModal'));
        modal.hide();
        
        exibirAlertaBarbeiro("success",data.message);
        buscarBarbeirosDaBarbearia();
    } else {
        exibirAlertaBarbeiro("error",data.message);
    }

    buscarBarbeirosDaBarbearia();
}

function limparCamposModalBarbeiro() {
    document.getElementById('nomeBarbeiro').value = '';
    document.getElementById('cpfBarbeiro').value = '';
    document.getElementById('celularBarbeiro').value = '';
    document.getElementById('emailBarbeiro').value = '';
    document.getElementById('checkboxBarbeiroAdmin').checked = false;
    document.getElementById('checkboxBarbeiroAtivo').checked = true;
}

 async function deletarBarbeiro(barbeiroId) {
    if (confirm('Tem certeza que deseja excluir o barbeiro?')) {
        const url = `${urlBackend}/barbeiros/${barbeiroId}`
        const headers = {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
        const response = await apiClient.delete(url,headers)
        if (response.ok) {
            exibirAlertaBarbeiro("success",'Barbeiro excluído com sucesso!');
            buscarBarbeirosDaBarbearia();
        } else {
            exibirAlertaBarbeiro("error",'Erro ao excluir barbeiro.');
        }
    }
}

function exibirAlertaBarbeiro(status, message) {
    var idAlerta = "";
    if (status === "success"){
        idAlerta = "alertSuccessBarbeiro";
    } else{
        idAlerta = "alertErrorBarbeiro";
    }
    var alert = document.getElementById(idAlerta);
    alert.classList.add("show");
    alert.innerHTML = message;
    alert.innerHTML +='<button type= "button" class= "btn-close" data-bs-dismiss="alert" aria-label="Close"></button>';
    setTimeout(function() {
        alert.classList.remove("show");
    },3000);

}