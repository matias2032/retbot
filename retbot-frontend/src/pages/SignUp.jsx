import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import utilizadorService from '../services/utilizadorService';

function SignUp() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    nome: '',
    email: '',
    senha: '',
  });
  const [erro, setErro] = useState(null);
  const [aEnviar, setAEnviar] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErro(null);
    setAEnviar(true);

    try {
      await utilizadorService.criar(formData);
      navigate('/login', { state: { mensagem: 'Conta criada com sucesso! Faça login.' } });
    } catch (err) {
      setErro(err.response?.data?.mensagem || 'Erro ao criar conta. Tente novamente.');
    } finally {
      setAEnviar(false);
    }
  };

  return (
    <div>
      <h1>Criar Conta</h1>

      <form onSubmit={handleSubmit}>
        <label htmlFor="nome">Nome Completo</label>
        <input
          id="nome"
          name="nome"
          type="text"
          value={formData.nome}
          onChange={handleChange}
          required
        />

        <label htmlFor="email">Email</label>
        <input
          id="email"
          name="email"
          type="email"
          value={formData.email}
          onChange={handleChange}
          required
          autoComplete="email"
        />

        <label htmlFor="senha">Senha</label>
        <input
          id="senha"
          name="senha"
          type="password"
          value={formData.senha}
          onChange={handleChange}
          minLength={8}
          required
          autoComplete="new-password"
        />

        {erro && <p role="alert">{erro}</p>}

        <button type="submit" disabled={aEnviar}>
          {aEnviar ? 'A registar...' : 'Registar'}
        </button>
      </form>

      <p>
        Já tem uma conta? <Link to="/login">Entrar</Link>
      </p>
    </div>
  );
}

export default SignUp;