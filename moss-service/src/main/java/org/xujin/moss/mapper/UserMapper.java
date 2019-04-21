package org.xujin.moss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.xujin.moss.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * UserVO
 * @author xujin
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM t_user u where u.is_deleted=0 and u.username=#{userName}")
    User findUserByUserName(@Param("userName") String userName);

    @Select("SELECT * FROM t_user u where u.is_deleted=0 and u.username=#{userName} and u.password=#{password}")
    User findUserByUserNameAndPassword(@Param("userName") String userName,@Param("password") String password );




}

