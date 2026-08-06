import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import terminal from 'vite-plugin-terminal';

export default defineConfig({
  plugins: [
    react(),
    terminal({
      console: 'remote', // Redireciona os console.log() normais diretamente para o terminal
    }),
  ],
});