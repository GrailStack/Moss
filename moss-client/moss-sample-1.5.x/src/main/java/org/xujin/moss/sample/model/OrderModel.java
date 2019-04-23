package org.xujin.moss.sample.model;

import java.util.Date;

import lombok.Data;

@Data
public class OrderModel {
	private Long orderNo;
	private Date createTime;
	private Date payTime;

}
