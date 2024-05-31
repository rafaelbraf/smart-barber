import { fazerRequisicaoGet } from "./request.js";

export async function carregarVariaveisDeAmbiente() {
    try {
        const response = await fazerRequisicaoGet('/config')
        const config = await response.json();

        return config.apiUrl;
    } catch (error) {
        console.error('Erro ao carregar variáveis de ambiente: ', error);
        throw error;
    }
}