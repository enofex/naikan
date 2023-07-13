declare const require: any;

export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api',
  version: require('../../package.json').version
};
