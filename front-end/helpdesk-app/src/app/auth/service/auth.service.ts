import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getRoles(): string[] {
    const token = this.getToken();
    if (!token) return [];

    const decodedToken: any = jwtDecode(token);
    return decodedToken.roles || []; // Get roles from JWT first
  }

  fetchRolesFromDatabase() {
    return this.http.get<string[]>('http://localhost:8080/api/employees/roles'); //!add a controller to fetch roles
  }
}
