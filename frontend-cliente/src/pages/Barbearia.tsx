import { Button, Container } from "react-bootstrap";
import { Link, useParams } from "react-router-dom";

export const BarbeariaPage = () => {
    const {idExterno } = useParams();
    return (
        <div>
            <h1>Página da Barbearia {idExterno}</h1>
            <p>Detalhes da Barbearia {idExterno}.</p>
        </div>
    );
}