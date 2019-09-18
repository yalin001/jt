package com.jt.controller;

import java.util.List;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.Order;
import com.jt.service.DubboCartService;
import com.jt.service.DubboOrderService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/order")
public class OrderController {
	
	@Reference(timeout=3000,check = false)
	private DubboOrderService orderService;
	@Reference(timeout=3000,check = false)
	private DubboCartService cartService;
	/**
	 * 1.跳转订单的确认页面
	 * http://www.jt.com/order/create.html
	 * 页面的跳转：cart-cart
	 * 页面的取值{carts}
	 */
	@RequestMapping("/create")
	public String create(Model model) {
		Long userId = UserThreadLocal.get().getId();
		List<Cart> carts = cartService.findCartListByUserId(userId);
		model.addAttribute("carts", carts);
		return "order-cart";
	}
	/**
	 * 2.实现订单的入库操作
	 * http://www.jt.com/order/submit.html
	 * 页面需要的返回数据：orderId
	 * 页面的取值sysresult.success(orderId)
	 */
	@RequestMapping("/submit")
	@ResponseBody
	public SysResult savaOrder(Order order) {
		//需要返回页面数据
		String orderId=orderService.saveOrder(order);
		return SysResult.success(orderId);
	}
	/**
	 * 3.实现订单的查询
	 * http://www.jt.com/order/submit.html
	 * 页面需要的返回数据：orderId
	 * 页面的取值sysresult.success(orderId)
	 */
	@RequestMapping("/success")
	public String findOrderById(String id,Model model) {
		//需要返回页面数据
		Order order=orderService.findOrderById(id);
		model.addAttribute("order", order);
		return "success";
	}
	/**
	  * 实现个人订单查询
	  */
	 @RequestMapping("myOrder")
	 public String myOrder() {
	  return "my-orders";
	 }
	
}
