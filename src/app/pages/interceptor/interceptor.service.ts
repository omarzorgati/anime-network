import { Injectable } from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {TokenService} from "../../services/token.service";
import {Observable, tap} from "rxjs";
import {environment} from "../../../environments/environment";
import {GlobalVariables} from "../../global-variables";

@Injectable({
  providedIn: 'root'
})
export class InterceptorService   implements HttpInterceptor  {

  token: string = '';

  constructor(private tokenService:TokenService,
              private glovar:GlobalVariables) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    this.token = this.tokenService.getToken();
    let url = req.url;
    let isPublic = url.includes(environment.baseUrl + '/api/v1/public/');
    let isOther = !url.includes(environment.baseUrl);

    if (!isPublic && !isOther && this.token) {
      const tokenizedReq = req.clone({
        setHeaders: { 'Authorization': `Bearer ${this.token}` }
      });
      return next.handle(tokenizedReq).pipe(
        tap({
          error: (err) => {
            if (err instanceof HttpErrorResponse) {
              this.glovar.handleHttpError(err);
            }
          }
        })
      );
    }

    return next.handle(req).pipe(
      tap({
        error: (err) => {
          if (err instanceof HttpErrorResponse) {
            this.glovar.handleHttpError(err);
          }
        }
      })
    );
  }

}
