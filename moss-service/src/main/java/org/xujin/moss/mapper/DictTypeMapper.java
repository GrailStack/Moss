package org.xujin.moss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.xujin.moss.entity.DictType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DictTypeMapper extends BaseMapper<DictType> {

    @Select("SELECT * FROM t_dict_type where is_deleted=0 and dict_code=#{dictCode}")
    DictType findByDictCode(@Param("dictCode") String dictCode);

    @Select("SELECT * FROM t_dict_type where is_deleted=0")
    List<DictType> findDictTypeList();

}
