import React from "react";
import { Button, Container } from "react-bootstrap";
import { Link } from "react-router-dom";
import { Header } from "../components/Header";
import { SearchBar } from "../components/SearchBar";

export const Inicio = () => {
    return (
        <div>
            <Header />
            <Container className="my-4">
                <h1>Welcome to the Home Page</h1>
                <p className="lead">This is the home page of our awesome application.</p>
                <Link to="/sobre">
                    <Button variant="primary">Learn more about us</Button>
                </Link>
                <SearchBar />
            </Container>            
        </div>
    );
}