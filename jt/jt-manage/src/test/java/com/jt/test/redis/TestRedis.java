package com.jt.test.redis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.Transaction;

public class TestRedis {

	/**
	 * 测试String类型操作
	 * 1.防火墙关闭  2.IP绑定注释  3.保护模式关闭  
	 */
	@Test
	public void test01() {
		Jedis jedis = new Jedis("192.168.15.129", 6379);

		//1.设定key值
		//jedis.set("1904","1904班redis学习");
		String result = jedis.get("1904");

		//2.为key设定超时时间
		//jedis.expire("1904", 100);

		//3.set数据 同时为数据添加超时时间
		jedis.setex("1904", 100, "1904班redis学习");

		//4.需要将key中的值覆盖
		jedis.set("1904","1904班快毕业了");
		System.out.println(jedis.get("1904"));

		//5.删除redis数据
		jedis.del("1904");

		//6.如果当前key已经存在,则不能修改.
		jedis.setnx("1904", "年薪百万");
		System.out.println("获取修改之后的值:"+jedis.get("1904"));

		//7:1.添加超时时间   2.不允许重复操作
		jedis.set("1904A","今天中午吃什么??", "NX", "EX", 50);
		System.out.println(jedis.get("1904A"));
	}

	/**
	 * 操作Hash
	 */
	@Test
	public void testHash() {
		Jedis jedis = new Jedis("192.168.15.129", 6379);
		jedis.hset("person","id", "100");
		jedis.hset("person","name", "人");
		jedis.hset("person","age", "18");
		System.out.println(jedis.hgetAll("person"));
	}

	/**
	 * 测试List集合
	 * 问:该方法执行到第五次获取的结果是??
	 * 答案: 1
	 */
	@Test
	public void testList() {
		Jedis jedis = new Jedis("192.168.15.129", 6379);
		jedis.lpush("list", "1","2","3","4");
		System.out.println(jedis.rpop("list"));
	}

	@Test
	public void testTx() {
		Jedis jedis = new Jedis("192.168.15.129", 6379);
		//开启事物
		Transaction transaction = jedis.multi();
		try {
			//业务操作
			transaction.set("aa", "aaa");
			transaction.set("bb", "bbb");
			transaction.set("cc", "ccc");
			//int a = 1/0;
			//事物提交
			transaction.exec();
		} catch (Exception e) {
			//事物回滚
			transaction.discard();
		}
	}
	
	/**
	 * redis分片测试
	 */
	@Test
	public void testShards() {
		String host = "192.168.15.129";
		List<JedisShardInfo> shards = 
							new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo(host, 6379));
		shards.add(new JedisShardInfo(host, 6380));
		shards.add(new JedisShardInfo(host, 6381));
		ShardedJedis jedis = new ShardedJedis(shards);
		jedis.set("uterldfkgdfg", "分片操作");
		System.out.println(jedis.get("1904"));
	}
	
	/**
	 * 测试哨兵  实现redis高可用
	 */
	@Test
	public void testSentinel() {
		Set<String> sentinels = new HashSet<>();
		sentinels.add("192.168.15.129:26379");
		JedisSentinelPool pool =
				new JedisSentinelPool("mymaster", sentinels) ;
		Jedis jedis = pool.getResource();
		jedis.set("1904", "测试哨兵!!!!");
		System.out.println("获取数据:"+jedis.get("1904"));
	}
	
	
	//明天 10点上课
	@Test
	public void testCluster() {
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.15.129", 7000));
		nodes.add(new HostAndPort("192.168.15.129", 7001));
		nodes.add(new HostAndPort("192.168.15.129", 7002));
		nodes.add(new HostAndPort("192.168.15.129", 7003));
		nodes.add(new HostAndPort("192.168.15.129", 7004));
		nodes.add(new HostAndPort("192.168.15.129", 7005));
		
		JedisCluster cluster = new JedisCluster(nodes);
		cluster.set("1904", "redis集群搭建完成!!!!");
		System.out.println(cluster.get("1904"));
	}
}
