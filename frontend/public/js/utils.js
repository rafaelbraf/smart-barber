export async function carregarVariaveisDeAmbiente() {
    try {
        const response = await fetch('/config');
        if (!response.ok) {
            throw new Error('Erro ao carregar variáveis de ambiente.');
        }

        const config = await response.json();

        return config.apiUrl;
    } catch (error) {
        console.error('Erro ao carregar variáveis de ambiente: ', error);
        throw error;
    }
}