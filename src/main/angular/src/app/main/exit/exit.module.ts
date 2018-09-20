import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ExitComponent } from './exit.component';
import { RouterModule } from '@angular/router';
import { ExitRouting } from './exit.routing';
import { SuccessComponent } from './success/success.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { FormBuilder } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatNativeDateModule } from '@angular/material';
import { MAT_DATE_LOCALE } from '@angular/material';
import { MatDatepickerModule } from '@angular/material';
import { MatDialogModule } from '@angular/material';
import { TextMaskModule } from 'angular2-text-mask';
import { UppercaseModule } from '../../directives/uppercase/uppercase.module';

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    ExitRouting,
    FlexLayoutModule,
    FormsModule,
    ReactiveFormsModule,
    MatInputModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatExpansionModule,
    MatIconModule,
    MatDialogModule,
    TextMaskModule,
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
    ExitComponent,
    SuccessComponent
  ]
})
export class ExitModule { }
