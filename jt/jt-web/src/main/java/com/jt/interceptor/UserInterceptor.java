package com.jt.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.UserThreadLocal;

import redis.clients.jedis.JedisCluster;

/**
 * 定义用户拦截器.实现用户权限判断
 * @author tarena
 * spring版本4, 必须重写拦截器方法.
 * spring版本5  不必全部重写.需要哪个写哪个. default
 * 		
 */
@Component
public class UserInterceptor implements HandlerInterceptor{
	
	@Autowired
	private JedisCluster jedisCluster;
	/**
	 * 重写perhandler校验用户是否登录
	 * 
	 * boolean:
	 * 		true:	请求放行
	 * 		false:  请求拦截
	 * 				一般会有重定向执行
	 * 用户登录校验的业务实现
	 * 	1.判断用户是否登录 cookie JT_TICKET
	 * 	2.检查redis中是否有数据.
	 *  3.return true
	 * 
	 * 问题:拦截器中的user对象如何传递给Controller
	 * 
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//1.获取用户cookie信息
		Cookie[] cookies = request.getCookies();
		String ticket = null;
		if(cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if("JT_TICKET".equals(cookie.getName())) {
					ticket = cookie.getValue();
					break;
				}
			}
		}
		
		//判断取值是否有效 不为null时校验信息
		if(!StringUtils.isEmpty(ticket)) {
			String userJSON = jedisCluster.get(ticket);
			if(!StringUtils.isEmpty(userJSON)) {
				//方式1:该方式公司中初级程序员必会 request
				//request.setAttribute("JT_USER", user);
				
				//方式2:使用ThreadLocal实现
				User user = ObjectMapperUtil.toObject(userJSON, User.class);
				UserThreadLocal.set(user);
				return true;	
			}
		}
		
		//重定向到用户登录页面
		response.sendRedirect("/user/login.html");
		return false; //表示请求拦截 
	}
	/*删除threadlocal线程,防止内存泄漏*/
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		UserThreadLocal.remove();
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
	
}
