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
        popularTabela(servicos);
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
        
        const deleteIcon = document.createElement('i');
        deleteIcon.className = 'bi bi-trash cursor-pointer';
        deleteIcon.onclick = function() { deletarServico(servico.id) };

        actionsCell.appendChild(editIcon);
        actionsCell.appendChild(deleteIcon);
        row.appendChild(actionsCell);

        tableBody.appendChild(row);
    });
}