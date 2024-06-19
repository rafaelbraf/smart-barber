import { Route, Routes } from "react-router-dom";
import { Inicio } from "./pages/Inicio";
import { Sobre } from "./pages/Sobre";
import { BarbeariaPage } from "./pages/Barbearia";


const AppRoutes = () => {
    return (
        <Routes>
            <Route path="/" element={ <Inicio /> } />
            <Route path="/barbearia/:idExterno" element={ <BarbeariaPage /> } />
            <Route path="/sobre" element={ <Sobre /> } />
        </Routes>
    );
}

export default AppRoutes;