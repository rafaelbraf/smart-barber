import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './routes/index.tsx';

import 'bootstrap/dist/css/bootstrap.min.css';
import './css/styles.css';

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
)
