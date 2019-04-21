package org.xujin.moss.controller;

import org.xujin.moss.common.ResultData;
import org.xujin.moss.common.domain.PageResult;
import org.xujin.moss.constant.Constants;
import org.xujin.moss.model.DictDataModel;
import org.xujin.moss.model.DictTypeModel;
import org.xujin.moss.model.MetaDataModel;
import org.xujin.moss.model.RegisterCenterModel;
import org.xujin.moss.request.DictTypeListRequest;
import org.xujin.moss.service.DictService;
import org.xujin.moss.service.RegisterCenterService;
import org.xujin.moss.vo.DictTypeListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/metadata")
public class MetaDataController {

    @Autowired
    private DictService dictService;

    @Autowired
    private RegisterCenterService registerCenterService;


    @GetMapping("/list")
    public ResultData metaDataList() {
        Map<String,Object> map=new HashMap<>();
        List<DictTypeModel> dictTypeModelList= dictService.findDictTypeList();
        for (DictTypeModel dictTypeModel: dictTypeModelList) {
            map.put(dictTypeModel.getDictCode(),dictService.findDictDataByDictCodeAndStatus(Constants.DICT_DATA_STATUS_TRUE,dictTypeModel.getDictCode()));
        }
        List<RegisterCenterModel> registerCenterModels= registerCenterService.findRegisterCenterListByStatus(Constants.REGISTER_CENTER_ENABLE);
        List list=new ArrayList<>();
        if(null!=registerCenterModels&&registerCenterModels.size()>1){
            MetaDataModel initMetaDataModel=new MetaDataModel();
            initMetaDataModel.setName(Constants.REGISTERCENTER_ALL);
            initMetaDataModel.setValue("");
            list.add(initMetaDataModel);
            for (RegisterCenterModel registerCenterModel:registerCenterModels) {
                MetaDataModel metaDataModel=new MetaDataModel();
                metaDataModel.setValue(registerCenterModel.getCode());
                metaDataModel.setName(registerCenterModel.getName());
                metaDataModel.setDesc(registerCenterModel.getDesc());
                list.add(metaDataModel);
            }
            map.put(Constants.REGISTERCENTER,list);

        }
        return ResultData.builder().data(map).build();
    }

    /**
     * @param model
     * @return
     */
    @PostMapping("/pageList")
    ResultData searchDictByPage(@RequestBody DictTypeListRequest model) {
        PageResult<DictTypeListVO> pageResult= dictService.findByPageVague(model);
        return ResultData.builder().data(pageResult).build();
    }

    /**
     * 增加数据字典类型
     * @param dictTypeModel
     * @return
     */
    @PostMapping("/addDictType")
    public ResultData addDictType(@RequestBody DictTypeModel dictTypeModel) {
        return dictService.addDictType(dictTypeModel);
    }

    /**
     * 更新数据字典类型
     * @param dictTypeModel
     * @return
     */
    @PostMapping("/updateDictType")
    public ResultData updateDictType(@RequestBody DictTypeModel dictTypeModel) {
        dictService.updateDictType(dictTypeModel);
        return ResultData.builder().build();
    }

    /**
     * 删除数据字典类型
     * @param id
     * @return
     */
    @GetMapping("/deleteDictType/{id}")
    public ResultData deleteDictType(@PathVariable Long id) {
        dictService.deleteDictType(id);
        return ResultData.builder().build();
    }

    /**
     * 增加数据字典项
     * @param DictDataModel
     * @return
     */
    @PostMapping("/addDictData")
    public ResultData addDictData(@RequestBody DictDataModel DictDataModel) {
        dictService.addDictData(DictDataModel);
        return ResultData.builder().build();
    }

    /**
     * 更新数据字典项
     * @param DictDataModel
     * @return
     */
    @PostMapping("/updateDictData")
    public ResultData updateDictData(@RequestBody DictDataModel DictDataModel) {
        dictService.updateDictData(DictDataModel);
        return ResultData.builder().build();
    }

    /**
     * 删除数据字典项
     * @param id
     * @return
     */
    @GetMapping("/deleteDictData/{id}")
    public ResultData deleteDictData(@PathVariable Long id) {
        dictService.deleteDictData(id);
        return ResultData.builder().build();
    }

}
