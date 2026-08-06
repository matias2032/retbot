import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import utilizadorService from '../services/utilizadorService';
import { useLogger } from '../hooks/useLogger';

// Perfis atribuíveis por um admin ao criar um utilizador.
// Fallback estático enquanto não existir GET /api/v1/perfis no backend —
// mesma abordagem usada em criar_usuario_screen.dart (Flutter). Substituir
// por chamada real ao backend quando esse endpoint existir.
const PERFIS_DISPONIVEIS = [
  { idPerfil: 2, nome: 'Operador' },
];

export default function CriarUtilizador() {
  const navigate = useNavigate();
  const { logAction } = useLogger();

  const [formData, setFormData] = useState({
    nome: '',
    email: '',
    idPerfil: PERFIS_DISPONIVEIS[0].idPerfil,
  });
  const [erro, setErro] = useState(null);
  const [aEnviar, setAEnviar] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: name === 'idPerfil' ? Number(value) : value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErro(null);
    setAEnviar(true);

    logAction('SUBMIT_CRIAR_UTILIZADOR_ADMIN', {
      nome: formData.nome,
      email: formData.email,
      idPerfil: formData.idPerfil,
    });

    try {
      await utilizadorService.criar({
        nome: formData.nome,
        email: formData.email,
        senha: undefined, // backend atribui a senha padrão "12345678"
        idPerfil: formData.idPerfil,
      });

      logAction('CRIAR_UTILIZADOR_ADMIN_SUCESSO', { email: formData.email });
      navigate('/utilizadores', {
        state: { mensagem: 'Utilizador criado com sucesso. Senha inicial: 12345678' },
      });
    } catch (err) {
      const mensagemErro = err.response?.data?.mensagem || 'Erro ao criar utilizador. Tente novamente.';

      logAction('ERRO_CRIAR_UTILIZADOR_ADMIN', {
        status: err.response?.status,
        mensagem: mensagemErro,
      });

      setErro(mensagemErro);
    } finally {
      setAEnviar(false);
    }
  };

  return (
    <div className="page-container">
      <h2>Novo Utilizador</h2>

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="nome">Nome completo</label>
          <input
            id="nome"
            name="nome"
            type="text"
            value={formData.nome}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
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
        </div>

        <div className="form-group">
          <label htmlFor="idPerfil">Perfil de acesso</label>
          <select
            id="idPerfil"
            name="idPerfil"
            value={formData.idPerfil}
            onChange={handleChange}
          >
            {PERFIS_DISPONIVEIS.map((p) => (
              <option key={p.idPerfil} value={p.idPerfil}>
                {p.nome}
              </option>
            ))}
          </select>
        </div>

        {erro && <div className="alerta-erro">{erro}</div>}

        <div className="acoes">
          <button type="submit" disabled={aEnviar} className="btn-primary">
            {aEnviar ? 'A criar...' : 'Criar Utilizador'}
          </button>
          <button
            type="button"
            onClick={() => navigate('/utilizadores')}
            disabled={aEnviar}
            className="btn-secondary"
          >
            Cancelar
          </button>
        </div>
      </form>

      <p className="aviso-senha-padrao">
        Este utilizador receberá a senha inicial <strong>12345678</strong> e será
        obrigado a alterá-la no primeiro acesso.
      </p>
    </div>
  );
}