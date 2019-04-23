package org.xujin.moss.sample.service;

import org.xujin.moss.sample.model.OrderModel;

/**
 * 
 * @author xujin
 *
 */
public interface OrderService {
	public OrderModel findOrderByOrderId(Long orderId);
}
