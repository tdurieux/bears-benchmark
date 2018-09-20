import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { EntranceComponent } from './entrance.component';

@NgModule({
  imports: [
    RouterModule.forChild([
        { 
            path: '', 
            component: EntranceComponent  
        }
    ])
  ]
})
export class EntranceRouting { }