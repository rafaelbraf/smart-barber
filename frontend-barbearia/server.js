require('dotenv').config();
const express = require('express');
const path = require('path');
const app = express();

app.use(express.static(path.join(__dirname, 'public')));

app.get('/config', (req, res) => {
    const config = {
        apiUrl: process.env.API_URL
    };

    res.status(200).json(config);
});

const port = process.env.PORT || 3000;

app.listen(port, () => {
    console.log(`Servidor iniciado na porta ${port}`);
});