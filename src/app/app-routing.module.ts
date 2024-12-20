import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { AnimesComponent } from './pages/animes/animes.component';
import { CategoryComponent } from './pages/category/category.component';
import { AnimeDetailsComponent } from './pages/anime-details/anime-details.component';
import { ManageUserComponent } from './Admin/manage-user/manage-user.component';
import {authGuard} from "./services/auth.guard";
import {AdminDashComponent} from "./Admin/admin-dash/admin-dash.component";
import {MangeReviewComponent} from "./Admin/mange-review/mange-review.component";
import {MangeCatComponent} from "./Admin/mange-cat/mange-cat.component";
import {MangeAnimeComponent} from "./Admin/mange-anime/mange-anime.component";

const routes: Routes = [
  {
    path: 'home',
    component: HomeComponent,
    canActivate: [authGuard]  // Guard applied here
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'categories',
    component: AnimesComponent,
    canActivate: [authGuard]  // Guard applied here
  },
  {
    path: 'anime/:id',
    component: AnimeDetailsComponent,
    canActivate: [authGuard]  // Guard applied here
  },
  {
    path: 'categories/:category',
    component: CategoryComponent,
    canActivate: [authGuard]  // Guard applied here
  },
  {
    path: 'Admin/Manage/User',
    component: ManageUserComponent,
    canActivate: [authGuard]  // Guard applied here
  },
  {
    path: 'Admin/Manage/Review',
    component: MangeReviewComponent,
    canActivate: [authGuard]  // Guard applied here
  },
  {
    path: 'Admin/Manage/Category',
    component: MangeCatComponent,
    canActivate: [authGuard]  // Guard applied here
  },
  {
    path: 'Admin/Manage/Anime',
    component: MangeAnimeComponent,
    canActivate: [authGuard]  // Guard applied here
  },
  {
    path: 'Admin/Dashboard',
    component: AdminDashComponent,
    canActivate: [authGuard]  // Guard applied here
  },
  {
    path: '**',
    redirectTo: 'home'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
