import { Injectable } from '@angular/core';
import {Router} from '@angular/router';
import {OAuthService, AuthConfig, JwksValidationHandler} from 'angular-oauth2-oidc';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {authConfig} from './authconfig';

export class Foo {
  constructor(public id: number, public name: string) {}
}


@Injectable({
  providedIn: 'root'
})
export class AppService {
  constructor(
    private router: Router,
    private http: HttpClient,
    private oauthService: OAuthService
  ) {
    // this.oauthService.configure(authConfig);
    // this.oauthService.loginUrl = 'http://localhost:8080/oauth/authorize';
    // this.oauthService.redirectUri = 'http://localhost:4200';
    // this.oauthService.clientId = 'sampleClientId';
    // this.oauthService.scope = 'read write foo bar';
    // this.oauthService.setStorage(sessionStorage);
    // this.oauthService.tryLogin({}).then((status) => {
    //   console.log(`TryLogin returned: ${status}`);
    // });
    // this.oauthService.oidc = false;
    this.oauthService.configure(authConfig);
    this.oauthService.tokenValidationHandler = new JwksValidationHandler();
    this.oauthService.tryLoginImplicitFlow().then((status) => {
          console.log(`TryLogin returned: ${status}`);
          console.log(this.oauthService.getIdentityClaims());
        });
  }

  obtainAccessToken() {
    this.oauthService.initImplicitFlow();
  }

  getResource(resourceUrl: string): Observable<string> {
    const headers = new HttpHeaders({
      'Content-type': 'application/x-www-form-urlencoded; charset=utf-8',
      Authorization: 'Bearer ' + this.oauthService.getAccessToken(),
    });
    return this.http.get<string>(resourceUrl, { headers });
  }

  getToken(): string {
    return this.oauthService.getAccessToken();
  }

  isLoggedIn() {
    console.log(this.oauthService.getAccessToken());
    return (this.oauthService.getAccessToken() !== null);
  }

  logout() {
    this.oauthService.logOut();
    location.reload();
  }
}
