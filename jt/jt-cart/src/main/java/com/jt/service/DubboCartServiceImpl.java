package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;

@Service(timeout = 3000)
public class DubboCartServiceImpl implements DubboCartService {
	
	@Autowired
	private CartMapper cartMapper;

	@Override
	public List<Cart> findCartListByUserId(Long userId) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
		queryWrapper.eq("user_id", userId);
		return cartMapper.selectList(queryWrapper);
	}
	
	/**
	 * 根据商品数量信息
	 * update tb_user set 
	 * 				  num=#{num},
	 * 				  updated=#{updated}
	 * where user_id=#{userId} and item_id = #{itemId}
	 * 
	 */
	@Override
	@Transactional
	public void updateNum(Cart cart) {
		Cart cartTemp = new Cart();
		cartTemp.setNum(cart.getNum())
				.setUpdated(new Date());
		UpdateWrapper<Cart> updateWrapper = 
							new UpdateWrapper<Cart>();
		updateWrapper.eq("user_id", cart.getUserId())
					 .eq("item_id", cart.getItemId());
		cartMapper.update(cartTemp, updateWrapper);
	}

	/**
	 * sql: delete from tb_cart 
	 * 		where 
	 * 			item_id=#{itemId} 
	 * 			and 
	 * 			user_id=#{userId}
	 */
	@Override
	@Transactional
	public void deleteCart(Cart cart) {
		QueryWrapper<Cart> queryWrapper = 
				new QueryWrapper<Cart>(cart);
		//根据对象中不为null的属性,充当where条件
		cartMapper.delete(queryWrapper);
	}

	
	/**
	 * 购物新增实现思路:
	 * 	1.在入库操作之前,必须先查询数据库 user_id和itemId
	 * 	2.null:	表示用户之前没有购买该商品,新增入库
	 *  3.!null:已经加入购物车,数量更新.
	 */
	@Override
	@Transactional
	public void insertCart(Cart cart) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
		queryWrapper.eq("user_id", cart.getUserId())
					.eq("item_id", cart.getItemId());
		Cart cartDB = cartMapper.selectOne(queryWrapper);
		if(cartDB == null) {
			cart.setCreated(new Date())
				.setUpdated(cart.getCreated());
			cartMapper.insert(cart);
		}else {
			int num = cart.getNum() + cartDB.getNum();
			Cart cartTemp = new Cart();
			cartTemp.setNum(num)
					.setUpdated(new Date());
			UpdateWrapper<Cart> upWrapper = new UpdateWrapper<>();
			upWrapper.eq("id", cartDB.getId());
			cartMapper.update(cartTemp, upWrapper);
		}
	}
}
