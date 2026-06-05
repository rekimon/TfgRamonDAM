export interface Parcela {
  id: number;
  ownerId: number;
  ownerNombre: string;
  nombre: string;
  descripcion?: string;
  superficieHa: number;
  latitud: number;
  longitud: number;
  municipio?: string;
  provincia?: string;
  referenciaCatastral?: string;
  activa: boolean;
  createdAt: string;
}

export interface CrearParcelaRequest {
  nombre: string;
  descripcion?: string;
  superficieHa: number;
  latitud: number;
  longitud: number;
  municipio?: string;
  provincia?: string;
  referenciaCatastral?: string;
}

export interface ActualizarParcelaRequest {
  nombre?: string;
  descripcion?: string;
  superficieHa?: number;
  latitud?: number;
  longitud?: number;
  municipio?: string;
  provincia?: string;
  referenciaCatastral?: string;
}