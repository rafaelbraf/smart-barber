import React from "react";
import { Col, Container, Row } from "react-bootstrap";
import { Header } from "../components/Header";
import { SearchBar } from "../components/SearchBar";
import { Footer } from "../components/Footer";
import { Image } from "../components/Image";

export const Inicio = () => {
    const urlImage = "https://visaoempreendedora.com.br/wp-content/uploads/2024/05/curso-barbeiro.png";

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
                        <SearchBar />
                    </Col>
                </Row>
            </Container>
            <Footer />
        </div>
    );
}