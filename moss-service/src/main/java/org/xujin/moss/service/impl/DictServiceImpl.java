package org.xujin.moss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.xujin.moss.common.ResultData;
import org.xujin.moss.common.domain.PageResult;
import org.xujin.moss.constant.Constants;
import org.xujin.moss.entity.DictData;
import org.xujin.moss.entity.DictType;
import org.xujin.moss.mapper.DictDataMapper;
import org.xujin.moss.mapper.DictTypeMapper;
import org.xujin.moss.model.DictDataModel;
import org.xujin.moss.model.DictTypeModel;
import org.xujin.moss.model.MetaDataModel;
import org.xujin.moss.request.DictTypeListRequest;
import org.xujin.moss.service.DictService;
import org.xujin.moss.utils.BeanMapper;
import org.xujin.moss.vo.DictTypeListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceImpl implements DictService {

    @Autowired
    private DictDataMapper dictDataMapper;

    @Autowired
    private DictTypeMapper dictTypeMapper;

    @Override
    @Transactional
    public ResultData addDictType(DictTypeModel dictTypeModel) {
        DictType dictType=dictTypeMapper.findByDictCode(dictTypeModel.getDictCode());
        if(null!=dictType){
            return ResultData.builder().msgCode("400").msgContent("数据字典Code:"+dictTypeModel.getDictCode()+"不能重复").build();
        }
        dictTypeModel.setIsDeleted(Constants.IS_DELETE_FALSE);
        dictTypeModel.setGmtCreate(new Timestamp(System.currentTimeMillis()));
        dictTypeModel.setGmtModified(new Timestamp(System.currentTimeMillis()));
        dictTypeMapper.insert(BeanMapper.map(dictTypeModel, DictType.class));
        return ResultData.builder().build();
    }

    @Override
    @Transactional
    public void updateDictType(DictTypeModel dictTypeModel) {

        DictType dictType=dictTypeMapper.selectById(dictTypeModel.getId());
        if(null==dictType){
            return ;
        }
        dictType.setGmtModified(new Timestamp(System.currentTimeMillis()));
        dictTypeMapper.updateById(BeanMapper.map(dictTypeModel,DictType.class));

    }

    @Override
    @Transactional
    public void deleteDictType(Long id) {
        DictType dictType=dictTypeMapper.selectById(id);
        if(null==dictType){
            return ;
        }
        dictType.setIsDeleted(Constants.IS_DELETE_TRUE);
        dictTypeMapper.updateById(dictType);
        dictDataMapper.batchDeleteDictDataByDictCode(dictType.getDictCode());

    }

    @Override
    public PageResult<DictTypeListVO> findByPageVague(DictTypeListRequest dictTypeListRequest) {
        Page pageRequest = new Page(dictTypeListRequest.getPageNo(),dictTypeListRequest.getPageSize());
        QueryWrapper<DictType> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(dictTypeListRequest.getDictCode())){
            queryWrapper.like("dict_code",dictTypeListRequest.getDictCode());
        }
        if(StringUtils.isNotEmpty(dictTypeListRequest.getDictName())){
            queryWrapper.like("dict_name",dictTypeListRequest.getDictName());
        }
        queryWrapper.eq("is_deleted",Constants.IS_DELETE_FALSE);
        IPage<DictType> page=dictTypeMapper.selectPage(pageRequest, queryWrapper);
        List<DictTypeListVO> list= BeanMapper.mapList(page.getRecords(),DictType.class,DictTypeListVO.class);
        for (DictTypeListVO dictTypeListVO:list) {
            List<DictData> dictDataList= dictDataMapper.findDictDataByDictCode(dictTypeListVO.getDictCode());
            dictTypeListVO.setDictDataModelList(BeanMapper.mapList(dictDataList,DictData.class,DictDataModel.class));
        }
        PageResult<DictTypeListVO> pageResult=new PageResult<DictTypeListVO>();
        pageResult.setCurrentPage(page.getCurrent());
        pageResult.setTotalCount(page.getTotal());
        pageResult.setList(list);
        pageResult.setTotalPage(page.getSize());
        return pageResult;
    }

    @Override
    @Transactional
    public void addDictData(DictDataModel dictDataModel) {
        dictDataMapper.insert(BeanMapper.map(dictDataModel, DictData.class));
    }

    @Override
    @Transactional
    public void updateDictData(DictDataModel dictDataModel) {
        DictData dictData=dictDataMapper.selectById(dictDataModel.getId());
        if(null==dictData){
            return ;
        }
        dictData.setGmtModified(new Timestamp(System.currentTimeMillis()));
        dictDataMapper.updateById(BeanMapper.map(dictDataModel,DictData.class));
    }

    @Override
    @Transactional
    public void deleteDictData(Long id) {
        DictData dictData=dictDataMapper.selectById(id);
        if(null==dictData){
            return ;
        }
        dictData.setIsDeleted(Constants.IS_DELETE_TRUE);
        dictDataMapper.updateById(dictData);
    }

    @Override
    public List<MetaDataModel> findDictDataByDictCodeAndStatus(int status, String dictCode) {
        List<DictData> dictDataList= dictDataMapper.findDictDataByDictCodeAndStatus(dictCode,status);
        List list=new ArrayList<>();
        for (DictData dictData :dictDataList) {
            MetaDataModel metaDataModel=new MetaDataModel();
            metaDataModel.setValue(dictData.getItemValue());
            metaDataModel.setName(dictData.getItemName());
            metaDataModel.setDesc(dictData.getItemDesc());
            list.add(metaDataModel);
        }
        return list;

    }

    @Override
    public List<DictTypeModel> findDictTypeList() {
        return BeanMapper.mapList(dictTypeMapper.findDictTypeList(),DictType.class,DictTypeModel.class);
    }
}
