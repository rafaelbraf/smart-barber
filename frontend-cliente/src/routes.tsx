import { Route, Routes } from "react-router-dom";
import { Inicio } from "./pages/Inicio";
import { Sobre } from "./pages/Sobre";


const AppRoutes = () => {
    return (
        <Routes>
            <Route path="/" element={ <Inicio /> } />
            <Route path="/sobre" element={ <Sobre /> } />
        </Routes>
    );
}

export default AppRoutes;