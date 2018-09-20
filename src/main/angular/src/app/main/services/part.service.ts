import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class PartService {

  private url = environment.url + "/api/v1/part";

  constructor(private _httpClient: HttpClient, private _router: Router) { }

  public findAll() {
    const HEADERS = new HttpHeaders().set('Content-Type', 'application/json');
    return this._httpClient.get<Array<Part>>(this.url,
      {
        headers: HEADERS
      }
    );
  }
  
}
