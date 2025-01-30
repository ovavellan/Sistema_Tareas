package com.proyecto.controller.student;

import com.proyecto.dto.CommentDTO;
import com.proyecto.dto.TaskDTO;
import com.proyecto.service.student.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    //Obtener las tareas de un estudiante
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDTO>> getTasksByUserId(){
        return ResponseEntity.ok(studentService.getTasksByUserId());
    }

    //Actualizar status de tarea
    @PutMapping("/task/{id}/{status}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @PathVariable String status){
        TaskDTO updateTaskDto = studentService.updateTask(id, status);
        if (updateTaskDto == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.ok(updateTaskDto);
    }

    //Obtener tarea por ID
    @GetMapping("/task/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getTaskById(id));
    }

    //Crear comentario para una tarea mediante el ID de la misma
    @PostMapping("/task/comment/{taskId}")
    public ResponseEntity<CommentDTO> createComment(@PathVariable Long taskId, @RequestParam String content) {
        CommentDTO createdCommentDTO = studentService.createComment(taskId, content);
        if (createdCommentDTO == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCommentDTO);
    }

    //Obtener los comentarios de una tarea mediante el ID de la misma
    @GetMapping("/comments/{taskId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByTaskId(@PathVariable Long taskId) {
        return ResponseEntity.ok(studentService.getCommentsByTaskId(taskId));
    }
}
