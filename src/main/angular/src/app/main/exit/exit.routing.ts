import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ExitComponent } from './exit.component';

@NgModule({
  imports: [
    RouterModule.forChild([
        { 
            path: ':protocol', 
            component: ExitComponent  
        }
    ])
  ]
})
export class ExitRouting { }