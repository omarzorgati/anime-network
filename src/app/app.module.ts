import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './include/header/header.component';
import { HomeComponent } from './pages/home/home.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatDialogModule} from "@angular/material/dialog";
import { FooterComponent } from './include/footer/footer.component';
import {LoginComponent} from "./pages/login/login.component";
import { RegisterComponent } from './pages/register/register.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { AnimesComponent } from './pages/animes/animes.component';
import { CategoryComponent } from './pages/category/category.component';
import { AnimeDetailsComponent } from './pages/anime-details/anime-details.component';
import { NgxStarRatingModule } from 'ngx-star-rating';
import { ManageUserComponent } from './Admin/manage-user/manage-user.component';
import {InterceptorService} from "./pages/interceptor/interceptor.service";
import {ToastrModule} from "ngx-toastr";
import { AdminDashComponent } from './Admin/admin-dash/admin-dash.component';
import { MangeReviewComponent } from './Admin/mange-review/mange-review.component';
import { MangeCatComponent } from './Admin/mange-cat/mange-cat.component';
import { MangeAnimeComponent } from './Admin/mange-anime/mange-anime.component';
import {PaginatorModule} from "primeng/paginator";
import { NewCatComponent } from './Admin/mange-cat/new-cat/new-cat.component';
import { UpdateCatComponent } from './Admin/mange-cat/update-cat/update-cat.component';
import { NewAnimeComponent } from './Admin/mange-anime/new-anime/new-anime.component';
@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    HomeComponent,
    FooterComponent,
    LoginComponent,
    RegisterComponent,
    AnimesComponent,
    CategoryComponent,
    AnimeDetailsComponent,
    ManageUserComponent,
    AdminDashComponent,
    MangeReviewComponent,
    MangeCatComponent,
    MangeAnimeComponent,
    NewCatComponent,
    UpdateCatComponent,
    NewAnimeComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatDialogModule,
    FormsModule,
    NgxStarRatingModule,
    ReactiveFormsModule,
    ToastrModule.forRoot({
      timeOut: 3000,
      positionClass: 'toast-top-right',
      preventDuplicates: true,
    }),
    PaginatorModule,

  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: InterceptorService, multi: true},
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
