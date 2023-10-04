package com.cydeo.service;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Task;
import com.cydeo.enums.Status;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    void save (TaskDTO dto);

    void update(TaskDTO dto);

    List<TaskDTO> listAllTasks();

    void delete(Long id);

   TaskDTO findById(Long id);

   int totalCompletedTaskByProject(String projectCode);

   int totalUnfinishedByProject(String projectCode);

   void completeByProject(ProjectDTO projectDTO);

   List<TaskDTO> findAllTasksByStatusIsNot(Status status);

   List<TaskDTO> findAllTasksByStatus(Status status);


}
