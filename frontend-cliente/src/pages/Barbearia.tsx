import { Button, Col, Container, Row } from "react-bootstrap";
import { Link, useParams } from "react-router-dom";

// 4 - template expression
import TemplateExpression from "../components/TemplatesExpression"; 
import { useEffect, useState } from "react";
import BarbeariaService from "../services/BarbeariaService";
import { Barbearia } from "../models/Barbearia";
import { Header } from "../components/Header";
import { Image } from "../components/Image";

export const BarbeariaPage = () => {
    const {idExterno} = useParams<{ idExterno: string }>();
    const [barbearia, setBarbearia] = useState<Barbearia>();
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const urlImage = "https://visaoempreendedora.com.br/wp-content/uploads/2024/05/curso-barbeiro.png";

    useEffect(() => {
        const fetchBarbearia:any = async () => {
            try {
                const idExternoString:string = idExterno as string
                const data = await BarbeariaService.pesquisarBarbeariaPorId(idExternoString);
                setBarbearia(data);
            } catch (error) {
                setError('Erro ao buscar Barbearia.');
            } finally {
                setLoading(false);
            }
        };

        fetchBarbearia();
    }, []);

    if (loading) {
        return <p>Carregando...</p>;
    }

    if (error) {
        return <p>{error}</p>;
    }

    return (
        <div>
            <Header />
            <Row className="d-flex align-items-center justify-content-center">
                <Col sm={12} md={6} lg={6} className="mb-4 d-flex justify-content-center text-center">
                    <h1>Página da Barbearia {barbearia?.nome}</h1>
                    <p>Detalhes da Barbearia {barbearia?.endereco}.</p>
                </Col>
                <Col sm={12} md={6} lg={6} className="mb-4 d-flex justify-content-center">
                    <Image src={urlImage} alt="Barbeiro cortando cabelo de cliente" height="400px" rounded />
                </Col>
            </Row>
        </div>
    );
}