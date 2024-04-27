const token = localStorage.getItem('token');
const barbeariaId = localStorage.getItem('barbearia');

buscarBarbeirosDaBarbearia();

function buscarBarbeirosDaBarbearia() {
    fetch(`http://localhost:5000/barbeiros/barbearia/${barbeariaId}`, {
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
            throw new Error('Erro ao buscar barbeiros.');
        }
    })
    .then((data) => {
        const container = document.getElementById('barbeiros-container');
        const barbeiros = data.barbeiros;
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
                        </div>
                    </div>
                </div>
            `;

            container.appendChild(card);
        });
    })
    .catch((error) => {
        console.error('Erro: ', error);
    })
}