package com.cydeo.service.impl;

import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Task;
import com.cydeo.enums.Status;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
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
         converted.setTaskStatus(task.get().getTaskStatus());
         converted.setAssignedDate(task.orElseThrow().getAssignedDate());
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


}
