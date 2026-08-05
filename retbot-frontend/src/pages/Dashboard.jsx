import { Link } from 'react-router-dom';
import useAuth from '../hooks/useAuth';

export default function Dashboard() {
  const { utilizador, logout } = useAuth();

  const modulos = [
    {
      titulo: 'Utilizadores',
      descricao: 'Gerir utilizadores, perfis e permissões de acesso.',
      rota: '/utilizadores',
      icone: '👥',
    },
    {
      titulo: 'Contas Sociais',
      descricao: 'Conectar e gerir perfis de redes sociais vinculadas.',
      rota: '/contas',
      icone: '🔗',
    },
    {
      titulo: 'Publicações',
      descricao: 'Criar e gerir publicações para as redes sociais.',
      rota: '/publicacoes',
      icone: '📝',
    },
    {
      titulo: 'Agendamentos',
      descricao: 'Calendário de publicações e programação automática.',
      rota: '/agendamentos',
      icone: '📅',
    },
    {
      titulo: 'Automação',
      descricao: 'Configurar respostas automáticas e fluxos de bots.',
      rota: '/automacao',
      icone: '⚡',
    },
  ];

  return (
    <div style={{ maxWidth: '1000px', margin: '2rem auto', padding: '0 1rem', fontFamily: 'sans-serif' }}>
      <header
        style={{
          display: 'flex',
          justify: 'space-between',
          alignItems: 'center',
          marginBottom: '2.5rem',
          borderBottom: '1px solid #e2e8f0',
          paddingBottom: '1rem',
        }}
      >
        <div>
          <h1 style={{ margin: 0, fontSize: '1.8rem', color: '#1a202c' }}>Painel Principal</h1>
          <p style={{ margin: '0.25rem 0 0', color: '#718096' }}>
            Bem-vindo, <strong>{utilizador?.nome || 'Utilizador'}</strong>
          </p>
        </div>
        <button
          onClick={logout}
          style={{
            padding: '0.5rem 1rem',
            backgroundColor: '#ef4444',
            color: '#fff',
            border: 'none',
            borderRadius: '6px',
            cursor: 'pointer',
            fontWeight: '600',
          }}
        >
          Sair
        </button>
      </header>

      <div
        style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fit, minmax(260px, 1fr))',
          gap: '1.5rem',
        }}
      >
        {modulos.map((modulo) => (
          <Link
            key={modulo.rota}
            to={modulo.rota}
            style={{
              display: 'flex',
              flexDirection: 'column',
              padding: '1.5rem',
              borderRadius: '10px',
              border: '1px solid #e2e8f0',
              backgroundColor: '#ffffff',
              textDecoration: 'none',
              color: 'inherit',
              boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.05)',
              transition: 'transform 0.15s ease, box-shadow 0.15s ease',
            }}
            onMouseEnter={(e) => {
              e.currentTarget.style.transform = 'translateY(-3px)';
              e.currentTarget.style.boxShadow = '0 10px 15px -3px rgba(0, 0, 0, 0.1)';
            }}
            onMouseLeave={(e) => {
              e.currentTarget.style.transform = 'translateY(0)';
              e.currentTarget.style.boxShadow = '0 4px 6px -1px rgba(0, 0, 0, 0.05)';
            }}
          >
            <span style={{ fontSize: '2rem', marginBottom: '0.75rem' }}>{modulo.icone}</span>
            <h3 style={{ margin: '0 0 0.5rem', color: '#2d3748', fontSize: '1.25rem' }}>{modulo.titulo}</h3>
            <p style={{ margin: 0, color: '#718096', fontSize: '0.9rem', lineHeight: '1.4' }}>{modulo.descricao}</p>
          </Link>
        ))}
      </div>
    </div>
  );
}