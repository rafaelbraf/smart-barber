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
    const precoServico = parseFloat(document.getElementById('precoServico').value);
    const duracaoServico = parseInt(document.getElementById('duracaoServico').value);
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

                    exibirAlertaServicos("success",data.mensagem);
                    buscarServicosDaBarbearia();
                });
            } else if (response.status === 400){
                response.json().then(data =>{
                    console.log(data)
                    exibirAlertaServicos("error",data.mensagem);
                })
             
                throw new Error('Erro ao salvar serviço.');
            }
        })
        .catch(error => console.error(error));
    
        buscarServicosDaBarbearia();
    } else {
        console.error('Por favor, preencha todos os campos corretamente.');
    }
}

async function mostrarServicoPorId(servicoId) {
    var modal = new bootstrap.Modal(document.getElementById('editarServicoModal'));
    modal.show();
    const servico = await buscarServico(servicoId);
    document.getElementById('idServico').value = servico.id;
    document.getElementById('nomeServicoEditar').value = servico.nome;
    document.getElementById('precoServicoEditar').value = servico.preco;
    document.getElementById('duracaoServicoEditar').value = servico.tempoDuracaoEmMinutos;
}

function editarServico() {
    const idServico = document.getElementById('idServico').value;
    const nomeServico = document.getElementById('nomeServicoEditar').value;
    const precoServico = parseFloat(document.getElementById('precoServicoEditar').value);
    const duracaoServico = parseInt(document.getElementById('duracaoServicoEditar').value);

    if (!isNullOrEmptyOrUndefined(nomeServico) && !isNullOrEmptyOrUndefined(precoServico) && !isNullOrEmptyOrUndefined(duracaoServico)) {
        const servico = {
            nome: nomeServico,
            preco: precoServico,
            tempoDuracaoEmMinutos: duracaoServico,
        };

        fetch(`http://localhost:5000/servicos/${idServico}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(servico)
        })
        .then((response) => {
            if (response.status === 200) {
                response.json().then(data => {
                    limparCamposModalEditarServico();

                    var modal = new bootstrap.Modal(document.getElementById('editarServicoModal'));
                    modal.hide();

                    exibirAlertaServicos("success",data.mensagem);
                    buscarServicosDaBarbearia();
                });
            } else if (response.status === 400){
                response.json().then(data =>{
                    exibirAlertaServicos("error",data.mensagem);
                })
             
                throw new Error('Erro ao editar serviço.');
            }
        })
        .catch(error => console.error(error)) ;
    }
}

async function buscarServico(servicoId) {
    try {
        const response = await fetch(`http://localhost:5000/servicos/${servicoId}`,{
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            return await response.json();
        } else {
            throw new Error(`Erro ao buscar servico: ${servicoId}.`);
        }
    } catch (error) {
        console.error('Erro: ', error);
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
                exibirAlertaServicos("success",'Serviço excluído com sucesso!');
                buscarServicosDaBarbearia();
            } else {
                exibirAlertaServicos("error",'Erro ao excluir serviço.');
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
        editIcon.onclick = function() { mostrarServicoPorId(servico.id) };

        const deleteIcon = document.createElement('i');
        deleteIcon.className = 'bi bi-trash cursor-pointer';
        deleteIcon.onclick = function() { deletarServico(servico.id) };

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
