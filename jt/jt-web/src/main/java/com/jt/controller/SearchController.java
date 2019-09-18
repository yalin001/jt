package com.jt.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Jedis;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.pojo.Order;
import com.jt.pojo.User;
import com.jt.service.DubboItemService;
import com.jt.service.DubboUserService;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/")
public class SearchController {
		
	@Reference(timeout = 3000,check = false)
	private DubboUserService userService;
	@Reference(timeout = 3000,check = false)
	private DubboItemService itemService;
	
	/**
	 * 实现查询商品，根据用户的搜索
	 * http://www.jt.com/search.html
	 * http://www.jt.com/search.html?q=%E6%89%8B%E6%9C%BA
	 */
	@RequestMapping("/search")
	public String searchItems(String q,Model model) {
		System.out.println(q);
		
		List<Item> itemList=itemService.findItemByTitle(q);
		boolean empty = itemList.isEmpty();
		System.out.println(empty);
		//需要返回页面数据
		model.addAttribute("itemList", itemList);
		
//		for (Item item : itemList) {
//			System.out.println(item.toString());
//		}
		System.out.println("执行了search方法");
		
		return "search";
	}
	
}
