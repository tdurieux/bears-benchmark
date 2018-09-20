import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { LoginService } from './login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class LoginComponent implements OnInit {

  public form:FormGroup;
  public login:boolean = false;
  public error;

  constructor(private _formBuilder: FormBuilder, private _loginService: LoginService, private _router: Router) {
    this.form = _formBuilder.group({
      "username": [null, Validators.required],
      "password" : [null, Validators.required]
    });
   }

  ngOnInit() {
    sessionStorage.clear();
    this.error = null;
    this.login = false;
  }
  
  doLogin(){
    this.login = true;
    this._loginService.login(this.form.value).subscribe(
      suc=>{
        sessionStorage.setItem("access", JSON.stringify(suc));
        this._router.navigate(["/main/search"]);
        this.login = false;
      },
      err=>{        
        this.login = false;
        this.error = "Usuário e/ou Senha estão errados.";
      }
    );
  }

  keydown(event) {
    if(event.keyCode == 13) {
      this.doLogin();
    }
  }

}