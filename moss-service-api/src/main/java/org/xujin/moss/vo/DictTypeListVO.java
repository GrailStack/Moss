package org.xujin.moss.vo;

import org.xujin.moss.constant.Constants;
import org.xujin.moss.model.DictDataModel;

import java.sql.Timestamp;
import java.util.List;

public class DictTypeListVO {

    private String dictName;

    private String dictCode;

    private int status;

    private Long id;

    private Timestamp gmtCreate;

    private Timestamp gmtModified;

    private Byte isDeleted= Constants.IS_DELETE_FALSE;

    private List<DictDataModel> dictDataModelList;

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Timestamp gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Timestamp getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Timestamp gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
    }

    public List<DictDataModel> getDictDataModelList() {
        return dictDataModelList;
    }

    public void setDictDataModelList(List<DictDataModel> dictDataModelList) {
        this.dictDataModelList = dictDataModelList;
    }
}
