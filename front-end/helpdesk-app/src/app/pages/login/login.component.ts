import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

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

  http = inject(HttpClient);
  router = inject(Router)

  onLogin() {
    this.http.post('http://localhost:8080/login', this.loginObj).subscribe((data:any) => {
      if(data.result){
        alert(data.message);
        localStorage.setItem('helpdesk-token', data.token);
        this.router.navigate(['dashboard']);
      } else {
        alert(data.message)
      }

  });
}
}
