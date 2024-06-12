import React, { useState } from "react";
import { BarbeariaService } from "../services/BarbeariaService";
import { Card, Col, Container, Row } from "react-bootstrap";
import { Barbearia } from "../models/Barbearia";

export const SearchBar: React.FC = () => {
    const [searchTerm, setSearchTerm] = useState<string>("");
    const [results, setResults] = useState<Barbearia[]>([]);

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setSearchTerm(event.target.value);
    }

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        try {
            const searchResults = await BarbeariaService.pesquisarBarbeariasPorNome(searchTerm);
            setResults(searchResults);
        } catch (error) {
            console.error(error);
        }
    }

    return (
        <Container>
            <form className="form-inline d-flex align-items-center mt-2" onSubmit={handleSubmit}>
                <input
                    className="form-control mr-2"
                    type="search"
                    placeholder="Pesquisar barbearia..."
                    aria-label="Search"
                    value={searchTerm}
                    onChange={handleChange}
                    style={{ width: "760px" }}
                />
                <button className="btn btn-primary my-2 my-sm-0 mt-0" type="submit">Pesquisar</button>
            </form>
            <Row className="mt-4">
                {results.map((barbearia) => (
                    <Col key={barbearia.id} sm={12} md={6} lg={4} className="mb-4">
                        <Card>
                            <Card.Body>
                                <Card.Title>{barbearia.nome}</Card.Title>
                                <Card.Text>{barbearia.endereco}</Card.Text>
                            </Card.Body>
                        </Card>
                    </Col>
                ))}
            </Row>
        </Container>
    );
}