import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "./pages/home/home.component";
import {LoginComponent} from "./pages/login/login.component";
import {RegisterComponent} from "./pages/register/register.component";
import {AnimesComponent} from "./pages/animes/animes.component";
import {CategoryComponent} from "./pages/category/category.component";
import {AnimeDetailsComponent} from "./pages/anime-details/anime-details.component";
import { ManageUserComponent } from './Admin/manage-user/manage-user.component';

const routes: Routes = [
  {
    path:'home',component:HomeComponent
  },
  {
    path:'login',component:LoginComponent
  },
  {
    path:'register',component:RegisterComponent
  },
  {
    path:'categories',component:AnimesComponent
  },
  {
    path:'anime/:id',component:AnimeDetailsComponent
  },
  {
    path:'categories/:category',component:CategoryComponent
  },
  {
    path:'Admin/Manage/User',component:ManageUserComponent
  },
  {
    path:'**',redirectTo:'home'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
