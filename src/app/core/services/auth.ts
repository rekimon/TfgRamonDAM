import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  LoginRequest, RegistroRequest,
  AuthResponse, RefreshTokenRequest
} from '../models/auth';
import { ApiResponse } from '../models/api-response';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private readonly apiUrl = `${environment.apiUrl}/auth`;

  currentUser = signal<AuthResponse | null>(this.cargarUsuario());

  constructor(private http: HttpClient, private router: Router) {}

  login(request: LoginRequest): Observable<ApiResponse<AuthResponse>> {
    return this.http.post<ApiResponse<AuthResponse>>(
      `${this.apiUrl}/login`, request
    ).pipe(
      tap(res => {
        if (res.data) {
          localStorage.setItem('auth', JSON.stringify(res.data));
          this.currentUser.set(res.data);
        }
      })
    );
  }

  registro(request: RegistroRequest): Observable<ApiResponse<void>> {
    return this.http.post<ApiResponse<void>>(
      `${this.apiUrl}/registro`, request
    );
  }

  logout(): void {
    const auth = this.currentUser();
    if (auth?.refreshToken) {
      this.http.post(`${this.apiUrl}/logout`,
        { refreshToken: auth.refreshToken }).subscribe();
    }
    localStorage.removeItem('auth');
    this.currentUser.set(null);
    this.router.navigate(['/login']);
  }

  refreshToken(): Observable<ApiResponse<AuthResponse>> {
    const auth = this.currentUser();
    const request: RefreshTokenRequest = {
      refreshToken: auth?.refreshToken ?? ''
    };
    return this.http.post<ApiResponse<AuthResponse>>(
      `${this.apiUrl}/refresh`, request
    ).pipe(
      tap(res => {
        if (res.data) {
          localStorage.setItem('auth', JSON.stringify(res.data));
          this.currentUser.set(res.data);
        }
      })
    );
  }

  getToken(): string | null {
    return this.currentUser()?.accessToken ?? null;
  }

  isLoggedIn(): boolean {
    return this.currentUser() !== null;
  }

  getRol(): string | null {
    return this.currentUser()?.rol ?? null;
  }

  isAdmin(): boolean {
    return this.getRol() === 'ROLE_ADMIN';
  }

  isOwner(): boolean {
    return this.getRol() === 'ROLE_OWNER';
  }

  private cargarUsuario(): AuthResponse | null {
    const stored = localStorage.getItem('auth');
    return stored ? JSON.parse(stored) : null;
  }
}