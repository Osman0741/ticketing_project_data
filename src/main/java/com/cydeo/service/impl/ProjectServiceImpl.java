package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.entity.Project;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
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
}
