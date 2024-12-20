import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {lastValueFrom} from "rxjs";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class AnimeService {

  constructor(private http:HttpClient) { }

  all_anime(size: any, page: any, animeTitle?: any, season?: any, status?: any, animeType?: any, categories?: string[]) {
    let params = new HttpParams()
      .append('size', size)
      .append('page', page);

    // Append optional parameters only if they are provided
    if (animeTitle) {
      params = params.append('animeTitle', animeTitle);
    }
    if (season) {
      params = params.append('season', season);
    }
    if (status) {
      params = params.append('status', status);
    }
    if (animeType) {
      params = params.append('animeType', animeType);
    }
    if (categories && categories.length > 0) {
      params = params.append('categories', categories.join(','));
    }

    return lastValueFrom(this.http.get<any>(environment.baseUrl + `/anime/filter`, { params }));
  }

  most_liked(size: any, page: any,) {
    let params = new HttpParams()
      .append('size', size)
      .append('page', page);



    return lastValueFrom(this.http.get<any>(environment.baseUrl + `/anime/most-liked`, { params }));
  }

  like_anime(animeId:any) {
    return lastValueFrom(this.http.post<any>(environment.baseUrl + `/anime/like/${animeId}`,null));
  }


  add_anime(data : any) {
    return lastValueFrom(this.http.post<any>(environment.baseUrl+ `/anime/create`,data))
  }

}
