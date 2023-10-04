package com.cydeo.service;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;

import java.util.List;

public interface ProjectService {

    List<ProjectDTO> listAllProject();

    ProjectDTO findByProjectCode(String code);

    void save(ProjectDTO project);

    void delete (String projectCode);

    void complete(String projectCode);

    void update(ProjectDTO project);

    List<ProjectDTO> listAllProjectDetails();

    List<ProjectDTO> listAllNonCompletedByAssignedManager(UserDTO assignedManager);
}
