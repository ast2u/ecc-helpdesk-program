import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { AuthService } from '../service/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private router: Router, private authService: AuthService) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const userRole = this.authService.getRoles();
    const requiredRole = route.data['role'];

    if (userRole.includes(requiredRole)) {
      return true;
    }

    this.router.navigate(['/login']); // Redirect unauthorized users
    return false;
  }
}
