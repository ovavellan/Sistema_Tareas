import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MatSnackBar } from "@angular/material/snack-bar";
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

@Component({
  selector: 'app-view-task-details',
  templateUrl: './view-task-details.component.html',
  styleUrls: ['./view-task-details.component.scss']
})
export class ViewTaskDetailsComponent implements OnInit {
  taskId: number = this.activatedRoute.snapshot.params["id"];
  taskData: any;
  comments: any;
  commentForm!: FormGroup;

  constructor(private service: AdminService,
              private activatedRoute: ActivatedRoute,
              private fb: FormBuilder,
              private snackbar: MatSnackBar
  ) {}

  ngOnInit() {
    this.getTaskById();
    this.getComments();
    this.commentForm = this.fb.group({
      content: [null, Validators.required]
    })
  }

  getTaskById() {
    this.service.getTaskById(this.taskId).subscribe((res) =>{
      this.taskData = res;
    })
  }

  getComments() {
    this.service.getCommentsByTask(this.taskId).subscribe((res) =>{
      this.comments = res;
    })
  }

  publishComment() {
    this.service.createComment(this.taskId,this.commentForm.get("content")?.value).subscribe((res)=>{
      if(res.id != null){
        this.snackbar.open("Comment posted successfully", "Close", {duration: 5000});
        this.getComments();
      } else {
        this.snackbar.open("Something went wrong", "Close", {duration: 5000});
      }
    })
  }

}
