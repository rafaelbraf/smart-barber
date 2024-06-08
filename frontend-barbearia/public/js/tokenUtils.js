const TOKEN_NAME = 'token';
const ID_LOGOUT_BUTTON = 'logoutButton';
const PAGINA_INDEX = '../index.html';

document.addEventListener('DOMContentLoaded', verificarSeContemToken);
document.getElementById(ID_LOGOUT_BUTTON).addEventListener('click', removerTokenERecarregarPagina);

function verificarSeContemToken() {
    const token = localStorage.getItem(TOKEN_NAME);
    if (isNullOrUndefined(token)) {
        redirecionarPagina(PAGINA_INDEX);
    }
}

function removerTokenERecarregarPagina() {
    localStorage.removeItem(TOKEN_NAME);
    
    location.reload();
}

function redirecionarPagina(pagina) {
    window.location.href = pagina;
}

function isNullOrUndefined(value) {
    return !value || value === 'undefined';
}