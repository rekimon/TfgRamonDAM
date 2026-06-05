import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Cosecha, CrearCosechaRequest, ActualizarCosechaRequest } from '../models/cosecha';
import { ApiResponse, PagedResponse } from '../models/api-response';

@Injectable({ providedIn: 'root' })
export class CosechaService {

  private readonly apiUrl = `${environment.apiUrl}/cosechas`;

  constructor(private http: HttpClient) {}

  listarPorCultivo(cultivoId: number, page = 0, size = 10):
      Observable<ApiResponse<PagedResponse<Cosecha>>> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size);
    return this.http.get<ApiResponse<PagedResponse<Cosecha>>>(
      `${this.apiUrl}/cultivo/${cultivoId}`, { params });
  }

  listarPorParcela(parcelaId: number, desde?: string,
      hasta?: string, page = 0, size = 10):
      Observable<ApiResponse<PagedResponse<Cosecha>>> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size);
    if (desde) params = params.set('desde', desde);
    if (hasta) params = params.set('hasta', hasta);
    return this.http.get<ApiResponse<PagedResponse<Cosecha>>>(
      `${this.apiUrl}/parcela/${parcelaId}`, { params });
  }

  crear(request: CrearCosechaRequest): Observable<ApiResponse<Cosecha>> {
    return this.http.post<ApiResponse<Cosecha>>(this.apiUrl, request);
  }

  actualizar(id: number, request: ActualizarCosechaRequest):
      Observable<ApiResponse<Cosecha>> {
    return this.http.put<ApiResponse<Cosecha>>(
      `${this.apiUrl}/${id}`, request);
  }

  eliminar(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${id}`);
  }
}