import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import './index.css'
import App from './App.jsx'
import { AuthProvider } from './contexts/AuthContext.jsx'
import { LoggerProvider } from "./contexts/LoggerProvider.jsx";
import './styles/variables.css';
import './styles/reset.css';
import './styles/layout.css';
import './styles/components.css';

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <BrowserRouter>
      <LoggerProvider>
        <AuthProvider>
          <App />
        </AuthProvider>
      </LoggerProvider>
    </BrowserRouter>
  </StrictMode>,
)