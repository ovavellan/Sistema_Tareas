import { Component } from '@angular/core';
import {AdminService} from "../../services/admin.service";
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-update-task',
  templateUrl: './update-task.component.html',
  styleUrls: ['./update-task.component.scss']
})
export class UpdateTaskComponent {

  id:number = this.route.snapshot.params["id"];
  updateTaskForm!: FormGroup;
  listOfEstudents: any = [];
  listOfPriorities: any = ["BAJO", "MEDIO", "ALTO"];
  listOfTaskStatus: any = ["PENDIENTE", "ENPROGRESO", "COMPLETADA", "APLAZADA", "CANCELADA"];

  constructor(private service: AdminService,
              private route: ActivatedRoute,
              private fb: FormBuilder,
              private adminService: AdminService,
              private snackBar: MatSnackBar,
              private router: Router) {
    this.getTaskById();
    this.getUsers();
    this.updateTaskForm = this.fb.group({
      studentId: [null, [Validators.required]],
      title: [null, [Validators.required, Validators.maxLength(100)]],
      description: [null, [Validators.required, Validators.maxLength(500)]],
      dueDate: [null, [Validators.required]],
      priority: [null, [Validators.required]],
      taskStatus: [null, [Validators.required]],
    })
  }

  getTaskById(){
    this.service.getTaskById(this.id).subscribe((res) =>{
      this.updateTaskForm.patchValue(res)
      console.log(res);
    })
  }

  getUsers() {
    this.adminService.getUsers().subscribe({
      next: (res) => {
        this.listOfEstudents = res;
        console.log('Estudiantes:', res);
      },
      error: (err) => {
        console.error('Error al obtener estudiantes:', err);
        this.snackBar.open('No se pudieron cargar los estudiantes', 'Cerrar', { duration: 3000 });
      }
    });
  }

  updateTask() {
    if (this.updateTaskForm.invalid) {
      this.snackBar.open('Por favor complete todos los campos requeridos', 'Cerrar', { duration: 3000 });
      return;
    }

    const taskData = { ...this.updateTaskForm.value };

    // Convertir la fecha de Material Datepicker a ISO String
    if (taskData.dueDate) {
      const date = new Date(taskData.dueDate);
      taskData.dueDate = date.toISOString();
    }

    console.log('Datos de la tarea:', taskData);

    this.adminService.updateTask(this.id, this.updateTaskForm.value).subscribe({
      next: (res) => {
        console.log('Respuesta del servidor:', res);
        if (res && res.id != null) {
          this.snackBar.open('Tarea actualizada exitosamente', 'Cerrar', { duration: 3000 });
          this.router.navigateByUrl('/admin/dashboard');
        } else {
          this.snackBar.open('No se pudo crear la tarea', 'Error', { duration: 3000 });
        }
      },
      error: (err) => {
        console.error('Error al actualizar tarea:', err);
        this.snackBar.open('Error al actualizar tarea: ' + (err.message || ''), 'Error', { duration: 3000 });
      }
    });
  }

}
