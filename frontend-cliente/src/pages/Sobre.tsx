import { Button, Container } from "react-bootstrap";
import { Link } from "react-router-dom";

export const Sobre = () => {
    return (
        <Container className="my-4">
            <h1>Sobre</h1>
            <Link to="/">
                <Button variant="primary">retornar a página inicial</Button>
            </Link>
        </Container>
    );
}