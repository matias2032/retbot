// src/components/NavigationLogger.jsx
import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';

export function NavigationLogger() {
  const location = useLocation();

  useEffect(() => {
    console.log(`[NAVEGAÇÃO] Página atual: ${location.pathname}${location.search}`);
  }, [location]);

  return null;
}