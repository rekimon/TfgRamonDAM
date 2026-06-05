import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Alerta, ReglaAlerta, CrearReglaAlertaRequest } from '../models/alerta';
import { ApiResponse, PagedResponse } from '../models/api-response';

@Injectable({ providedIn: 'root' })
export class AlertaService {

  private readonly apiUrl = `${environment.apiUrl}/alertas`;

  constructor(private http: HttpClient) {}
listarPorParcela(parcelaId: number, estado?: string,
    severidad?: string, page = 0, size = 10):
    Observable<ApiResponse<PagedResponse<Alerta>>> {
  let params = new HttpParams()
    .set('page', page)
    .set('size', size);
  if (estado != null) params = params.set('estado', estado);
  if (severidad != null) params = params.set('severidad', severidad);
  return this.http.get<ApiResponse<PagedResponse<Alerta>>>(
    `${this.apiUrl}/parcela/${parcelaId}`, { params });
}
listarTodas(estado?: string, severidad?: string, page = 0, size = 20):
    Observable<ApiResponse<PagedResponse<Alerta>>> {
  let params = new HttpParams()
    .set('page', page)
    .set('size', size);
  if (estado) params = params.set('estado', estado);
  if (severidad) params = params.set('severidad', severidad);
  return this.http.get<ApiResponse<PagedResponse<Alerta>>>(
    this.apiUrl, { params });
}

  reconocer(id: number): Observable<ApiResponse<Alerta>> {
    return this.http.post<ApiResponse<Alerta>>(
      `${this.apiUrl}/${id}/reconocer`, {});
  }

  resolver(id: number): Observable<ApiResponse<Alerta>> {
    return this.http.post<ApiResponse<Alerta>>(
      `${this.apiUrl}/${id}/resolver`, {});
  }

  listarReglas(parcelaId: number, page = 0, size = 10):
      Observable<ApiResponse<PagedResponse<ReglaAlerta>>> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size);
    return this.http.get<ApiResponse<PagedResponse<ReglaAlerta>>>(
      `${this.apiUrl}/reglas/parcela/${parcelaId}`, { params });
  }

  crearRegla(request: CrearReglaAlertaRequest):
      Observable<ApiResponse<ReglaAlerta>> {
    return this.http.post<ApiResponse<ReglaAlerta>>(
      `${this.apiUrl}/reglas`, request);
  }

  eliminarRegla(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(
      `${this.apiUrl}/reglas/${id}`);
  }
}