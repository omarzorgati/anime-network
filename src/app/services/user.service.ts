import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {CookieService} from "ngx-cookie-service";
import {lastValueFrom} from "rxjs";
import {environment} from "../../environments/environment";
import {TokenService} from "./token.service";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http:HttpClient,private TokenService:TokenService,private cookieService:CookieService ) { }

  all_users(size : any , page : any ) {
    return lastValueFrom(this.http.get<any>(environment.baseUrl+ `/admin/all-users` ,
      {params : new HttpParams().append('page' , page).append('size',size)}))
  }

  elevate(email : any) {
    return lastValueFrom(this.http.post<any>(environment.baseUrl+ `/admin/elevate` ,null,
      {params : new HttpParams().append('email' , email)}))
  }
  delete(id : any) {
    return lastValueFrom(this.http.delete<any>(environment.baseUrl+ `/admin/delete-user/${id}`))
  }

}
