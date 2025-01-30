import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { StorageService } from "../../../auth/services/storage/storage.service";
import { Observable } from "rxjs";
import { ApiConfig } from 'src/app/core/config/api.config'; // Importa ApiConfig

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  constructor(private http: HttpClient) { }

  getStudentTasksById(): Observable<any> {
    return this.http.get(`${ApiConfig.BASE_URL}api/student/tasks`, {
      headers: this.createAuthorizationHeader()
    });
  }

  updateStatus(id: number, status: string): Observable<any> {
    return this.http.get(`${ApiConfig.BASE_URL}api/student/task/${id}/${status}`, {
      headers: this.createAuthorizationHeader()
    });
  }

  getTaskById(id: number): Observable<any> {
    return this.http.get(`${ApiConfig.BASE_URL}api/student/task/${id}`, {
      headers: this.createAuthorizationHeader()
    });
  }

  createComment(id: number, content: string): Observable<any> {
    const params = { content: content };
    return this.http.post(`${ApiConfig.BASE_URL}api/student/task/comment/${id}`, null, {
      params: params,
      headers: this.createAuthorizationHeader()
    });
  }

  getCommentsByTask(id: number): Observable<any> {
    return this.http.get(`${ApiConfig.BASE_URL}api/student/comments/${id}`, {
      headers: this.createAuthorizationHeader()
    });
  }

  private createAuthorizationHeader(): HttpHeaders {
    return new HttpHeaders().set(
      'Authorization', 'Bearer ' + StorageService.getToken()
    );
  }
}
