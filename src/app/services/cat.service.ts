import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {CookieService} from "ngx-cookie-service";
import {lastValueFrom} from "rxjs";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class CatService {

  constructor(private http:HttpClient ) { }

  all_cat(size : any , page : any ) {
    return lastValueFrom(this.http.get<any>(environment.baseUrl+ `/categories/all-categories` ,
      {params : new HttpParams().append('page' , page).append('size',size)}))
  }

  add_cat(data : any) {
    return lastValueFrom(this.http.post<any>(environment.baseUrl+ `/categories/add`,data))
  }

  update_cat(data : any,id:any) {
    return lastValueFrom(this.http.put<any>(environment.baseUrl+ `/categories/update/${id}`,data))
  }

  delete_cat(id:any) {
    return lastValueFrom(this.http.delete<any>(environment.baseUrl+ `/categories/delete/${id}`,{}))
  }
}
