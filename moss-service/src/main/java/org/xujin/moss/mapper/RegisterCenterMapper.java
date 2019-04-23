package org.xujin.moss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.xujin.moss.entity.RegisterCenter;

import java.util.List;

@Mapper
public interface RegisterCenterMapper extends BaseMapper<RegisterCenter> {

   @Select("SELECT * FROM t_register_center a where a.is_deleted=0 and a.code=#{code}")
   RegisterCenter findRegisterCenterByCode(@Param("code") String code);


   /**
    * 根据Status查询的注册中心列表
    * @return
    */
   @Select("SELECT * FROM t_register_center a where a.is_deleted=0 and a.status=#{status}")
   List<RegisterCenter> findRegisterCenterListByStatus(@Param("status") int status);


}
