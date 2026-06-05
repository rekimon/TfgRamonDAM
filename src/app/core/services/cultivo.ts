import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Cultivo, CrearCultivoRequest, ActualizarCultivoRequest } from '../models/cultivo';
import { ApiResponse, PagedResponse } from '../models/api-response';

@Injectable({ providedIn: 'root' })
export class CultivoService {

  private readonly apiUrl = `${environment.apiUrl}/cultivos`;

  constructor(private http: HttpClient) {}

  listarPorParcela(parcelaId: number, estado?: string, page = 0, size = 10):
      Observable<ApiResponse<PagedResponse<Cultivo>>> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size);
    if (estado) params = params.set('estado', estado);
    return this.http.get<ApiResponse<PagedResponse<Cultivo>>>(
      `${this.apiUrl}/parcela/${parcelaId}`, { params });
  }

  obtener(id: number): Observable<ApiResponse<Cultivo>> {
    return this.http.get<ApiResponse<Cultivo>>(`${this.apiUrl}/${id}`);
  }

  crear(request: CrearCultivoRequest): Observable<ApiResponse<Cultivo>> {
    return this.http.post<ApiResponse<Cultivo>>(this.apiUrl, request);
  }

  actualizar(id: number, request: ActualizarCultivoRequest):
      Observable<ApiResponse<Cultivo>> {
    return this.http.put<ApiResponse<Cultivo>>(
      `${this.apiUrl}/${id}`, request);
  }

  eliminar(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${id}`);
  }
}