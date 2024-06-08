export class ApiClient {

    GET_METHOD = 'GET';
    POST_METHOD = 'POST';
    PUT_METHOD = 'PUT';
    DELETE_METHOD = 'DELETE';

    constructor() {}

    async get(url, headers) {
        return await sendRequestWithoutBody(url, this.GET_METHOD, headers);
    }

    async post(url, headers, body) {
        return await sendRequestWithBody(url, this.POST_METHOD, headers, body);
    }

    async put(url, headers, body) {
        return await sendRequestWithBody(url, this.PUT_METHOD, headers, body);
    }

    async delete(url, headers) {
        return await sendRequestWithoutBody(url, this.DELETE_METHOD, headers);
    }

}

async function sendRequestWithBody(url, method, headers, body) {
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

async function sendRequestWithoutBody(url, method, headers) {
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