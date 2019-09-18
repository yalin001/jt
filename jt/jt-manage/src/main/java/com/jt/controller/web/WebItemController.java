package com.jt.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.anno.Cache_Find;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;

//要求返回的是json串
@RestController
@RequestMapping("/web/item")
public class WebItemController {
	
	@Autowired
	private ItemService itemService;
	/**
	 * "http://manage.jt.com/web/item/findItemById?itemId=562379";
	 */
	@RequestMapping("findItemById")
	@Cache_Find(seconds = 7*24*3600)
	public Item findItemById(Long itemId) {
		
		return itemService.findItemById(itemId);
	}
	
	//http://manage.jt.com/web/item/findItemDescById
	@RequestMapping("findItemDescById")
	@Cache_Find(seconds = 7*24*3600)
	public ItemDesc findItemDescById(Long itemId) {
		
		return itemService.findItemDescById(itemId);
	}
}
