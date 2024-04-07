function fazerLogin() {
    const email = document.getElementById("email").value;
    const senha = document.getElementById("senha").value;

    if (email && senha) {
        const dadosLogin = {
            email: email,
            senha: senha
        };

        fetch('http://localhost:5000/auth/login', {
            method: 'POST',
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(dadosLogin)
        })
        .then(response => response.json())
        .then(data => {
            localStorage.setItem('token', data.access_token);
            
            window.location.href = 'pages/home.html';
        })
        .catch(error => {
            console.log("Erro na requisição: " + error);
        });
    }
}