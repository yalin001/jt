package com.jt.controller.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.ItemCat;
import com.jt.util.ObjectMapperUtil;

@RestController
public class JSONPController {
	
	/**
	 * JSONP调用,服务器端返回数据的要求:
	 * callback(json串)
	 */
	
	/*
	 * @RequestMapping("/web/testJSONP") public String jsonp(String callback) {
	 * ItemCat itemCat = new ItemCat(); itemCat.setId(10086L);
	 * itemCat.setName("jsonp测试调用!!!!"); String json =
	 * ObjectMapperUtil.toJSON(itemCat); return callback+"("+json+")"; }
	 */
	
	@RequestMapping("/web/testJSONP")
	public JSONPObject jsonp(String callback) {
		ItemCat itemCat = new ItemCat(); 
		itemCat.setId(10086L);
		itemCat.setName("jsonp测试调用!!!!");
		return new JSONPObject(callback, itemCat);
	}
}
