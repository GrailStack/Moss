package org.xujin.moss.model;

import org.xujin.moss.base.BaseModel;

/**
 * @author xujin
 * 数据字典分类表
 */
public class DictTypeModel extends BaseModel {

	private String dictName;

	private String dictCode;

	private int status;

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
}
