import { Routes, Route } from 'react-router-dom'
import ProtectedRoute from './routes/ProtectedRoute'
import Login from './pages/Login'
import ContasSociais from './pages/ContasSociais'
import './App.css'

function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />

      <Route element={<ProtectedRoute />}>
        <Route path="/" element={<div>Área autenticada (por construir)</div>} />
        <Route path="/contas" element={<ContasSociais />} />
      </Route>
    </Routes>
  )
}

export default App