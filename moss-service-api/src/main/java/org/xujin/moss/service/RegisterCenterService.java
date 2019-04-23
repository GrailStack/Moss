package org.xujin.moss.service;

import org.xujin.moss.common.ResultData;
import org.xujin.moss.common.domain.PageResult;
import org.xujin.moss.model.RegisterCenterModel;
import org.xujin.moss.request.RegisterCenterPageRequest;

import java.util.List;

public interface RegisterCenterService {

    ResultData addRegisterCenter(RegisterCenterModel model) ;

    RegisterCenterModel getRegisterCenterById(Long id);

    PageResult<RegisterCenterModel> findPageByParam(RegisterCenterPageRequest request);

    void update(RegisterCenterModel model) ;

    void deleteRegisterCenterById(Long id) ;

    /**
     * 根据是否启用查询注册中心
     * @param status
     * @return
     */
    List<RegisterCenterModel> findRegisterCenterListByStatus(int status);

}
