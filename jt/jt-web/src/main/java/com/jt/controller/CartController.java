package com.jt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.service.DubboCartService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Reference(timeout=3000,check = false)
	private DubboCartService cartService;
	/**
	 * 1.根据用户信息获取购物车列表数据
	 */
	@RequestMapping("/show")
	public String show(Model model) {
		Long userId = 7L; //暂时写死
		List<Cart> cartList = 
		cartService.findCartListByUserId(userId);
		model.addAttribute("cartList", cartList);
		return "cart";
	}
	/**
	 * 2.修改购物车商品数量
	 * 规则:如果{参数}的名称与对象中的属性一致.
	 * 		则可以使用对象直接取值.
	 */
	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateNum(Cart cart) {
		Long userId = UserThreadLocal.get().getId();
		cart.setUserId(userId);
		cartService.updateNum(cart);
		return SysResult.success();
	}
	
	
	
	@RequestMapping("/delete/{itemId}")
	public String deleteCart(Cart cart) {
		Long userId = 7L;
		cart.setUserId(userId);
		cartService.deleteCart(cart);
		return "redirect:/cart/show.html";
	}
	
	
	/**
	 * 4.完成购物车新增
	 */
	@RequestMapping("/add/{itemId}")
	public String saveCart(Cart cart) {
		Long userId = 7L;
		cart.setUserId(userId);
		cartService.insertCart(cart);
		return "redirect:/cart/show.html";
	}
	
	
}
