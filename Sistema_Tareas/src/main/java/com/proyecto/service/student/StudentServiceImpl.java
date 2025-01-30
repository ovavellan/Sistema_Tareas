package com.proyecto.service.student;

import com.proyecto.dto.CommentDTO;
import com.proyecto.dto.TaskDTO;
import com.proyecto.entities.Comment;
import com.proyecto.entities.Task;
import com.proyecto.entities.User;
import com.proyecto.enums.TaskStatus;
import com.proyecto.enums.UserRole;
import com.proyecto.repository.CommentRepository;
import com.proyecto.repository.TaskRespository;
import com.proyecto.utils.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final TaskRespository taskRespository;
    private final JwtUtil jwtUtil;
    private final CommentRepository commentRepository;

    @Override
    public List<TaskDTO> getTasksByUserId() {
        User user = jwtUtil.getLoggedInUser();
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }

        return taskRespository.findAllByUser_Id(user.getId())
                .stream()
                .sorted(Comparator.comparing(Task::getDueDate).reversed())
                .map(Task::getTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO updateTask(Long id, String status) {
        User user = jwtUtil.getLoggedInUser();
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }

        Optional<Task> optionalTask = taskRespository.findById(id);
        if (optionalTask.isPresent()) {
            Task existingTask = optionalTask.get();

            // Verificar que el usuario que intenta actualizar la tarea es el dueño de la tarea
            if (existingTask.getUser().getId() != user.getId()) { // Comparación de tipos primitivos long
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para actualizar esta tarea");
            }

            existingTask.setTaskStatus(mapStringToTaskStatus(status));
            return taskRespository.save(existingTask).getTaskDTO();
        }
        throw new EntityNotFoundException("Task not found");
    }

    private TaskStatus mapStringToTaskStatus(String status) {
        return switch (status) {
            case "PENDIENTE" -> TaskStatus.PENDIENTE;
            case "ENPROGRESO" -> TaskStatus.ENPROGRESO;
            case "COMPLETADA" -> TaskStatus.COMPLETADA;
            case "APLAZADA" -> TaskStatus.APLAZADA;
            default -> TaskStatus.CANCELADA;
        };
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        User user = jwtUtil.getLoggedInUser();
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }

        Optional<Task> optionalTask = taskRespository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();

            // Verificar que el usuario que intenta ver la tarea es el dueño de la tarea
            if (task.getUser().getId() != user.getId()) { // Comparación de tipos primitivos long
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para ver esta tarea");
            }

            return task.getTaskDTO();
        }
        throw new EntityNotFoundException("Task not found");
    }

    @Override
    public CommentDTO createComment(Long taskId, String content) {
        User user = jwtUtil.getLoggedInUser();
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }

        Optional<Task> optionalTask = taskRespository.findById(taskId);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();

            // Verificar que el usuario que intenta crear un comentario es el dueño de la tarea
            if (task.getUser().getId() != user.getId()) { // Comparación de tipos primitivos long
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para comentar en esta tarea");
            }

            Comment comment = new Comment();
            comment.setCreateAt(new Date());
            comment.setContent(content);
            comment.setTask(task);
            comment.setUser(user);
            return commentRepository.save(comment).getCommentDTO();
        }
        throw new EntityNotFoundException("Task not found");
    }

    @Override
    public List<CommentDTO> getCommentsByTaskId(Long taskId) {
        User user = jwtUtil.getLoggedInUser();
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }

        Optional<Task> task = taskRespository.findById(taskId);
        if (task.isEmpty()) {
            throw new EntityNotFoundException("Tarea no encontrada");
        }

        // Verificar que el usuario que intenta ver los comentarios es el dueño de la tarea
        if (task.get().getUser().getId() != user.getId()) { // Comparación de tipos primitivos long
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para ver estos comentarios");
        }

        return commentRepository.findAllByTaskId(taskId)
                .stream()
                .map(Comment::getCommentDTO)
                .collect(Collectors.toList());
    }
}