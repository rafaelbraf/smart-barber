const apiUrl = process.env.REACT_APP_BACKEND_API_URL;
const token = "";

export const BarbeariaService = {    
    async pesquisarBarbeariasPorNome(searchTerm: string): Promise<any> {        
        try {
            const response = await fetch(`${apiUrl}/barbearias/nome?nome=${searchTerm}`, {
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
            console.error(error);
            throw error;
        }
    },

    async pesquisarTodasAsBarbearias(): Promise<any> {
        try {
            const response = await fetch(`${apiUrl}/barbearias?limit=8`, {
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
            console.error(error);
            throw error;
        }
    }
};