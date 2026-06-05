export type EstadoAlerta = 'ACTIVA' | 'RECONOCIDA' | 'RESUELTA';
export type SeveridadAlerta = 'BAJA' | 'MEDIA' | 'ALTA' | 'CRITICA';
export type TipoOrigen = 'MANUAL' | 'AUTOMATICA';

export interface Alerta {
  id: number;
  parcelaId: number;
  parcelaNombre: string;
  cultivoId?: number;
  cultivoNombre?: string;
  tipoOrigen: TipoOrigen;
  tipoAlerta: string;
  severidad: SeveridadAlerta;
  mensaje: string;
  valorDetectado?: number;
  fechaDisparo: string;
  estado: EstadoAlerta;
  reconocidaEn?: string;
  resueltaEn?: string;
  createdAt: string;
}

export interface ReglaAlerta {
  id: number;
  parcelaId: number;
  parcelaNombre: string;
  nombre: string;
  descripcion?: string;
  campo: string;
  operador: string;
  valorUmbral: number;
  valorUmbralMax?: number;
  severidad: SeveridadAlerta;
  activa: boolean;
  createdAt: string;
}

export interface CrearReglaAlertaRequest {
  parcelaId: number;
  nombre: string;
  descripcion?: string;
  campo: 'TEMPERATURA' | 'HUMEDAD_SUELO' | 'HUMEDAD_AMBIENTAL' | 'LUMINOSIDAD';
  operador: 'MAYOR_QUE' | 'MENOR_QUE' | 'ENTRE';
  valorUmbral: number;
  valorUmbralMax?: number;
  severidad?: SeveridadAlerta;
}