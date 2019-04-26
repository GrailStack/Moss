package org.xujin.moss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.xujin.moss.entity.App;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AppMapper extends BaseMapper<App> {

    IPage<App> selectPageVo(Page page, @Param("state") Integer state);

    @Select("SELECT Count(*) FROM t_app where is_deleted=0")
    int totalCount();

    @Select("SELECT Count(*) FROM t_app a where a.is_deleted=0 and a.framework_version=#{version}")
    int totalUseFrameworkCount(@Param("version") int version);


    @Select("SELECT Count(*) FROM t_app a where a.is_deleted=0 and a.spring_boot_version=#{version}")
    int totalUseSbVersionCount(@Param("version") int version);

    @Select("SELECT Count(*) FROM t_app a where a.is_deleted=0 and a.spring_cloud_version=#{version}")
    int totalUseScVersionCount(@Param("version") int version);

    @Select("SELECT Count(*) FROM t_app a where a.is_deleted=0 and a.take_over=#{takeOver}")
    int totalTakeOverCount(@Param("takeOver") int takeOver);

    @Select("SELECT Count(*) FROM t_app a where a.is_deleted=0 and a.owner_id=#{ownerId}")
    int totalMyAppCount(@Param("ownerId") String ownerId);

   /* @Select("select * from t_app a where a.is_deleted=0 and a.project_key=#{projectKey} and  a.status=#{status} and a.take_over=#{takeOver} " +
            " and a.name like %#{name}% ")
    Page<App> selectPageTest(Page<App> page,@Param("status") int status, @Param("projectKey") String projectKey,
                             @Param("name") String name,@Param("takeOver") int takeOver);
*/


}
