package org.xujin.moss.service;


import org.xujin.moss.common.ResultData;
import org.xujin.moss.common.domain.PageResult;
import org.xujin.moss.request.ProjectByPageRequest;
import org.xujin.moss.model.ProjectModel;

public interface ProjectService {

    ResultData addProject(ProjectModel model) ;

    ProjectModel getProjectById(Long id);

    PageResult<ProjectModel> findPageByParam(ProjectByPageRequest model);

    void update(ProjectModel model) ;

    int totalProjectCount();

    int totalProjectCountByOwnerId(String ownerId);

    void deleteProjectById(Long id) ;

}
