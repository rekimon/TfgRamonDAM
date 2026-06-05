import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { SensorDatos, SensorDatosRequest } from '../models/sensor-datos';
import { ApiResponse, PagedResponse } from '../models/api-response';

@Injectable({ providedIn: 'root' })
export class SensorService {

  private readonly apiUrl = `${environment.apiUrl}/sensor-datos`;

  constructor(private http: HttpClient) {}

  registrar(request: SensorDatosRequest): Observable<ApiResponse<SensorDatos>> {
    return this.http.post<ApiResponse<SensorDatos>>(this.apiUrl, request);
  }

  listarPorParcela(parcelaId: number, page = 0, size = 20):
      Observable<ApiResponse<PagedResponse<SensorDatos>>> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size);
    return this.http.get<ApiResponse<PagedResponse<SensorDatos>>>(
      `${this.apiUrl}/parcela/${parcelaId}`, { params });
  }

  obtenerUltimo(parcelaId: number): Observable<ApiResponse<SensorDatos>> {
    return this.http.get<ApiResponse<SensorDatos>>(
      `${this.apiUrl}/parcela/${parcelaId}/ultimo`);
  }
}