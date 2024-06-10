import { ApiClient } from "./utils/apiClient.js";
import { carregarVariaveisDeAmbiente } from "./utils/loadEnvs.js";
let urlBackend;

const apiClient = new ApiClient();


document.addEventListener('DOMContentLoaded', async () => {
    urlBackend = await carregarVariaveisDeAmbiente();
});

const token = localStorage.getItem('token');
const barbeariaId = localStorage.getItem('barbearia');


async function buscarClientesDaBarbearia() {
    const url = `${urlBackend}/usuarios/barbearia/${barbeariaId}`
    const headers = {
        'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
    }
    const response = await apiClient.get(url,headers)
    const data = response.json()
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
        
        })
}