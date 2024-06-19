import React, { useEffect, useState } from "react";
import { Card, Col, Container, Row } from "react-bootstrap";
import { Header } from "../components/Header";
import { SearchBar } from "../components/SearchBar";
import { Footer } from "../components/Footer";
import { Image } from "../components/Image";
import { BarbeariaService } from "../services/BarbeariaService";
import { Barbearia } from "../models/Barbearia";
import { Link } from "react-router-dom";

export const Inicio: React.FC = () => {
    const urlImage = "https://visaoempreendedora.com.br/wp-content/uploads/2024/05/curso-barbeiro.png";
    const [barbearias, setBarbearias] = useState<Barbearia[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchBarbearias = async () => {
            try {
                const data = await BarbeariaService.pesquisarTodasAsBarbearias();
                setBarbearias(data);
            } catch (error) {
                setError('Erro ao buscar Barbearias.');
            } finally {
                setLoading(false);
            }
        };

        fetchBarbearias();
    }, []);

    const handleSearchResults = (results: Barbearia[]) => {
        setBarbearias(results);
    }

    if (loading) {
        return <p>Carregando...</p>;
    }

    if (error) {
        return <p>{error}</p>;
    }

    return (
        <div>
            <Header />            
            <Container className="my-4">
                <Row className="d-flex align-items-center justify-content-center">
                    <Col sm={12} md={6} lg={6} className="mb-4 d-flex justify-content-center text-center">
                        <h1>Encontre a barbearia mais próxima de você!</h1>
                    </Col>
                    <Col sm={12} md={6} lg={6} className="mb-4 d-flex justify-content-center">
                        <Image src={urlImage} alt="Barbeiro cortando cabelo de cliente" height="400px" rounded />
                    </Col>
                </Row>
                <Row className="d-flex justify-content-center mt-4">
                    <Col sm={12} md={8} lg={6} className="d-flex justify-content-center">
                        <SearchBar onResults={handleSearchResults}/>
                    </Col>
                </Row>
                <Row className="mt-4">
                    {barbearias.length === 0 ? (
                        <Col className="text-center">
                            <p>Nenhuma barbearia com esse nome foi encontrada.</p>
                        </Col>
                    ) : (
                        barbearias.map((barbearia) => (
                            <Col key={barbearia.idExterno} sm={12} md={6} lg={3} className="mb-4">
                                <Link to={`/barbearia/${barbearia.idExterno}`}  style={{ textDecoration: 'none' }}>
                                    <Card>
                                        <Card.Body>
                                            <Card.Title>{barbearia.nome}</Card.Title>
                                            <Card.Text>{barbearia.endereco}</Card.Text>
                                        </Card.Body>
                                    </Card>
                                </Link>
                            </Col>
                        ))
                    )}
                </Row>
            </Container>
            <Footer />
        </div>
    );
}