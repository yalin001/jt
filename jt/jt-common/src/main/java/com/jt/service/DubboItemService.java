package com.jt.service;

import java.util.List;

import com.alibaba.dubbo.config.annotation.Service;
import com.jt.pojo.Item;

@Service
public interface DubboItemService {

	List<Item> findItemByTitle(String keyword);
	//查询商品
	
}
