import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Tarea, CrearTareaRequest, ActualizarTareaRequest } from '../models/tarea';
import { ApiResponse, PagedResponse } from '../models/api-response';

@Injectable({ providedIn: 'root' })
export class TareaService {

  private readonly apiUrl = `${environment.apiUrl}/tareas`;

  constructor(private http: HttpClient) {}

  listar(parcelaId?: number, estado?: string,
    prioridad?: string, page = 0, size = 10):
    Observable<ApiResponse<PagedResponse<Tarea>>> {
  let params = new HttpParams()
    .set('page', page)
    .set('size', size);
  if (parcelaId != null) params = params.set('parcelaId', parcelaId);
  if (estado != null) params = params.set('estado', estado);
  if (prioridad != null) params = params.set('prioridad', prioridad);
  return this.http.get<ApiResponse<PagedResponse<Tarea>>>(
    this.apiUrl, { params });
}

  crear(request: CrearTareaRequest): Observable<ApiResponse<Tarea>> {
    return this.http.post<ApiResponse<Tarea>>(this.apiUrl, request);
  }

  actualizar(id: number, request: ActualizarTareaRequest):
      Observable<ApiResponse<Tarea>> {
    return this.http.put<ApiResponse<Tarea>>(
      `${this.apiUrl}/${id}`, request);
  }

  eliminar(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${id}`);
  }
}