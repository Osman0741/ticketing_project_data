package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Task;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final ProjectMapper projectMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, UserService userService, UserMapper userMapper, ProjectMapper projectMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.projectMapper = projectMapper;
    }

    @Override
    public void save(TaskDTO dto) {

         Task task = taskMapper.convertToEntity(dto);
         task.setTaskStatus(Status.OPEN);

        taskRepository.save(task);

    }

    @Override
    public void update(TaskDTO dto) {

     Optional<Task> task = taskRepository.findById(dto.getId());
     Task converted = taskMapper.convertToEntity(dto);

     if(task.isPresent()){
         converted.setTaskStatus(dto.getTaskStatus()== null ? task.get().getTaskStatus() : dto.getTaskStatus());
         converted.setAssignedDate(task.get().getAssignedDate());
         taskRepository.save(converted);
     }
    }

    @Override
    public List<TaskDTO> listAllTasks() {
        return taskRepository.findAll().stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {

       Optional<Task> task= taskRepository.findById(id);

       if(task.isPresent()){
           task.get().setDeleted(true);
           taskRepository.save(task.get());
       }
    }

    @Override
    public TaskDTO findById(Long id) {

       Optional<Task> task = taskRepository.findById(id);
       if(task.isPresent()){
           return taskMapper.convertToDto(task.get());
       }
       return null;
    }

    @Override
    public int totalCompletedTaskByProject(String projectCode) {
        return taskRepository.totalCompletedTaskByProject(projectCode);
    }

    @Override
    public int totalUnfinishedByProject(String projectCode) {
        return taskRepository.totalUnfinishedTaskByProject(projectCode);
    }

    @Override
    public void completeByProject(ProjectDTO projectDTO) {

        List<Task> list = taskRepository.findAllByProject(projectMapper.convertToEntity(projectDTO));

        list.stream().map(taskMapper::convertToDto).forEach(taskDto-> {

            taskDto.setTaskStatus(Status.COMPLETE);
            update(taskDto);


        });

    }

    @Override
    public List<TaskDTO> findAllTasksByStatusIsNot(Status status) {

        UserDTO loginUser = userService.findByUserName("john@employee.com");

        return taskRepository.findAllByTaskStatusIsNotAndAssignedEmployee(status, userMapper.convertToEntity(loginUser) ).stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> findAllTasksByStatus(Status status) {

        UserDTO loginUser = userService.findByUserName("john@employee.com");
        return taskRepository.findAllByTaskStatusAndAssignedEmployee(status , userMapper.convertToEntity(loginUser)).stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }


}
