import { Routes, Route } from 'react-router-dom'
import ProtectedRoute from './routes/ProtectedRoute'
import './App.css'

function App() {
  return (
    <Routes>
      <Route path="/login" element={<div>Página de login (por construir)</div>} />

      <Route element={<ProtectedRoute />}>
        <Route path="/" element={<div>Área autenticada (por construir)</div>} />
      </Route>
    </Routes>
  )
}

export default App