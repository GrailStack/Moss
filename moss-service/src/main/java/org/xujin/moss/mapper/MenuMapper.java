package org.xujin.moss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.xujin.moss.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *
 * @Description:
 * @Author: xujin
 * @Create: 2018/10/15 15:12
 **/
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    @Select("SELECT * from t_menu where is_deleted=0 ORDER BY sort DESC;")
    List<Menu> getMenuList();
}
