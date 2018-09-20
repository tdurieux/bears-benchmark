import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { PrintService } from '../../services/print.service';
import { saveAs } from 'file-saver';
import { Router } from '@angular/router';

@Component({
  selector: 'app-success',
  templateUrl: './success.component.html',
  styleUrls: ['./success.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class SuccessComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
              private _router: Router,
              private _printService: PrintService) { }

  ngOnInit() {
  }

  printProtocol(protocol) {
    this._printService.printProcol(protocol).subscribe(
      suc => {
        saveAs(suc.body, 'protocolo.pdf')
      }
    );
  }

  search() {
    this._router.navigate(["/main/search"]);
  }

}
