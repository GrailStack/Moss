package org.xujin.moss.controller;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xujin.moss.common.ResultData;
import org.xujin.moss.constant.Constants;
import org.xujin.moss.model.MetaDataModel;
import org.xujin.moss.service.DictService;

import java.util.List;

/**
 * 字典控制器
 * @author homeant
 * @date 2019-05-26 10:32:07
 */
@RestController
@RequestMapping("dist")
public class DictController extends BaseController {

    @Autowired
    DictService dictService;

    /**
     * 通过类型获取字典项
     * @param type
     * @return
     */
    @GetMapping("{type}")
    public ResultData fecthDict(@PathVariable("type") String type){
        List<MetaDataModel> dictLists = dictService.findDictDataByDictCodeAndStatus(Constants.DICT_DATA_STATUS_TRUE, type);
        if(CollectionUtils.isNotEmpty(dictLists)){
            return ResultData.ok(dictLists).build();
        }else{
            return ResultData.noContent().build();
        }
    }
}
