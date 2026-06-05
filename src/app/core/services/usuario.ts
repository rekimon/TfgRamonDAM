import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Usuario, AprobarUsuarioRequest } from '../models/usuario';
import { ApiResponse, PagedResponse } from '../models/api-response';

@Injectable({ providedIn: 'root' })
export class UsuarioService {

  private readonly apiUrl = `${environment.apiUrl}/usuarios`;

  constructor(private http: HttpClient) {}

  listar(estado?: string, rol?: string, busqueda?: string,
      page = 0, size = 15):
      Observable<ApiResponse<PagedResponse<Usuario>>> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size);
    if (estado) params = params.set('estado', estado);
    if (rol) params = params.set('rol', rol);
    if (busqueda) params = params.set('busqueda', busqueda);
    return this.http.get<ApiResponse<PagedResponse<Usuario>>>(
      this.apiUrl, { params });
  }

  obtenerPerfil(): Observable<ApiResponse<Usuario>> {
    return this.http.get<ApiResponse<Usuario>>(`${this.apiUrl}/me`);
  }

  aprobar(id: number, request: AprobarUsuarioRequest):
      Observable<ApiResponse<Usuario>> {
    return this.http.post<ApiResponse<Usuario>>(
      `${this.apiUrl}/${id}/aprobar`, request);
  }

  rechazar(id: number): Observable<ApiResponse<Usuario>> {
    return this.http.post<ApiResponse<Usuario>>(
      `${this.apiUrl}/${id}/rechazar`, {});
  }

  desactivar(id: number): Observable<ApiResponse<Usuario>> {
    return this.http.post<ApiResponse<Usuario>>(
      `${this.apiUrl}/${id}/desactivar`, {});
  }

  activar(id: number): Observable<ApiResponse<Usuario>> {
    return this.http.post<ApiResponse<Usuario>>(
      `${this.apiUrl}/${id}/activar`, {});
  }
 listarWorkers(): Observable<ApiResponse<PagedResponse<Usuario>>> {
  const params = new HttpParams()
    .set('page', 0)
    .set('size', 50);
  return this.http.get<ApiResponse<PagedResponse<Usuario>>>(
    `${this.apiUrl}/workers`, { params });
}
}