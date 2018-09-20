import { Injectable } from '@angular/core';
import { saveAs } from 'file-saver';
import { PrintService } from '../../services/print.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { LoadingService } from '../../services/loading.service';

@Injectable({
  providedIn: 'root'
})
export class SealService {

  private url = environment.url + '/api/v1/print/seal';

  constructor(private _httpClient: HttpClient,
              private _printService: PrintService,
              private _loadingService: LoadingService) { }

  print(data) {
    this._loadingService.show();
    const HEADERS = new HttpHeaders().set('Content-Type', 'application/json');
    this._httpClient.post<FileIdentifier>(this.url,
      data,
      {
        headers: HEADERS
      }
    ).subscribe(suc => {
      let identifier = suc.identifier;
      this._printService.printSeal(identifier).subscribe(suc => {
        saveAs(suc.body, 'lacre.pdf');
        this._loadingService.hide();
      });    
    });
  }
}
