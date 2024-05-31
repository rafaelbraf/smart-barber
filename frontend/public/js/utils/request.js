const GET_METHOD = 'GET';
const POST_METHOD = 'POST';
const PUT_METHOD = 'PUT';
const DELETE_METHOD = 'DELETE';

export async function fazerRequisicaoGet(url, headers) {
    return await enviarRequisicaoSemBody(url, GET_METHOD, headers);
}

export async function fazerRequisicaoPost(url, headers, body) {
    return await enviarRequisicaoComBody(url, POST_METHOD, headers, body);
}

export async function fazerRequisicaoPut(url, headers, body) {
    return await enviarRequisicaoComBody(url, PUT_METHOD, headers, body);
}

export async function fazerRequisicaoDelete(url, headers) {
    return await enviarRequisicaoSemBody(url, DELETE_METHOD, headers);
}

async function enviarRequisicaoComBody(url, method, headers, body) {
    try {
        return await fetch(url, {
            method: method,
            headers: headers,
            body: JSON.stringify(body)
        });
    } catch (error) {
        console.error(`Erro ao fazer requisição ${method} para url: ${url} - error: `, error);
        throw error;
    }
}

async function enviarRequisicaoSemBody(url, method, headers) {
    try {
        return await fetch(url, {
            method: method,
            headers: headers
        });
    } catch (error) {
        console.error(`Erro ao fazer requisição ${method} para url: ${url} - error: `, error);
        throw error;
    }    
}