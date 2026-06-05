export interface TipoCultivo {
  id: number;
  nombre: string;
  nombreCientifico?: string;
  descripcion?: string;
  iconoUrl?: string;
  tempOptimaMin?: number;
  tempOptimaMax?: number;
  tempCriticaMin?: number;
  tempCriticaMax?: number;
  humedadSueloOptimaMin?: number;
  humedadSueloOptimaMax?: number;
  humedadSueloCriticaMin?: number;
  humedadSueloCriticaMax?: number;
  humedadAmbOptimaMin?: number;
  humedadAmbOptimaMax?: number;
  luminosidadOptimaMin?: number;
  luminosidadOptimaMax?: number;
  recomendacionRiego?: string;
  recomendacionHelada?: string;
  recomendacionEstresHidrico?: string;
  recomendacionGeneral?: string;
}

export interface TipoCultivoResumen {
  id: number;
  nombre: string;
  nombreCientifico?: string;
  descripcion?: string;
  iconoUrl?: string;
}