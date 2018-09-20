import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class ShedService {

  private url = environment.url + "/api/v1/shed";

  constructor(private _httpClient: HttpClient, private _router: Router) { }

  public findAll() {
    const HEADERS = new HttpHeaders().set('Content-Type', 'application/json');
    return this._httpClient.get<Array<Shed>>(this.url,
      {
        headers: HEADERS
      }
    );
  }
  
}
