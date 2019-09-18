package com.jt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.EasyUITree;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

@Service
public class ItemCatServiceImpl implements ItemCatService {
	
	@Autowired
	private ItemCatMapper itemCatMaper;
	
	//@Autowired  后期缓存处理利用AOP完成.
	private ShardedJedis jedis;
	
	
	
	@Override
	public String findItemCatNameById(Long itemCatId) {
		
		ItemCat itemCat = itemCatMaper.selectById(itemCatId);
		return itemCat.getName();
	}
	
	/**
	 * EasyUITree VO对象
	 *   依赖
	 * ItemCat	  数据库对象
	 * 
	  * 思路:
	 * 	1.先查询数据库List信息
	 *  2.将数据库对象转化为VO对象.
	 */
	public List<ItemCat> findItemCatList(Long parentId){
		QueryWrapper<ItemCat> queryWrapper = 
				new QueryWrapper<ItemCat>();
		queryWrapper.eq("parent_id", parentId);
		List<ItemCat> itemCatList = 
				itemCatMaper.selectList(queryWrapper);
		return itemCatList;
	}
	
	@Override
	public List<EasyUITree> findEasyUITreeList(Long parentId) {
		//1.定义返回数据
		List<EasyUITree> treeList = new ArrayList<EasyUITree>();
		List<ItemCat> itemCatList = findItemCatList(parentId);
		for (ItemCat itemCat: itemCatList) {
			EasyUITree easyUITree = new EasyUITree();
			String state = 
					itemCat.getIsParent()?"closed":"open";
			easyUITree.setId(itemCat.getId())
					  .setText(itemCat.getName())
					  //如果是父级菜单应该关闭,否则应该打开
					  .setState(state);
			treeList.add(easyUITree);
		}
		
		return treeList;
	}

	/**
	 * 添加缓存的实现
	 * 业务思路:
	 * 		key:value		
	 * key: key必须保证唯一,存取使用同一个key/可读性强
	 * value: List<EasyUITree>的json数据
	 * 	
	 * 	1.通过生成的key查询redis缓存服务器.
	 *  2.null表示没有数据,需要查询数据库,之后将数据转化为json.保存
	 *  到redis中,方便下次调用.
	 *  3.!null,表示redis缓存中有json数据.需要将json转化为对象.之后返回
	 * 
	 */
	
	
	@Override
	public List<EasyUITree> findEasyUITreeCache(Long parentId) {
		List<EasyUITree> treeList = new ArrayList<EasyUITree>();
		String key = "ITEM_CAT_"+parentId;
		//1.根据key查询redis服务器
		String result = jedis.get(key);
		if(StringUtils.isEmpty(result)) {
			//表示缓存没有数据,需要查询数据库
			treeList = findEasyUITreeList(parentId);
			//将数据保存到缓存中
			String value = ObjectMapperUtil.toJSON(treeList);
			jedis.set(key, value);
			System.out.println("查询后台数据库!!!!!");
		}else {
			//缓存中有数据
			treeList = ObjectMapperUtil.toObject(result, treeList.getClass());
			System.out.println("查询Redis缓存");
		}
		
		return treeList;
	}
}
