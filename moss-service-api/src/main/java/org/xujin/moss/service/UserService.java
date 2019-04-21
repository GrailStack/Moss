package org.xujin.moss.service;

import org.xujin.moss.common.ResultData;
import org.xujin.moss.common.domain.PageResult;
import org.xujin.moss.model.UserModel;
import org.xujin.moss.request.UserPageListRequest;
import org.xujin.moss.vo.UserVO;

import java.util.List;

public interface UserService {

    ResultData addUser(UserModel model) ;

    UserModel getUserById(Long id);

    PageResult<UserVO> findPageByParam(UserPageListRequest userPageListRequest);

    List<UserModel> getUserList();

    void update(UserModel model) ;

    void deleteUserById(Long id) ;

    UserModel getUserByUserNameAndPassWord(String username,String password);


    UserModel getUserByUserName(String userName);

}
