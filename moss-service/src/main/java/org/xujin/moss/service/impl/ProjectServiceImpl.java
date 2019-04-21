package org.xujin.moss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.xujin.moss.common.ResultData;
import org.xujin.moss.common.domain.PageResult;
import org.xujin.moss.constant.Constants;
import org.xujin.moss.entity.Project;
import org.xujin.moss.mapper.ProjectMapper;
import org.xujin.moss.request.ProjectByPageRequest;
import org.xujin.moss.model.ProjectModel;
import org.xujin.moss.service.ProjectService;
import org.xujin.moss.utils.BeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * ProjectService
 * @author xujin
 */
@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    @Override
    @Transactional
    public ResultData addProject(ProjectModel model) {
        Project project=projectMapper.findProjectByKey(model.getKey());
        if(null!=project){
            return ResultData.builder().msgCode("400").msgContent("项目KEY:"+model.getKey()+"不能重复").build();
        }
        model.setIsDeleted(Constants.IS_DELETE_FALSE);
        model.setGmtCreate(new Timestamp(System.currentTimeMillis()));
        model.setGmtModified(new Timestamp(System.currentTimeMillis()));
        projectMapper.insert(BeanMapper.map(model, Project.class));
        return ResultData.builder().build();

    }

    @Override
    public ProjectModel getProjectById(Long id) {
        return BeanMapper.map(projectMapper.selectById(id),ProjectModel.class);
    }

    @Override
    public PageResult<ProjectModel> findPageByParam(ProjectByPageRequest model) {
        Page pageRequest = new Page(model.getPageNo(),model.getPageSize());
        QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(model.getName())){
            queryWrapper.like("name",model.getName());
        }
        queryWrapper.eq("is_deleted",Constants.IS_DELETE_FALSE);
        IPage<Project> page=projectMapper.selectPage(pageRequest, queryWrapper);
        List<ProjectModel> list= BeanMapper.mapList(page.getRecords(),Project.class,ProjectModel.class);
        PageResult<ProjectModel> pageResult=new PageResult<ProjectModel>();
        pageResult.setCurrentPage(page.getCurrent());
        pageResult.setTotalCount(page.getTotal());
        pageResult.setList(list);
        pageResult.setTotalPage(page.getSize());
        return pageResult;
    }

    @Override
    @Transactional
    public void update(ProjectModel model) {
        Project project=projectMapper.selectById(model.getId());
        if(null==project){
            return ;
        }
        project.setGmtModified(new Timestamp(System.currentTimeMillis()));
        projectMapper.updateById(BeanMapper.map(model,Project.class));
    }

    @Override
    public int totalProjectConut() {
        return projectMapper.totalConut();
    }

    @Override
    public int totalProjectConutByOwnerId(String ownerId) {
        return projectMapper.totalMyprojectConut(ownerId);
    }

    @Override
    @Transactional
    public void deleteProjectById(Long id){
        Project project=projectMapper.selectById(id);
        if(null==project){
            return ;
        }
        project.setIsDeleted(Constants.IS_DELETE_TRUE);
        projectMapper.updateById(project);
    }
}
