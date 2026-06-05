export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegistroRequest {
  nombre: string;
  apellidos: string;
  email: string;
  password: string;
  telefono?: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  tokenTipo: string;
  expiresIn: number;
  usuarioId: number;
  nombre: string;
  email: string;
  rol: RolNombre;
}

export interface RefreshTokenRequest {
  refreshToken: string;
}

export type RolNombre = 'ROLE_ADMIN' | 'ROLE_OWNER' | 'ROLE_WORKER';