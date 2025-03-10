import { JsonPipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { AuthService } from '../../auth/service/auth.service';

@Component({
  selector: 'app-admin-dashboard',
  imports: [JsonPipe],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent implements OnInit {
  constructor(private authService: AuthService) { }

  userRoles: string[] = [];
  http= inject(HttpClient);
  employeeList: any[] = [];

  ngOnInit(): void {
    this.getAllEmployees();
    this.userRoles = this.authService.getRoles();
  }

  refreshRoles() {
    this.authService.fetchRolesFromDatabase().subscribe((roles: string[]) => {
      this.userRoles = roles;
      localStorage.setItem('roles', JSON.stringify(roles));
    });
  }

  //create a seperate views for getall employees and add employee

  getAllEmployees(){
    this.http.get('http://localhost:8080/api/employees').subscribe((data:any) => {
      this.employeeList = data.content;
  });
  }

}
