package com.proyecto.repositorio;

import com.proyecto.dto.TaskDTO;
import com.proyecto.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRespository extends JpaRepository<Task, Long> {

    List<Task> findAllByTitleContaining(String title);

    List<Task> findAllByUsuario_Id(Long id);
}
