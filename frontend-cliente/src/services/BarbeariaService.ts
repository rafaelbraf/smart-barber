export const BarbeariaService = {
    async pesquisarBarbeariasPorNome(searchTerm: string): Promise<any> {
        const apiUrl = process.env.REACT_APP_BACKEND_API_URL;
        const token = "";
        try {
            const response = await fetch(`${apiUrl}/barbearias?nome=${searchTerm}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });

            if (!response.ok) {
                throw new Error('Erro ao pesquisar barbearias por nome.');
            }

            const responseJson = await response.json();
            const barbeariasEncontradasLista = await responseJson.result;

            return barbeariasEncontradasLista;
        } catch (error) {
            console.error("Error fetching search results:", error);
            throw error;
        }
    }
};