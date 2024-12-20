import { Injectable } from '@angular/core';
import {CookieService} from "ngx-cookie-service";

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  isLogin = false;
  roleAs?: string | null;
  token?: string | null;
  constructor(private cookieService:CookieService) { }

  isLoggedIn(): boolean {
    const loggedIn = this.cookieService.get('STATE_DASH_ANIME');
    const token = this.cookieService.get('TOKEN_DASH_ANIME');
    this.isLogin = loggedIn === 'true' && token !== '';
    return this.isLogin;
  }

  getRole() {
    this.roleAs = this.cookieService.get('ROLE_DASH_ANIME');
    return this.roleAs;
  }

  getToken() {
    this.token = this.cookieService.get('TOKEN_DASH_ANIME');
    return this.token;
  }

}
