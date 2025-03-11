import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { LayoutComponent } from './pages/layout/layout.component';
import { AuthGuard } from './auth/guards/auth.guard';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { NoAuthGuard } from './auth/guards/no-auth.guard';


export const routes: Routes = [
    {
        path:'',
        redirectTo: 'login',
        pathMatch: 'full'
    },
    {
        path:'login',
        component:LoginComponent,
        canActivate:[NoAuthGuard]
    },
    {
        path: '',
        component:LayoutComponent,
        children: [
            { path: 'dashboard',
                component: DashboardComponent,
                canActivate: [AuthGuard]
            }
        ]
    }
];
