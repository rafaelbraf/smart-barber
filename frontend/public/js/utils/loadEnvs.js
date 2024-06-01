import { ApiClient } from "./apiClient.js";

const apiClient = new ApiClient();

export async function carregarVariaveisDeAmbiente() {
    try {
        const response = await apiClient.get('/config', {})
        const config = await response.json();

        return config.apiUrl;
    } catch (error) {
        console.error('Erro ao carregar variáveis de ambiente: ', error);
        throw error;
    }
}