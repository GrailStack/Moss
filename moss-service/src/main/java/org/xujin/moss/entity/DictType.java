package org.xujin.moss.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author xujin
 * 数据字典分类表
 */
@TableName("t_dict_type")
public class DictType extends BaseEntity {

	@TableField("dict_name")
	private String dictName;

	@TableField("dict_code")
	private String dictCode;

	@TableField("status")
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
