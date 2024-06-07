import { ApiClient } from "./utils/apiClient.js";
import { carregarVariaveisDeAmbiente } from "./utils/loadEnvs.js";
let urlBackend;

document.addEventListener('DOMContentLoaded', async () => {
    urlBackend = await carregarVariaveisDeAmbiente();
});

const token = localStorage.getItem('token');
const barbeariaId = localStorage.getItem('barbearia');


function buscarClientesDaBarbearia() {
    const url = `${urlBackend}/usuarios/barbearia/${barbeariaId}`
    const headers = {
        'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
    }
    const response = await ApiClient.get(url,headers)
    const data =
    fetch(`${urlBackend}/usuarios/barbearia/${barbeariaId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    })
    .then((response) => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error('Erro ao buscar clientes.');
        }
    })
    .then((data) => {
        const container = document.getElementById('clientes-container');
        const clientes = data.usuarios;
        clientes.forEach(cliente => {
            const card = document.createElement('div');
            card.innerHTML = `
                <div class="card mt-1" style="cursor: pointer;">
                    <div class="card-body justify-content-between">
                        <div class="row">
                            <div class="col">
                                ${cliente.id}
                            </div>
                            <div class="col">
                                ${cliente.nome}
                            </div>
                            <div class="col">
                                ${cliente.celular}
                            </div>
                            <div class="col">
                                ${cliente.email}
                            </div>
                        </div>
                    </div>
                </div>
            `;

            container.appendChild(card);
        });
    })
    .catch((error) => {
        console.error('Erro: ', error);
    });
}