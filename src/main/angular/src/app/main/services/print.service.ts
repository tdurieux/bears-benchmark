import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PrintService {

  private urlProtocol = environment.url + "/api/v1/print/protocol/";
  private urlSeal = environment.url + "/api/v1/print/seal/";

  constructor(private _httpClient: HttpClient) { }

  public printProcol(id) {
    return this.print(this.urlProtocol + id);
  }

  public printSeal(id) {
    return this.print(this.urlSeal + id);
  }

  private print(url) {
    return this._httpClient.get(url,
      {
        observe: 'response',
        responseType: 'blob'
      });
  }

}
