package org.xujin.moss.controller;

import org.xujin.moss.common.ResultData;
import org.xujin.moss.common.domain.PageResult;
import org.xujin.moss.request.ProjectByPageRequest;
import org.xujin.moss.model.ProjectModel;
import org.xujin.moss.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/project")
public class ProjectController {


    @Autowired
    private ProjectService projectService;

    /**
     * 查询项目列表并分页
     * @param model
     * @return
     */
    @PostMapping("/list")
    ResultData searchProjectByPage(@RequestBody ProjectByPageRequest model) {
        PageResult<ProjectModel> pageResult= projectService.findPageByParam(model);
        return ResultData.builder().data(pageResult).build();
    }

    /**
     * 增加一个项目
     * @param projectModel
     * @return
     */
    @PostMapping("/add")
    public ResultData addProject(@RequestBody ProjectModel projectModel) {
        return projectService.addProject(projectModel);
    }

    /**
     * 更新一个项目
     * @param projectModel
     * @return
     */
    @PostMapping("/update")
    public ResultData updateProject(@RequestBody ProjectModel projectModel) {
        projectService.update(projectModel);
        return ResultData.builder().build();
    }

    /**
     * 更新一个项目
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public ResultData deleteProjet(@PathVariable Long id) {
        projectService.deleteProjectById(id);
        return ResultData.builder().build();
    }


}
