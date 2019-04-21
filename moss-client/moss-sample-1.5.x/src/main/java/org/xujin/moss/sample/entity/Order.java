package org.xujin.moss.sample.entity;

import java.util.Date;

import lombok.Data;

/**
 * 
 * @author xujin
 *
 */
@Data
public class Order {
	private Long orderNo;
	private Date createTime;
	private Date payTime;
}
