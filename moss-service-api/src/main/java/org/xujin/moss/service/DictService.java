package org.xujin.moss.service;

import org.xujin.moss.common.ResultData;
import org.xujin.moss.common.domain.PageResult;
import org.xujin.moss.model.DictDataModel;
import org.xujin.moss.model.DictTypeModel;
import org.xujin.moss.model.MetaDataModel;
import org.xujin.moss.request.DictTypeListRequest;
import org.xujin.moss.vo.DictTypeListVO;

import java.util.List;

public interface DictService {

    /**
     * 增加数据字典类型
     * @param dictTypeModel
     */
    ResultData addDictType(DictTypeModel dictTypeModel);

    /**
     * 更新数据字典类型
     * @param dictTypeModel
     */
    void updateDictType(DictTypeModel dictTypeModel);

    /**
     * 通过id删除数据字典类型
     * @param id
     */
    void deleteDictType(Long id);

    /**
     * 查询数据字典类型并分页
     * @param dictTypeListRequest
     * @return
     */
    PageResult<DictTypeListVO> findByPageVague(DictTypeListRequest dictTypeListRequest);

    /**
     * 增加数据字典类型数据
     * @param dictDataModel
     */
    void addDictData(DictDataModel dictDataModel);

    /**
     * 更新数据字典某个具体的项
     * @param dictDataModel
     */
    void updateDictData(DictDataModel dictDataModel);

    /**
     * 通过id删除数据字典项
     * @param id
     */
    void deleteDictData(Long id);

    List<MetaDataModel> findDictDataByDictCodeAndStatus(int status, String dictCode);

    List<DictTypeModel> findDictTypeList();

}
