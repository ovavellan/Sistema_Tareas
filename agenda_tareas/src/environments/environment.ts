export const environment = {
  production: false,
  apiPort: '8080',
  get apiUrl() {
    return `http://localhost:${this.apiPort}/`;
  }
};
