import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';
import { Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private roleApiUrl = 'http://localhost:8080/api/employees/roles';
  private apiUrl = 'http://localhost:8080';
  private tokenKey = 'token';

  constructor(private http: HttpClient) { }

  login(username: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, { username, password }).pipe(
      tap(response => {
        localStorage.setItem(this.tokenKey, response.token); // Store token
      }),
      catchError(error => {
        console.error('Login failed:', error);
        return of({ token: null, message: error.error.message });
      })
    );
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    window.location.href = '/login';
  }

  isLoggedIn(): boolean {
    const token = localStorage.getItem(this.tokenKey);
    if (!token) return false;

    try {
      const decodedToken: any = jwtDecode(token);
      const isExpired = decodedToken.exp * 1000 < Date.now();

      if (isExpired) {
        this.logout(); // Automatically log out if expired
        return false;
      }
      return true;
    } catch (error) {
      this.logout();
      return false;
    }
  }

  getRolesFromToken(token: string): string[] {
    try {
      const decoded: any = jwtDecode(token);
      return decoded.roles || []; // ✅ Extract roles from token (if present)
    } catch (error) {
      console.error('Error decoding token:', error);
      return [];
    }
  }




  getUserRoles(): Observable<string[]> {
    const token = localStorage.getItem('helpdesk-token'); // Get JWT from local storage

    if (!token) {
      throw new Error('No token found');
    }

    let roles: string[] = this.getRolesFromToken(token);

    if (roles.length > 0) {
      // ✅ If roles exist in JWT, return them immediately
      return of(roles);
    }

    // ❌ If roles are missing, fetch from the database
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http.get<string[]>(this.roleApiUrl, { headers }).pipe(
      catchError((error) => {
        console.error('Error fetching roles:', error);
        return of([]); // Return an empty array on error
      })
    );
  }
}
