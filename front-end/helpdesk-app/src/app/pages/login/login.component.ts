import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../auth/service/auth.service';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  loginObj: any = {
    "username": "",
    "password": ""
  }
  
  authService = inject(AuthService)
  router = inject(Router)

  onLogin() {
    this.authService.login(this.loginObj.username, this.loginObj.password).subscribe({
      next: (data) => {
        if (data) {
          alert(data.message);
          this.router.navigate(['dashboard']); // Redirect to dashboard
        } else {
          alert(data.message);
        }
      },
      error: (err) => {
        if (err.status === 401) {
          alert('Invalid username or password');
        } else {
          alert('Something went wrong. Please try again later.');
        }
      }
    });
}
}
