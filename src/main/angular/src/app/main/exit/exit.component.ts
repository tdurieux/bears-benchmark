import { Component, OnInit, AfterViewChecked, AfterViewInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { Validators } from '@angular/forms';
import { FormGroup } from '@angular/forms';
import * as moment from 'moment';
import { MatDialog } from '@angular/material';
import { SuccessComponent } from './success/success.component';
import { ExitService } from './exit.service';
import { LoadingService } from '../services/loading.service';

@Component({
  selector: 'app-exit',
  templateUrl: './exit.component.html',
  styleUrls: ['./exit.component.css']
})
export class ExitComponent implements OnInit {

  protocol: string;
  form: FormGroup;
  maxDate = new Date();
  taxIdentifierMask = [/\d/, /\d/, /\d/, '.', /\d/, /\d/, /\d/, '.', /\d/, /\d/, /\d/, '-', /\d/, /\d/];
  
  constructor(private _activateRoute: ActivatedRoute,
              private _formBuilder: FormBuilder,
              private _successDialog: MatDialog,
              private _exitService: ExitService,
              private _loadingService: LoadingService) { }

  ngOnInit() {
    this._activateRoute.params.subscribe(params=>{
      this.protocol = params['protocol'];
    });
    this.form = this._formBuilder.group({
      protocol: [{value: this.protocol, disabled: true}, Validators.required],
      date: ['', Validators.required],
      taxIdentifier: ['', Validators.required],
      name: ['', Validators.required]
    });
  }

  save() {
    this._loadingService.show();
    this.form.value.date = moment(this.form.value.date).format('YYYY-MM-DD');
    this.form.value.taxIdentifier = this.form.value.taxIdentifier.replace(/\D+/g, '');
    this.form.value.protocol = this.protocol;
    this._exitService.save(this.form.value).subscribe(
      suc=>{
        this.openSuccessDialog();
        this._loadingService.hide();
      }
    );
    
  }

  openSuccessDialog() {
    this._successDialog.open(SuccessComponent, {
      data: {
        protocol: this.protocol
      }
    });
  }

}
