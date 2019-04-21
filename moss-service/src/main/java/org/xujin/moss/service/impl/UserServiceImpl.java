package org.xujin.moss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.xujin.moss.common.ResultData;
import org.xujin.moss.common.domain.PageResult;
import org.xujin.moss.constant.Constants;
import org.xujin.moss.entity.User;
import org.xujin.moss.exception.ApplicationException;
import org.xujin.moss.mapper.UserMapper;
import org.xujin.moss.model.UserModel;
import org.xujin.moss.request.UserPageListRequest;
import org.xujin.moss.service.UserService;
import org.xujin.moss.utils.BeanMapper;
import org.xujin.moss.utils.MD5;
import org.xujin.moss.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public ResultData addUser(UserModel model) {
        User userDto=userMapper.findUserByUserName(model.getUsername());
        if(null!=userDto){
            return ResultData.builder().msgCode("400").msgContent("用户名:"+model.getUsername()+"不能重复").build();
        }
        User entity= BeanMapper.map(model,User.class);
        entity.setPassword(MD5.md5(entity.getPassword()));
        entity.setIsDeleted(Constants.IS_DELETE_FALSE);
        entity.setGmtCreate(new Timestamp(System.currentTimeMillis()));
        entity.setGmtModified(new Timestamp(System.currentTimeMillis()));
        userMapper.insert(entity);
        return ResultData.builder().build();
    }

    @Override
    public UserModel getUserById(Long id) {
        return BeanMapper.map(userMapper.selectById(id),UserModel.class);
    }

    @Override
    public UserModel getUserByUserNameAndPassWord(String userName, String password) {
        User user=userMapper.findUserByUserNameAndPassword(userName,MD5.md5(password));
        if(null==user){
            return null;
        }
        return BeanMapper.map(user,UserModel.class);
    }

    @Override
    public UserModel getUserByUserName(String userName) {
        User user=userMapper.findUserByUserName(userName);
        return BeanMapper.map(user,UserModel.class);
    }


    @Override
    public PageResult<UserVO> findPageByParam(UserPageListRequest userPageListRequest) {
        Page pageRequest = new Page(userPageListRequest.getPageNo(),userPageListRequest.getPageSize());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(userPageListRequest.getName())){
            queryWrapper.like("name",userPageListRequest.getName());
        }
        if(StringUtils.isNotEmpty(userPageListRequest.getUsername())){
            queryWrapper.like("username",userPageListRequest.getUsername());
        }
        queryWrapper.eq("is_deleted",Constants.IS_DELETE_FALSE);
        IPage<User> page=userMapper.selectPage(pageRequest, queryWrapper);
        List<UserVO> list= BeanMapper.mapList(page.getRecords(),User.class,UserVO.class);
        PageResult<UserVO> pageResult=new PageResult<UserVO>();
        pageResult.setCurrentPage(page.getCurrent());
        pageResult.setTotalCount(page.getTotal());
        pageResult.setList(list);
        pageResult.setTotalPage(page.getSize());
        return pageResult;
    }


    @Override
    public List<UserModel> getUserList() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("is_deleted", Constants.IS_DELETE_FALSE);
        return BeanMapper.mapList(userMapper.selectList(queryWrapper),User.class,UserModel.class);
    }

    @Override
    @Transactional
    public void update(UserModel model) {
        User entity=userMapper.selectById(model.getId());
        if(null==entity){
            throw new ApplicationException("您更新的用户不存在") ;
        }
        if(StringUtils.isNotEmpty(model.getPassword())){
            entity.setPassword(MD5.md5(entity.getPassword()));
        }
        if(StringUtils.isNotEmpty(model.getEmail())){
            entity.setEmail(model.getEmail());
        }
        if(StringUtils.isNotEmpty(model.getName())){
            entity.setName(model.getName());
        }
        entity.setStatus(model.getStatus());
        entity.setGmtModified(new Timestamp(System.currentTimeMillis()));
        userMapper.updateById(entity);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id){
        User user=userMapper.selectById(id);
        if(null==user){
            return ;
        }
        user.setIsDeleted(Constants.IS_DELETE_TRUE);
        userMapper.updateById(user);
    }
}
