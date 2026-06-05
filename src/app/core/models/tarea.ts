export type EstadoTarea = 'PENDIENTE' | 'EN_PROGRESO' | 'COMPLETADA' | 'CANCELADA';
export type PrioridadTarea = 'BAJA' | 'MEDIA' | 'ALTA' | 'URGENTE';
export type TipoTarea = 'RIEGO' | 'FERTILIZACION' | 'PODA' | 'RECOLECCION' | 'MANTENIMIENTO' | 'OTRO';

export interface Tarea {
  id: number;
  parcelaId: number;
  parcelaNombre: string;
  cultivoId?: number;
  cultivoNombre?: string;
  asignadoAId?: number;
  asignadoANombre?: string;
  titulo: string;
  descripcion?: string;
  tipo: TipoTarea;
  prioridad: PrioridadTarea;
  estado: EstadoTarea;
  fechaPrevista: string;
  fechaCompletada?: string;
  notasCompletado?: string;
  createdAt: string;
}

export interface CrearTareaRequest {
  parcelaId: number;
  cultivoId?: number;
  asignadoAId?: number;
  titulo: string;
  descripcion?: string;
  tipo: TipoTarea;
  prioridad?: PrioridadTarea;
  fechaPrevista: string;
}

export interface ActualizarTareaRequest {
  titulo?: string;
  descripcion?: string;
  prioridad?: PrioridadTarea;
  estado?: EstadoTarea;
  fechaPrevista?: string;
  asignadoAId?: number;
  notasCompletado?: string;
}