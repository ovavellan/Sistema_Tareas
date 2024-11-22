import { Component } from '@angular/core';
import {AdminService} from "../../services/admin.service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {

  listofTasks: any = [];

  constructor(private service: AdminService,
              private snackBar: MatSnackBar,) {
    this.getTasks();
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
}
