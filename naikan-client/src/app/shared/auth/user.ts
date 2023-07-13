export interface User {
  username: string;
  authorities?: string[];
  favorites?: string[];
  details?: Details;
}

export interface Details {
  title?: string;
  displayName?: string;
  givenName?: string;
  mail?: string;
  mobile?: string;
  telephoneNumber?: string;
}