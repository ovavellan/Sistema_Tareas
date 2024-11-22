package com.proyecto.servicio.admin;

import com.proyecto.dto.TaskDTO;
import com.proyecto.dto.UserDto;
import com.proyecto.entities.Task;
import com.proyecto.enums.TaskStatus;
import com.proyecto.enums.UserRole;
import com.proyecto.entities.Usuario;
import com.proyecto.repositorio.TaskRespository;
import com.proyecto.repositorio.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final UsuarioRepositorio usuarioRepositorio;

    private final TaskRespository taskRespository;

    @Override
    public List<UserDto> getUsers() {
        return usuarioRepositorio.findAll()
                .stream()
                .filter(user -> user.getUserRole() == UserRole.ESTUDIANTE)
                .map(Usuario::getUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        Optional<Usuario> optionalUser = usuarioRepositorio.findById(taskDTO.getStudentId());
        if (optionalUser.isPresent()) {
            Task task = new Task();
            task.setTitle(taskDTO.getTitle());
            task.setDescription(taskDTO.getDescription());
            task.setPriority(taskDTO.getPriority());
            task.setDueDate(taskDTO.getDueDate());
            task.setTaskStatus(TaskStatus.ENPROGRESO);
            task.setUsuario(optionalUser.get());
            return taskRespository.save(task).getTaskDTO();
        }
        return null;
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        return taskRespository.findAll()
                .stream()
                .sorted(Comparator.comparing(Task::getDueDate).reversed())
                .map(Task::getTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTask(Long id) {
        taskRespository.deleteById(id);
    }
}
