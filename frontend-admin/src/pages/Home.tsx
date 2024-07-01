import { Button, Container } from "react-bootstrap";
import { Link } from "react-router-dom";

export const Home = () => {
    const token = localStorage.getItem('token');
    
    return (
        <Container className="my-4">
            <h1>Home</h1>
            <p>Usuário logado</p>
            <p>Token: {token}</p>
            <Link to="/">
                <Button variant="primary">retornar a página inicial</Button>
            </Link>
        </Container>
    );
}