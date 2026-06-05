export type CalidadCosecha = 'BAJA' | 'ESTANDAR' | 'PREMIUM';

export interface Cosecha {
  id: number;
  cultivoId: number;
  cultivoNombre: string;
  parcelaNombre: string;
  fechaCosecha: string;
  kgObtenidos: number;
  precioPorKg: number;
  ingresoTotal: number;
  calidad: CalidadCosecha;
  observaciones?: string;
  createdAt: string;
}

export interface CrearCosechaRequest {
  cultivoId: number;
  fechaCosecha: string;
  kgObtenidos: number;
  precioPorKg: number;
  calidad?: CalidadCosecha;
  observaciones?: string;
}

export interface ActualizarCosechaRequest {
  fechaCosecha?: string;
  kgObtenidos?: number;
  precioPorKg?: number;
  calidad?: CalidadCosecha;
  observaciones?: string;
}