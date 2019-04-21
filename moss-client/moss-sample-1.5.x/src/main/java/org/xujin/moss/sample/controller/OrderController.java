package org.xujin.moss.sample.controller;

import org.xujin.moss.sample.model.OrderModel;
import org.xujin.moss.sample.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * OrderController
 * @author xujin
 *
 */
@RestController
public class OrderController {

	@Autowired
	private OrderService orderService;

	@GetMapping("/sc/order/{id}")
	public OrderModel findOrderById(@PathVariable Long id) {
		OrderModel orderModel = orderService.findOrderByOrderId(id);
		return orderModel;
	}

}
