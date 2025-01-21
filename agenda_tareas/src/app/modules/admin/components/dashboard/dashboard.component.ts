import { Component } from '@angular/core';
import {AdminService} from "../../services/admin.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {

  listofTasks: any = [];
  searchForm!: FormGroup;

  constructor(private service: AdminService,
              private snackBar: MatSnackBar,
              private fb: FormBuilder,) {
    this.getTasks();
    this.searchForm = this.fb.group({
      title: [null]
    })
  }

  getTasks() {
    this.service.getAllTasks().subscribe((res) =>{
      this.listofTasks = res;
    })
  }

  deleteTask(id) {
    this.service.deleteTask(id).subscribe((res) =>{
      this.snackBar.open("Tarea eliminada correctamente", "Cerrar", { duration: 5000 });
      this.getTasks();
    })
  }

  searchTask() {
    this.listofTasks = [];
    const title = this.searchForm.get("title")!.value;
    console.log(title);
    this.service.searchTask(title).subscribe((res) =>{
      console.log(res); 
      this.listofTasks = res;
    })
  }
}
