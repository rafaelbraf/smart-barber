import { carregarVariaveisDeAmbiente } from "./utils/loadEnvs.js";
import { ApiClient } from "./utils/apiClient.js";

let urlBackend;

const apiClient = new ApiClient();

document.addEventListener('DOMContentLoaded', async () => {
    urlBackend = await carregarVariaveisDeAmbiente();
});

const buttonLogin = document.getElementById('buttonLogin');
buttonLogin.addEventListener('click', fazerLogin);

async function fazerLogin() {
    const email = document.getElementById("email").value;
    const senha = document.getElementById("senha").value;

    if (email && senha) {
        const url = `${urlBackend}/auth/login`;
        const dadosLogin = { 
            email: email, 
            senha: senha 
        };
        const headers = { 
            'Content-Type': 'application/json' 
        };
        const response = await apiClient.post(url, headers, dadosLogin);

        const data = await response.json();
        if (data.success) {
            localStorage.setItem('token', data.accessToken);
            localStorage.setItem('barbearia', data.result.id);

            window.location.href = 'pages/home.html';
        } else {
            if (data.statusCode === '401') {
                console.log('Credenciais inválidas');
            } else {
                console.log('Erro ao fazer login.');
            }
        }
    }
}