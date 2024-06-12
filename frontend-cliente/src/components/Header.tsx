import { Link } from "react-router-dom";

export const Header = () => {
    return (
        <nav className="navbar navbar-expand-lg navbar-primary bg-primary text-white">
            <div className="container">
                <Link className="navbar-brand" to="/">Click Barber</Link>
                <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="navbarNav">
                    <ul className="navbar-nav ml-auto">
                        <li className="nav-item">
                            <Link className="nav-link" to="/">Home</Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link" to="/sobre">About</Link>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    );
}