let urlBackend;

document.addEventListener('DOMContentLoaded', carregarVariaveisDeAmbiente);

function fazerLogin() {
    const email = document.getElementById("email").value;
    const senha = document.getElementById("senha").value;

    if (email && senha) {
        const dadosLogin = {
            email: email,
            senha: senha
        };

        fetch(`${urlBackend}/auth/barbearias/login`, {
            method: 'POST',
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(dadosLogin)
        })
        .then(response => response.json())
        .then(data => {
            localStorage.setItem('token', data.access_token);
            localStorage.setItem('barbearia', data.barbearia.id);
            
            window.location.href = 'pages/home.html';
        })
        .catch(error => {
            console.log("Erro na requisição: " + error);
        });
    }
}

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
        })
        .catch(error => console.error("Erro ao carregar variáveis de ambiente: ", error));
}