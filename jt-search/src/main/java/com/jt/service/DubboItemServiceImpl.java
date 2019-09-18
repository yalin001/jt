package com.jt.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;

@Service
public class DubboItemServiceImpl implements DubboItemService {
	
	@Autowired
	private ItemMapper itemMaper;
	
	@Override
	public List<Item> findItemByTitle(String keyword) {
		QueryWrapper<Item> queryWrapper = 
				new QueryWrapper<Item>().like("title", keyword);
		List<Item> itemList = 
				itemMaper.selectList(queryWrapper);
//		List<Item> itemList = 
//				itemMaper.selectItemByTitle(keyword);
		return itemList;
	}



}
