export interface SensorDatos {
  id: number;
  parcelaId: number;
  parcelaNombre: string;
  temperatura?: number;
  humedadSuelo?: number;
  humedadAmbiental?: number;
  luminosidad?: number;
  timestamp: string;
  createdAt: string;
}

export interface SensorDatosRequest {
  parcelaId: number;
  temperatura?: number;
  humedadSuelo?: number;
  humedadAmbiental?: number;
  luminosidad?: number;
  timestamp: string;
}