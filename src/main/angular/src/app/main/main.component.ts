import { Component, OnInit, ViewEncapsulation, OnDestroy } from '@angular/core';
import { LoadingService } from './services/loading.service';
import { Subscription } from 'rxjs/Subscription';
import { Router } from '@angular/router';
import { MainService } from './main.service';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class MainComponent implements OnInit, OnDestroy {
 
  private isLoadingSubscription: Subscription;
  public loading: Boolean;

  constructor(private _loadingService: LoadingService, 
              private _mainService: MainService,
              private _router: Router) { }

  ngOnInit() {
    this.loading = this._loadingService.isLoading;
    this.isLoadingSubscription = this._loadingService.getLoading().subscribe(valor=>{      
       setTimeout(()=>{
        this.loading = valor;  
       },1);
    });
  }

  ngOnDestroy(){
    if(this.isLoadingSubscription) {
      this.isLoadingSubscription.unsubscribe();
    }
  }

  public logout() {
    this._mainService.logout().subscribe(suc => {
      sessionStorage.clear();
      this._router.navigate(["/login"]);
    });    
  }

}