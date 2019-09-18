package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.jt.service.DubboOrderService;
import com.jt.mapper.OrderItemMapper;
import com.jt.mapper.OrderMapper;
import com.jt.mapper.OrderShippingMapper;
import com.jt.pojo.Order;
import com.jt.pojo.OrderItem;
import com.jt.pojo.OrderShipping;

@Service
public class  OrderServiceImpl implements DubboOrderService {
	
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	
	@Transactional
	@Override
	public String saveOrder(Order order) {
		//生成订单id
		String orderId=""+order.getUserId()+System.currentTimeMillis();
		Date date = new Date();
		//实现订单入库
		order.setStatus(1)
				.setOrderId(orderId)
				.setCreated(date)
				.setUpdated(date);
		orderMapper.insert(order);
		System.out.println("订单入库成功");
		//入库订单物流信息
		OrderShipping orderShipping=order.getOrderShipping();
		orderShipping.setOrderId(orderId)
					.setCreated(date)
					.setUpdated(date);
		
		orderShippingMapper.insert(orderShipping);
		System.out.println("物流入库成功");
		//入库订单商品
		List<OrderItem> orderItems = order.getOrderItems();
		for (OrderItem orderItem : orderItems) {
			orderItem.setOrderId(orderId)
						.setCreated(date)
						.setUpdated(date);
			orderItemMapper.insert(orderItem);
			System.out.println("订单商品入库成功");
		}
		return orderId;
	}

	/**
	 * 1.使用三个mapper查询效率低，需要连接三次
	 * */
	
	@Override
	public Order findOrderById(String id) {
		//方法一：执行三次数据库查询
//		Order order = orderMapper.selectById(id);
//		OrderShipping orderShipping = orderShippingMapper.selectById(id);
//		QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<OrderItem>();
//		queryWrapper.eq("order_id", id);
//		
//		List<OrderItem> list = orderItemMapper.selectList(queryWrapper);
//		order.setOrderShipping(orderShipping)
//			.setOrderItems(list);
//		return order;
		//方法二
		
		Order order = orderMapper.selectByOrderId(id);
		
		return order;
	}
	
	
}
