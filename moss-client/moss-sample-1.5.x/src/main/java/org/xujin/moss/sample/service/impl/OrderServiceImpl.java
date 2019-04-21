package org.xujin.moss.sample.service.impl;

import java.util.Date;

import org.xujin.moss.sample.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.xujin.moss.sample.entity.Order;
import org.xujin.moss.sample.model.OrderModel;

@Service
public class OrderServiceImpl implements OrderService {
	public OrderModel findOrderByOrderId(Long orderId) {
		OrderModel orderModel = new OrderModel();
		if (orderId.equals(2L)) {
			Order order = new Order();
			order.setCreateTime(new Date());
			order.setOrderNo(2L);
			BeanUtils.copyProperties(order, orderModel);
		}
		return orderModel;
	}

}
