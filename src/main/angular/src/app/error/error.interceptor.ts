import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/do';
import { ErrorComponent } from './error.component';
import { MatDialog } from '@angular/material';
import { Router } from '@angular/router';
import { LoadingService } from '../main/services/loading.service';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  constructor(private dialog: MatDialog, 
              private _router: Router,
              private _loadingService: LoadingService) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (request.url.endsWith('/oauth/token')) {
      return next.handle(request);
    }
    return next.handle(request).do((event: HttpEvent<any>) => { }, (err: any) => {
      if (err instanceof HttpErrorResponse) {
        if (err.status === 400) {
          this.openDialog({ message: err.error.message })
        } else if (err.status === 401) {
          this.openDialog({ message: "Sessão expirada" });
          this._router.navigate(["/login"]);
        } else if (err.status === 404) {
          this.openDialog({ message: "Serviço não disponível"});
        } else {
          this.openDialog({ message: "Não foi possível validar a causa do erro" })  
        }
      } else {
        this.openDialog({ code: "Indisponível", message: "Não foi possível validar a causa do erro" })
      }
      this._loadingService.hide();
    });
  }

  private openDialog(info) {
    this.dialog.open(ErrorComponent, {
      data: info
    });
  }

}