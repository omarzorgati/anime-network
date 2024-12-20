import { Injectable } from '@angular/core';
import {lastValueFrom} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {TokenService} from "./token.service";
import {CookieService} from "ngx-cookie-service";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http:HttpClient,private TokenService:TokenService,private cookieService:CookieService ) { }

  Register(user: any) {
    return lastValueFrom(this.http.post<any>(environment.baseUrl + '/auth/register', user))
  }

  SignIn(user: any) {
    return lastValueFrom(this.http.post<any>(environment.baseUrl + '/auth/authenticate', user))
  }

  CurrentUser() {
    return lastValueFrom(this.http.get<any>(environment.baseUrl + '/auth/CurrentUser'))
  }

  Logout(){
    this.TokenService.isLogin = false;
    this.TokenService.roleAs = "";
    this.cookieService.set("STATE_DASH_ANIME", "false");
    this.cookieService.set("ROLE_DASH_ANIME", "");
    this.cookieService.set("TOKEN_DASH_ANIME", "");

  }

}
