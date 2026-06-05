import { TipoCultivoResumen } from './tipo-cultivo';

export type EstadoCultivo = 'ACTIVO' | 'FINALIZADO' | 'PERDIDO' | 'EN_ESPERA' | 'LISTO_COSECHA';

export interface Cultivo {
  id: number;
  parcelaId: number;
  parcelaNombre: string;
  tipoCultivo: TipoCultivoResumen;
  nombrePersonalizado?: string;
  fechaSiembra: string;
  fechaCosechaEstimada?: string;
  estado: EstadoCultivo;
  notas?: string;
  activo: boolean;
  createdAt: string;
}

export interface CrearCultivoRequest {
  parcelaId: number;
  tipoCultivoId: number;
  nombrePersonalizado?: string;
  fechaSiembra: string;
  fechaCosechaEstimada?: string;
  notas?: string;
}

export interface ActualizarCultivoRequest {
  nombrePersonalizado?: string;
  fechaCosechaEstimada?: string;
  estado?: EstadoCultivo;
  notas?: string;
}