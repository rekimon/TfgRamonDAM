import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Parcela, CrearParcelaRequest, ActualizarParcelaRequest } from '../models/parcela';
import { ApiResponse, PagedResponse } from '../models/api-response';

@Injectable({ providedIn: 'root' })
export class ParcelaService {

  private readonly apiUrl = `${environment.apiUrl}/parcelas`;

  constructor(private http: HttpClient) {}

  listar(busqueda?: string, page = 0, size = 10):
      Observable<ApiResponse<PagedResponse<Parcela>>> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size);
    if (busqueda) params = params.set('busqueda', busqueda);
    return this.http.get<ApiResponse<PagedResponse<Parcela>>>(
      this.apiUrl, { params });
  }

  obtener(id: number): Observable<ApiResponse<Parcela>> {
    return this.http.get<ApiResponse<Parcela>>(`${this.apiUrl}/${id}`);
  }

  crear(request: CrearParcelaRequest): Observable<ApiResponse<Parcela>> {
    return this.http.post<ApiResponse<Parcela>>(this.apiUrl, request);
  }

  actualizar(id: number, request: ActualizarParcelaRequest):
      Observable<ApiResponse<Parcela>> {
    return this.http.put<ApiResponse<Parcela>>(
      `${this.apiUrl}/${id}`, request);
  }

  eliminar(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${id}`);
  }
}