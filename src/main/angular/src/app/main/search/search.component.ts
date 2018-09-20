import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { SearchService } from './search.service';
import { MatTableDataSource } from '@angular/material/table';
import { PrintService } from '../services/print.service';
import { saveAs } from 'file-saver';
import { MatDialog } from '@angular/material';
import { SealComponent } from './seal/seal.component';
import { LoadingService } from '../services/loading.service';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  results = 0;
  form: FormGroup;
  displayedColumns: string[] = ['protocol', 'entranceDate', 'exitDate', 'sportingPlate', 'originalPlate', 'printProtocol', 'printSeals', 'exit'];
  dataSource: MatTableDataSource<Protocol>;
  url = environment.url + '/api/v1/print/protocol';

  constructor(private _formBuilder: FormBuilder, 
              private _router: Router,
              private _searchService: SearchService,
              private _printService: PrintService,
              private _sealDialog: MatDialog,
              private _loadingService: LoadingService,
              private _httpClient: HttpClient) { }

  ngOnInit() {
    this.form = this._formBuilder.group({
      protocol: '',
      startDate: '',
      endDate: ''
    });
  }

  search() {
    this._loadingService.show();
    this._searchService.search(this.form.value).subscribe(
      suc => {
        this.results = suc.length;
        this.dataSource = new MatTableDataSource(suc);
        this._loadingService.hide();
      }
    );
  }

  printProtocol(protocol) {
    this._loadingService.show();
    var data = { 'protocol': protocol };
    const HEADERS = new HttpHeaders().set('Content-Type', 'application/json');
    this._httpClient.post<FileIdentifier>(this.url,
      data,
      {
        headers: HEADERS
      }
    ).subscribe(suc => {
      let identifier = suc.identifier;
      this._printService.printProcol(identifier).subscribe(suc => {
        saveAs(suc.body, 'protocolo.pdf');
        this._loadingService.hide();
      });    
    });
  }

  printSeals(protocol) {
    this._sealDialog.open(SealComponent, {
      data: protocol
    });
  }

  exit(protocol) {
    this._router.navigate(["/main/exit", protocol]);
  }

}
