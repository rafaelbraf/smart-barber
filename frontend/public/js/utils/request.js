export async function fazerRequisicaoGet(url, headers, body) {
    try {
        return await fetch(url, {
            method: 'GET',
            headers: headers,
            body: JSON.stringify(body)
        });
    } catch (error) {
        console.error('Erro ao carregar variáveis de ambiente: ', error);
        throw error;
    }
}

export async function fazerRequisicaoPost(url, headers, body) {
    try {
        return await fetch(url, {
            method: 'POST',
            headers: headers,
            body: JSON.stringify(body)
        });
    } catch (error) {
        console.error('Erro ao carregar variáveis de ambiente: ', error);
        throw error;
    }
}