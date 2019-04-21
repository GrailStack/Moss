package org.xujin.moss.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 数据字典详情表
 * @author xujin
 */
@TableName("t_dict_data")
public class DictData extends  BaseEntity{

	@TableField("dict_code")
	private String dictCode;

	@TableField("item_name")
	private String itemName;

	@TableField("item_value")
	private String itemValue;

	@TableField("item_desc")
	private String itemDesc;

	@TableField("item_sort")
	private int itemSort;

	@TableField("status")
	private int status;

	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemValue() {
		return itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	public int getItemSort() {
		return itemSort;
	}

	public void setItemSort(int itemSort) {
		this.itemSort = itemSort;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
