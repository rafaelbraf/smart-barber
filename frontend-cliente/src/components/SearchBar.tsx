import React, { useState } from "react";

export const SearchBar: React.FC = () => {
    const [searchTerm, setSearchTerm] = useState<string>("");

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setSearchTerm(event.target.value);
    }

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        const apiUrl = process.env.REACT_APP_BACKEND_API_URL;

        try {
            const response = await fetch(`${apiUrl}/barbearias/nome=${searchTerm}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            console.log(response);
        } catch (error) {
            console.error("Error fetching search results:", error);
        }
    }

    return (
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
            <button className="btn btn-outline-success my-2 my-sm-0 mt-0" type="submit">Pesquisar</button>
        </form>
    );
}