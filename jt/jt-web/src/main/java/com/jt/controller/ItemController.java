package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;

@Controller
@RequestMapping("/items")
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	/**
	 * 实现商品详情展现.
	 */
	 @RequestMapping("/{itemId}") 
	 public String findItemById(
		 @PathVariable Long itemId,Model model) {
		 Item item = itemService.findItemById(itemId);
		 ItemDesc itemDesc = itemService.findItemDescById(itemId);
		 model.addAttribute("item", item);
		 model.addAttribute("itemDesc", itemDesc);
		 return "item"; //跳转到商品展现页面
	 }
}	
