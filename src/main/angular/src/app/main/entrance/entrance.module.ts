import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EntranceComponent } from './entrance.component';
import { RouterModule } from '@angular/router';
import { EntranceRouting } from './entrance.routing';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { FormBuilder } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatRadioModule } from '@angular/material/radio';
import { MatNativeDateModule } from '@angular/material';
import { MAT_DATE_LOCALE } from '@angular/material';
import { FlexLayoutModule } from '@angular/flex-layout';
import { TextMaskModule } from 'angular2-text-mask';
import { MatExpansionModule } from '@angular/material/expansion';
import { SuccessComponent } from './success/success.component';
import { MatDialogModule } from '@angular/material/dialog';
import { UppercaseModule } from '../../directives/uppercase/uppercase.module';

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    EntranceRouting,
    FlexLayoutModule,
    FormsModule,
    ReactiveFormsModule,
    TextMaskModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatRadioModule,    
    MatExpansionModule,
    MatDialogModule,
    UppercaseModule
  ],
  providers: [
    FormBuilder,
    {provide: MAT_DATE_LOCALE, useValue: 'pt-BR'}
  ],
  entryComponents: [
    SuccessComponent
  ],
  declarations: [
    EntranceComponent,
    SuccessComponent
  ]
})
export class EntranceModule { }
