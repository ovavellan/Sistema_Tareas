package com.proyecto.servicio.admin;

import com.proyecto.dto.TaskDTO;
import com.proyecto.dto.UserDto;

import java.util.List;

public interface AdminService {

    List<UserDto> getUsers();

    TaskDTO createTask(TaskDTO task);

    List<TaskDTO> getAllTasks();

    void deleteTask(Long id);
}
