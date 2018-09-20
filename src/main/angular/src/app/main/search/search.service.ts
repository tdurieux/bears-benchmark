import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  private url = environment.url + "/api/v1/entrance/search";

  constructor(private _httpClient: HttpClient, private _router: Router) { }

  public search(data) {
    const HEADERS = new HttpHeaders().set('Content-Type', 'application/json');
    return this._httpClient.post<Array<Protocol>>(this.url,
      data,
      {
        headers: HEADERS
      }
    );
  }

}
