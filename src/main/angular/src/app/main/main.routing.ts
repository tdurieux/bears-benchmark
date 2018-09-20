import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MainComponent } from './main.component';
import { AuthGuard } from './auth.guard';
import { EntranceComponent } from './entrance/entrance.component';

@NgModule({
  imports: [
    RouterModule.forChild([
      { 
        path: 'main', 
        component: MainComponent,
        canActivate: [
          AuthGuard
        ],
        canActivateChild: [
          AuthGuard
        ],
        children: [
          {
            path: 'entrance',
            loadChildren: './entrance/entrance.module#EntranceModule'
          },
          {
            path: 'exit',
            loadChildren: './exit/exit.module#ExitModule'
          },
          {
            path: 'search',
            loadChildren: './search/search.module#SearchModule'
          }
        ]
      }
    ])
  ]
})
export class MainRouting { }