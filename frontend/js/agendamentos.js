const token = localStorage.getItem('token');
const barbeariaId = localStorage.getItem('barbearia');

buscarAgendamentosDaBarbearia();

function buscarAgendamentosDaBarbearia() {
    fetch(`http://localhost:5000/agendamentos/barbearia/${barbeariaId}`, {
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
            throw new Error('Erro ao buscar agendamentos.');
        }
    })
    .then((data) => {
        const container = document.getElementById('agendamentos-container');
        const agendamentos = data.agendamentos;
        agendamentos.forEach(agendamento => {
            const card = document.createElement('div');
            card.innerHTML = `
                <div class="card mt-1" style="cursor: pointer;">
                    <div class="card-body justify-content-between">
                        <div class="row">
                            <div class="col">
                                ${agendamento.id}
                            </div>
                            <div class="col">
                                ${agendamento.data}
                            </div>
                            <div class="col">
                                ${agendamento.hora}
                            </div>
                            <div class="col">
                                R$ ${formatarPreco(agendamento.valorTotal)}
                            </div>
                            <div class="col">
                                ${agendamento.tempoDuracaoEmMinutos} minutos
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

function formatarPreco(preco) {
    return preco.toString().replace(/\./g, ",");
}