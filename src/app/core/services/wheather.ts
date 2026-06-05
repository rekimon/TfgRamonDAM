import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse } from '../models/api-response';

export interface DiaPrevision {
  fecha: string;
  diaSemana: string;
  tempMax: number;
  tempMin: number;
  tempMedia: number;
  humedad: number;
  probabilidadLluvia: number;
  descripcion: string;
  icono: string;
  viento: number;
}

export interface WeatherForecast {
  ciudad: string;
  dias: DiaPrevision[];
}

@Injectable({ providedIn: 'root' })
export class WeatherService {

  private readonly apiUrl = `${environment.apiUrl}/weather`;

  constructor(private http: HttpClient) {}

  obtenerPrevisión(lat: number, lon: number):
      Observable<ApiResponse<WeatherForecast>> {
    const params = new HttpParams()
      .set('lat', lat)
      .set('lon', lon);
    return this.http.get<ApiResponse<WeatherForecast>>(
      `${this.apiUrl}/forecast`, { params });
  }
}