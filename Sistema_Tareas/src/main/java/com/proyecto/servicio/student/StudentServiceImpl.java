package com.proyecto.servicio.student;

import com.proyecto.dto.CommentDTO;
import com.proyecto.dto.TaskDTO;
import com.proyecto.entities.Comment;
import com.proyecto.entities.Task;
import com.proyecto.entities.Usuario;
import com.proyecto.enums.TaskStatus;
import com.proyecto.enums.UserRole;
import com.proyecto.repositorio.CommentRepository;
import com.proyecto.repositorio.TaskRespository;
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
        Usuario user = jwtUtil.getLoggedInUser();
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }

        return taskRespository.findAllByUsuario_Id(user.getId())
                .stream()
                .sorted(Comparator.comparing(Task::getDueDate).reversed())
                .map(Task::getTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO updateTask(Long id, String status) {
        Optional<Task> optionalTask = taskRespository.findById(id);
        if (optionalTask.isPresent()){
            Task existingTask = optionalTask.get();
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
        Optional<Task> optionalTask = taskRespository.findById(id);
        return optionalTask.map(Task::getTaskDTO).orElse(null);
    }

    @Override
    public CommentDTO createComment(Long taskId, String content) {
        Optional<Task> optionalTask = taskRespository.findById(taskId);
        Usuario usuario=jwtUtil.getLoggedInUser();
        if ((optionalTask.isPresent()) && usuario != null) {
            Comment comment = new Comment();
            comment.setCreateAt(new Date());
            comment.setContent(content);
            comment.setTask(optionalTask.get());
            comment.setUsuario(usuario);
            return commentRepository.save(comment).getCommentDTO();
        }
        throw new EntityNotFoundException("User or Task not found");
    }

    @Override
    public List<CommentDTO> getCommentsByTaskId(Long taskId) {
        Usuario usuario = jwtUtil.getLoggedInUser();
        if (usuario == null) {
            throw new EntityNotFoundException("Usuario no autenticado");
        }
        Optional<Task> task = taskRespository.findById(taskId);
        if (task.isEmpty()) {
            throw new EntityNotFoundException("Tarea no encontrada");
        }
        if (usuario.getUserRole() == UserRole.ESTUDIANTE &&
                task.get().getUsuario().getId() != usuario.getId()) {  // Cambiado a comparación con !=
            throw new EntityNotFoundException("No tienes permiso para ver estos comentarios");
        }
        return commentRepository.findAllByTaskId(taskId)
                .stream()
                .map(Comment::getCommentDTO)
                .collect(Collectors.toList());
    }

}