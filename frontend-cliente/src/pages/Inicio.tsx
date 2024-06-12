import React from "react";
import { Container } from "react-bootstrap";
import { Header } from "../components/Header";
import { SearchBar } from "../components/SearchBar";
import { Footer } from "../components/Footer";

export const Inicio = () => {
    return (
        <div>
            <Header />
            <Container className="my-4">
                <h1>Encontre a barbearia mais próxima de você!</h1>
                <SearchBar />
            </Container>
            <Footer />
        </div>
    );
}