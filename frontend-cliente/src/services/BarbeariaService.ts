import ApiClient from "../api/ApiClient";
import { ApiResponse } from "../api/ApiResponse";
import { Barbearia } from "../models/Barbearia";

class BarbeariaService {
    static async pesquisarBarbeariasPorNome(searchTerm: string): Promise<Barbearia[]> {
        try {
            const response = await ApiClient.get<ApiResponse<Barbearia[]>>(`/barbearias/nome?nome=${searchTerm}`);
            return response.result;
        } catch (error) {
            console.error('Erro ao pesquisar barbearias por nome:', error);
            throw error;
        }
    }

    static async pesquisarTodasAsBarbearias(): Promise<Barbearia[]> {
        try {
            const response = await ApiClient.get<ApiResponse<Barbearia[]>>(`/barbearias?limit=8`);
            return response.result;
        } catch (error) {
            console.error('Erro ao pesquisar todas as barbearias:', error);
            throw error;
        }
    }
};

export default BarbeariaService;