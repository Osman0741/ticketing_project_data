package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final TaskRepository taskRepository;
    private final TaskService taskService;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper, UserService userService, UserMapper userMapper, TaskRepository taskRepository, TaskService taskService) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.taskRepository = taskRepository;
        this.taskService = taskService;
    }

    @Override
    public List<ProjectDTO> listAllProject() {
       List<Project> projectList = projectRepository.findAll();
        return projectList.stream().map(projectMapper::convertToDto).collect(Collectors.toList());

    }

    @Override
    public ProjectDTO findByProjectCode(String code) {

        Project project = projectRepository.findByProjectCode(code);

        return projectMapper.convertToDto(project);
    }

    @Override
    public void save(ProjectDTO project) {

        project.setProjectStatus(Status.OPEN);
        projectRepository.save(projectMapper.convertToEntity(project));
    }

    @Override
    public void delete(String projectCode) {
    Project project =    projectRepository.findByProjectCode(projectCode);
    project.setDeleted(true);
    projectRepository.save(project);
    }

    @Override
    public void complete(ProjectDTO project) {
        Project project1 = projectRepository.findByProjectCode(project.getProjectCode());
        project1.setProjectStatus(Status.COMPLETE);
        projectRepository.save(project1);
    }

    @Override
    public void update(ProjectDTO dto) {

        Project project= projectRepository.findByProjectCode(dto.getProjectCode());
        Project converted= projectMapper.convertToEntity(dto);
        converted.setId(project.getId());
        converted.setProjectStatus(project.getProjectStatus());
        projectRepository.save(converted);


    }

    @Override
    public List<ProjectDTO> listAllProjectDetails() {

        UserDTO userDto = userService.findByUserName("harold@manager.com");

        List<Project> list =  projectRepository.findAllByAssignedManager(userMapper.convertToEntity(userDto));

        return list.stream().map(project->{

            ProjectDTO projectDTO= projectMapper.convertToDto(project);
            projectDTO.setCompleteTaskCounts(taskService.totalCompletedTaskByProject(project.getProjectCode()));
            projectDTO.setUnfinishedTaskCounts(taskService.totalUnfinishedByProject(project.getProjectCode()));
            return projectDTO;

        }).collect(Collectors.toList());
    }
}
