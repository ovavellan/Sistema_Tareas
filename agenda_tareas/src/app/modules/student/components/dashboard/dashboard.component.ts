import { Component } from '@angular/core';
import {StudentService} from "../../services/student.service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {

  listOfTasks: any = [];

  constructor(private service: StudentService,
              private snackBar: MatSnackBar) {
    this.getTasks();
  }

  getTasks() {
    this.service.getStudentTasksById().subscribe((res) => {
      console.log(res);
      this.listOfTasks = res;
    })
  }

  updateStatus(id: number, status: string) {
    this.service.updateStatus(id, status).subscribe((res) => {
      if(res.id!=null){
        this.snackBar.open("Task status updated successfully", "Close", { duration: 5000 });
        this.getTasks()
      } else {
        this.snackBar.open("Getting error while updating task", "Close", { duration: 5000 });
      }
    })
  }

}
