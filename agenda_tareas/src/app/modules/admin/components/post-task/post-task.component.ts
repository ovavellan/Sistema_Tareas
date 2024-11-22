import { Component, OnInit } from '@angular/core';
import { AdminService } from "../../services/admin.service";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MatSnackBar } from "@angular/material/snack-bar";
import { Router } from "@angular/router";

@Component({
  selector: 'app-post-task',
  templateUrl: './post-task.component.html',
  styleUrls: ['./post-task.component.scss']
})
export class PostTaskComponent implements OnInit {
  taskForm!: FormGroup;
  listOfEstudents: any[] = [];
  listOfPriorities: string[] = ["LOW", "MEDIUM", "HIGH"];

  constructor(
    private adminService: AdminService,
    private fb: FormBuilder,
    private snackBar: MatSnackBar,
    private router: Router
  ) {}

  ngOnInit() {
    this.initForm();
    this.getUsers();
  }

  initForm() {
    this.taskForm = this.fb.group({
      studentId: [null, [Validators.required]],
      title: [null, [Validators.required, Validators.maxLength(100)]],
      description: [null, [Validators.required, Validators.maxLength(500)]],
      dueDate: [null, [Validators.required]],
      priority: [null, [Validators.required]]
    });
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

  postTask() {
    if (this.taskForm.invalid) {
      this.snackBar.open('Por favor complete todos los campos requeridos', 'Cerrar', { duration: 3000 });
      return;
    }

    const taskData = { ...this.taskForm.value };

    // Convertir la fecha de Material Datepicker a ISO String
    if (taskData.dueDate) {
      const date = new Date(taskData.dueDate);
      taskData.dueDate = date.toISOString();
    }

    console.log('Datos de la tarea:', taskData);

    this.adminService.postTask(taskData).subscribe({
      next: (res) => {
        console.log('Respuesta del servidor:', res);
        if (res && res.id != null) {
          this.snackBar.open('Tarea creada exitosamente', 'Cerrar', { duration: 3000 });
          this.router.navigateByUrl('/admin/dashboard');
        } else {
          this.snackBar.open('No se pudo crear la tarea', 'Error', { duration: 3000 });
        }
      },
      error: (err) => {
        console.error('Error al crear tarea:', err);
        this.snackBar.open('Error al crear tarea: ' + (err.message || ''), 'Error', { duration: 3000 });
      }
    });
  }
}
