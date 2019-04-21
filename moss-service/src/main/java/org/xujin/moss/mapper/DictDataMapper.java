package org.xujin.moss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.xujin.moss.entity.DictData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DictDataMapper extends BaseMapper<DictData> {

    @Select("SELECT * FROM t_dict_data where is_deleted=0 and dict_code=#{dictCode}")
    List<DictData> findDictDataByDictCode(@Param("dictCode") String dictCode);

    @Update("update t_dict_data set is_deleted=1  where dict_code=#{dictCode} ")
    void batchDeleteDictDataByDictCode(@Param("dictCode") String dictCode);

    @Select("SELECT * FROM t_dict_data where is_deleted=0 and status=#{status} and dict_code=#{dictCode}")
    List<DictData> findDictDataByDictCodeAndStatus(@Param("dictCode") String dictCode,@Param("status") int status);




}
