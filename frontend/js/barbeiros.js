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
        container.innerHTML = '';

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
        });
    })
    .catch((error) => {
        console.error('Erro: ', error);
    })
}

function cadastrarBarbeiroNaBarbearia() {
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
        idBarbearia: idBarbearia
    };

    fetch('http://localhost:5000/barbeiros', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(barbeiro)
    })
    .then((response) => {
        if (response.status === 201) {
            response.json().then(data => {
                limparCamposModalBarbeiro();

                var modal = new bootstrap.Modal(document.getElementById('cadastrarBarbeiroModal'));
                modal.hide();
                
                exibirAlertaBarbeiro("success",data.mensagem);
                buscarBarbeirosDaBarbearia();
            });
        } else if (response.status === 400){
            response.json().then(data =>{
                console.log(data)
                exibirAlertaBarbeiro("error",data.mensagem);
            })
        }
         else {
            throw new Error('Erro ao salvar barbeiro.');
        }
    })
    .catch(error => console.error(error));

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

function deletarBarbeiro(barbeiroId) {
    if (confirm('Tem certeza que deseja excluir o barbeiro?')) {
        fetch(`http://localhost:5000/barbeiros/${barbeiroId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        })
        .then(response => {
            if (response.ok) {
                exibirAlertaBarbeiro("success",'Barbeiro excluído com sucesso!');
                buscarBarbeirosDaBarbearia();
            } else {
                exibirAlertaBarbeiro("error",'Erro ao excluir barbeiro.');
                throw new Error('Erro ao excluir barbeiro.');
            }
        })
        .catch(error => console.error(error));
    }
}

function exibirAlertaBarbeiro(status, mensagem) {
    var idAlerta = "";
    if (status === "success"){
        idAlerta = "alertSuccessBarbeiro";
    } else{
        idAlerta = "alertErrorBarbeiro";
    }
    var alert = document.getElementById(idAlerta);
    alert.classList.add("show");
    alert.innerHTML = mensagem;
    alert.innerHTML +='<button type= "button" class= "btn-close" data-bs-dismiss="alert" aria-label="Close"></button>';
    setTimeout(function() {
        alert.classList.remove("show");
    },3000);

}