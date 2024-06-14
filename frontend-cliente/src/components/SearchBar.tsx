import React, { useState } from "react";
import { BarbeariaService } from "../services/BarbeariaService";
import { Container, Form } from "react-bootstrap";
import { Barbearia } from "../models/Barbearia";

interface SearchBarProps {
    onResults: (results: Barbearia[]) => void;
}

export const SearchBar: React.FC<SearchBarProps> = ({ onResults }) => {
    const [searchTerm, setSearchTerm] = useState<string>("");

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setSearchTerm(event.target.value);
    }

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        try {
            const searchResults = await BarbeariaService.pesquisarBarbeariasPorNome(searchTerm);
            onResults(searchResults);
        } catch (error) {
            console.error(error);
        }
    }

    return (
        <Container>
            <Form className="form-inline d-flex align-items-center mt-2" onSubmit={handleSubmit}>
                <Form.Control
                    className="form-control mr-2"
                    type="search"
                    placeholder="Pesquisar barbearia..."
                    aria-label="Search"
                    value={searchTerm}
                    onChange={handleChange}
                    style={{ width: "760px" }}
                />
                <button className="btn btn-primary my-2 my-sm-0 mt-0" type="submit">Pesquisar</button>
            </Form>
        </Container>
    );
}