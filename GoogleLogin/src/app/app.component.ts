import { Component } from '@angular/core';
import {AppService} from '../app.service';
import {OnInit} from '@angular/core/src/metadata/lifecycle_hooks';
import {OAuthService} from 'angular-oauth2-oidc';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'GoogleLogin';
  public isLoggedIn = false;

  constructor(
    private appService: AppService,
    private oauthService: OAuthService
  ) {}

  ngOnInit(): void {
    this.isLoggedIn = this.appService.isLoggedIn();
  }

  login() {
    this.appService.obtainAccessToken();
  }

  res() {
    console.log(this.oauthService.getIdentityClaims());
    this.appService.getResource('http://localhost:8081/user').subscribe((str) => {
      console.log(`User:`);
      console.log(str);
    });
  }

  res2() {
    console.log(this.oauthService.getIdentityClaims());
    this.appService.getResource('http://localhost:8081/user2').subscribe((str) => {
      console.log(`User:`);
      console.log(str);
    });
  }

  logout() {
    this.appService.logout();
  }

}
