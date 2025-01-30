export const environment = {
  production: true,
  apiPort: '8000', // Puedes cambiar este puerto para producción si lo necesitas
  get apiUrl() {
    return `http://localhost:${this.apiPort}/`;  // En producción probablemente querrás cambiar también el host
  }
};
