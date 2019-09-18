package com.jt.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;

	/**
	 * 根据用户输入的信息,校验用户数据.
	 * type: 1 username  2 phone 3 email
	 * param:用户需要校验的信息.
	 */
	@Override
	public boolean findCheckUser(String param, Integer type) {
		//1.需要将type类型转化为具体的字段.
		String column = 
		(type==1)?"username":(type==2?"phone":"email");
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(column, param);
		User user = userMapper.selectOne(queryWrapper);
		//User user2 = userMapper.findUserByCheck(column,param);
		return user==null?false:true;
	}
}
