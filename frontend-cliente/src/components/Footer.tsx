import React from "react";
import { Col, Container, Row } from "react-bootstrap";

export const Footer: React.FC = () => {
    return (
        <footer className="bg-primary text-white mt-4">
            <Container>
                <Row>
                    <Col className="py-4">
                        <h5>Click Barber</h5>
                        <p>Some footer content here.</p>
                    </Col>
                    <Col className="py-4">
                        <h5>Links</h5>
                        <ul className="list-unstyled">
                            <li><a href="/" className="text-white">Início</a></li>
                            <li><a href="/sobre" className="text-white">About</a></li>
                        </ul>
                    </Col>
                </Row>
                <Row>
                    <Col className="text-center py-3">
                        <p className="mb-0">© 2024 Click Barber. Todos os direitos reservados.</p>
                    </Col>
                </Row>
            </Container>
        </footer>
    )
};