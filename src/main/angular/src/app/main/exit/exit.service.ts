import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ExitService {

  private url = environment.url + "/api/v1/exit";

  constructor(private _httpCliente: HttpClient) { }

  public save(data) {
    const HEADERS = new HttpHeaders().set('Content-Type', 'application/json');
    return this._httpCliente.post<string>(this.url,
      data,
      {
        headers: HEADERS
      }
    );
  }
  
}
