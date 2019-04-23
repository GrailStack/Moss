package org.xujin.moss.base;


import org.xujin.moss.constant.Constants;

import java.sql.Timestamp;

/**
 * base Model
 * @author xujin
 */
public class BaseModel {

    private Long id;

    private Timestamp gmtCreate;

    private Timestamp gmtModified;

    private Byte isDeleted= Constants.IS_DELETE_FALSE;

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
}
