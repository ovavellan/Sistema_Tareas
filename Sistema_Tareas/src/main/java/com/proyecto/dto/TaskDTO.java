package com.proyecto.dto;

import com.proyecto.enums.TaskStatus;
import lombok.Data;

import java.util.Date;

@Data
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private Date dueDate;
    private String priority;
    private TaskStatus taskStatus;

    private Long studentId;

    private String studentName;
}
