import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MainService {

  private url = environment.url + "/api/v1/logout";

  constructor(private _httpCliente: HttpClient) { }

  public logout() {
    const HEADERS = new HttpHeaders({'Content-Type': 'application/json'});
    
    return this._httpCliente.delete(this.url,
      {
        headers: HEADERS
      }
    );
  }
}
