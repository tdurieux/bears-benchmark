import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment'
import { HttpClient } from '@angular/common/http';
import { HttpHeaders } from '@angular/common/http';

@Injectable()
export class LoginService {

  private url = environment.url + "/oauth/token";

  constructor(private _httpCliente: HttpClient) { }

  public login(data) {
    let params = new URLSearchParams();
    params.append('username', data.username);
    params.append('password', data.password);
    params.append('grant_type', 'password');
    params.append('client_id', 'patiolegal');

    const HEADERS = new HttpHeaders({'Content-Type': 'application/x-www-form-urlencoded', 'Authorization': 'Basic cGF0aW9sZWdhbDpQNFQxMEwzRzRM' })
    
    return this._httpCliente.post<Login>(this.url,
      params.toString(),
      {
        headers: HEADERS
      }
    );
  }

}