let urlBackend;

document.addEventListener('DOMContentLoaded', carregarVariaveisDeAmbiente);

const token = localStorage.getItem('token');
const barbeariaId = localStorage.getItem('barbearia');


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
            buscarClientesDaBarbearia();
        })
        .catch(error => console.error("Erro ao carregar variáveis de ambiente: ", error));
}

function buscarClientesDaBarbearia() {
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