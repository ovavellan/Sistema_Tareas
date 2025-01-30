import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { ApiConfig } from 'src/app/core/config/api.config';
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = ApiConfig.BASE_URL;

  constructor(private http: HttpClient) {}

  signup(signupRequest: any): Observable<any> {
    return this.http.post(`${this.apiUrl}api/auth/signup`, signupRequest);
  }

  login(loginRequest: any): Observable<any> {
    return this.http.post(`${this.apiUrl}api/auth/login`, loginRequest);
  }
}
