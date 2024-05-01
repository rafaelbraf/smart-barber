const token = localStorage.getItem('token');
const barbeariaId = localStorage.getItem('barbearia');

buscarServicosDaBarbearia();

function buscarServicosDaBarbearia() {
    fetch(`http://localhost:5000/servicos/barbearia/${barbeariaId}`, {
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
            throw new Error('Erro ao buscar serviços.');
        }
    })
    .then((data) => {
        const container = document.getElementById('servicos-container');
        container.innerHTML = '';

        const servicos = data.servicos;
        servicos.forEach(servico => {
            const card = document.createElement('div');
            card.innerHTML = `
                <div class="card mt-1" style="cursor: pointer;">
                    <div class="card-body justify-content-between">
                        <div class="row">
                            <div class="col">
                                ${servico.id}
                            </div>
                            <div class="col">
                                ${servico.nome}
                            </div>
                            <div class="col">
                                R$ ${formatarPreco(servico.preco)}
                            </div>
                            <div class="col">
                                ${servico.tempoDuracaoEmMinutos} minutos
                            </div>
                            <div class="col text-end">
                                <button class="btn btn-danger btn-sm" onclick="deletarServico(${servico.id})">
                                    <i class="bi bi-trash"></i>
                                </button>
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

function formatarPreco(preco) {
    return preco.toString().replace(/\./g, ",");
}

function salvarServico() {
    const nomeServico = document.getElementById('nomeServico').value;
    const precoServico = document.getElementById('precoServico').value;
    const duracaoServico = document.getElementById('duracaoServico').value;
    const idBarbearia = localStorage.getItem('barbearia');

    if (!isNullOrEmptyOrUndefined(nomeServico) && !isNullOrEmptyOrUndefined(precoServico) && !isNullOrEmptyOrUndefined(duracaoServico) && !isNullOrEmptyOrUndefined(idBarbearia)) {
        const servico = {
            nome: nomeServico,
            preco: precoServico,
            tempoDuracaoEmMinutos: duracaoServico,
            idBarbearia: idBarbearia
        };
    
        fetch('http://localhost:5000/servicos', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(servico)
        })
        .then((response) => {
            if (response.status === 201) {
                response.json().then(data => {
                    console.log(data);

                    limparCamposModalServico();

                    var modal = new bootstrap.Modal(document.getElementById('incluirServicoModal'));
                    modal.hide();
                });
            } else {
                throw new Error('Erro ao salvar serviço.');
            }
        })
        .catch(error => console.error(error));
    
        buscarServicosDaBarbearia();
    } else {
        console.error('Por favor, preencha todos os campos corretamente.');
    }
}

function deletarServico(servicoId) {
    if (confirm('Tem certeza que deseja excluir o serviço?')) {
        fetch(`http://localhost:5000/servicos/${servicoId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        })
        .then(response => {
            if (response.ok) {
                alert('Serviço excluído com sucesso!');
                
                buscarServicosDaBarbearia();
            } else {
                throw new Error('Erro ao excluir serviço.');
            }
        })
        .catch(error => console.error(error));
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