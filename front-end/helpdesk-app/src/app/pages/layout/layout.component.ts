import { Component, inject } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { AuthService } from '../../auth/service/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-layout',
  imports: [RouterOutlet,CommonModule],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.css'
})
export class LayoutComponent {
  authService = inject(AuthService);
  router = inject(Router)

  isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  toggle(): void {
    document.querySelector("#sidebar")?.classList.toggle("collapsed");
  }

  onLogout() {
    this.authService.logout();
    this.router.navigate(['/login']); // Redirect to login page
  }
}
