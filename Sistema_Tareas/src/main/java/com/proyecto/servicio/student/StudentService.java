package com.proyecto.servicio.student;

import com.proyecto.dto.CommentDTO;
import com.proyecto.dto.TaskDTO;

import java.util.List;

public interface StudentService {

    List<TaskDTO> getTasksByUserId();

    TaskDTO updateTask(Long id, String status);

    TaskDTO getTaskById(Long id);

    CommentDTO createComment(Long taskId, String content);

    List<CommentDTO> getCommentsByTaskId(Long taskId);

}
