package s3.satelite.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import s3.satelite.common.utils.CookieUtils;
import s3.satelite.common.utils.S3Result;
import s3.satelite.pojo.TbUser;
import s3.satelite.sso.service.TokenService;

public class LoginInterceptor implements HandlerInterceptor{

	@Autowired
	private TokenService tokenService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//1 从cookie中取token
		String token = CookieUtils.getCookieValue(request, "token");
		//2如果没有token,未登陆状态，直接放行
		if(StringUtils.isBlank(token)){
			return false;
		}
		//3 取到token，需要调用sso系统的服务，根据token取用户信息
		S3Result s3Result = tokenService.getUserByToken(token);
		//4.没有取到用户信息，登陆过期，直接放行
		if(s3Result.getStatus() != 200) {
			return true;
		}
		//5 取到用户信息，登陆状态
		TbUser tbUser = (TbUser) s3Result.getData();
		//6 把用户信息放到request，只需要在controller中判断request中是否包含user信息。放行
		request.setAttribute("user", tbUser);
		
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		//handler执行后，返回modeandview之前
		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
		//完成处理，返回ModelAndview
		//可以再此处理异常
		
	}

	
}
