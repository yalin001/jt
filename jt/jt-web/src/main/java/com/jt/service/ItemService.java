package com.jt.service;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;

public interface ItemService {
	//查询商品
	Item findItemById(Long itemId);
	
	//查询商品详情
	ItemDesc findItemDescById(Long itemId);
	
}
