package org.xujin.moss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import de.codecentric.boot.admin.server.cloud.extension.MultRegisterCenterServerMgmtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xujin.moss.common.ResultData;
import org.xujin.moss.common.domain.PageResult;
import org.xujin.moss.constant.Constants;
import org.xujin.moss.entity.RegisterCenter;
import org.xujin.moss.mapper.RegisterCenterMapper;
import org.xujin.moss.model.RegisterCenterModel;
import org.xujin.moss.request.RegisterCenterPageRequest;
import org.xujin.moss.service.RegisterCenterService;
import org.xujin.moss.utils.BeanMapper;

import java.sql.Timestamp;
import java.util.List;

@Service
public class RegisterCenterServiceImpl implements RegisterCenterService {

    @Autowired
    private MultRegisterCenterServerMgmtConfig multEurekaServerMgmtConfig;

    @Autowired
    private RegisterCenterMapper registerCenterMapper;

    @Override
    @Transactional
    public ResultData addRegisterCenter(RegisterCenterModel model) {
        RegisterCenter rc=registerCenterMapper.findRegisterCenterByCode(model.getCode());
        if(null!=rc){
            return ResultData.builder().msgCode("400").msgContent("注册中心的code:"+model.getCode()+"不能重复").build();
        }
        model.setIsDeleted(Constants.IS_DELETE_FALSE);
        model.setGmtCreate(new Timestamp(System.currentTimeMillis()));
        model.setGmtModified(new Timestamp(System.currentTimeMillis()));
        registerCenterMapper.insert(BeanMapper.map(model, RegisterCenter.class));
        //当状态是启用的时候动态添加
        if (Constants.REGISTER_CENTER_ENABLE==model.getStatus()) {
            multEurekaServerMgmtConfig.addEureka(model.getCode(),model.getUrl());
        }
        return ResultData.builder().build();
    }

    @Override
    public RegisterCenterModel getRegisterCenterById(Long id) {
        return BeanMapper.map(registerCenterMapper.selectById(id),RegisterCenterModel.class);
    }

    @Override
    public PageResult<RegisterCenterModel> findPageByParam(RegisterCenterPageRequest model) {
        PageResult<RegisterCenterModel> pageResult= new PageResult<RegisterCenterModel>();
        Page pageRequest = new Page(model.getPageNo(),model.getPageSize());
        QueryWrapper<RegisterCenter> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(model.getCode())){
            queryWrapper.like("code",model.getCode());
        }
        queryWrapper.eq("is_deleted",Constants.IS_DELETE_FALSE);
        IPage<RegisterCenter> page=registerCenterMapper.selectPage(pageRequest, queryWrapper);
        List<RegisterCenterModel> list= BeanMapper.mapList(page.getRecords(),RegisterCenter.class,RegisterCenterModel.class);
        pageResult.setCurrentPage(page.getCurrent());
        pageResult.setTotalCount(page.getTotal());
        pageResult.setList(list);
        pageResult.setTotalPage(page.getSize());
        return pageResult;
    }

    @Override
    @Transactional
    public void update(RegisterCenterModel model) {
        RegisterCenter registerCenter=registerCenterMapper.selectById(model.getId());
        if(null==registerCenter){
            return ;
        }
        registerCenter.setGmtModified(new Timestamp(System.currentTimeMillis()));
        registerCenterMapper.updateById(BeanMapper.map(model,RegisterCenter.class));
        //当状态是启用的时候动态添加
        if (Constants.REGISTER_CENTER_ENABLE==model.getStatus()) {
            multEurekaServerMgmtConfig.addEureka(model.getCode(),model.getUrl());
        }else{
            multEurekaServerMgmtConfig.revomeEureka(registerCenter.getCode());
        }

    }

    @Override
    @Transactional
    public void deleteRegisterCenterById(Long id) {
        RegisterCenter registerCenter=registerCenterMapper.selectById(id);
        if(null==registerCenter){
            return ;
        }
        registerCenter.setIsDeleted(Constants.IS_DELETE_TRUE);
        registerCenterMapper.updateById(registerCenter);
        multEurekaServerMgmtConfig.revomeEureka(registerCenter.getCode());

    }

    @Override
    public List<RegisterCenterModel> findRegisterCenterListByStatus(int status) {
        return BeanMapper.mapList(registerCenterMapper.findRegisterCenterListByStatus(status),RegisterCenter.class,RegisterCenterModel.class);
    }
}
