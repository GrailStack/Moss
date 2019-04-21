package org.xujin.moss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.xujin.moss.entity.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * ProjectModel
 * @author xujin
 */
@Mapper
public interface ProjectMapper extends BaseMapper<Project> {

    @Select("SELECT Count(*) FROM t_project where is_deleted=0")
    int totalConut();

    @Select("SELECT Count(*) FROM t_project a where a.is_deleted=0 and a.owner_id=#{ownerId}")
    int totalMyprojectConut(@Param("ownerId") String ownerId);

    @Select("SELECT * FROM t_project a where a.is_deleted=0 and a.key=#{key}")
    Project findProjectByKey(@Param("key") String key);

}
