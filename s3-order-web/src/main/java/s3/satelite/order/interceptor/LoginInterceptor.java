package s3.satelite.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import s3.satelite.cart.service.CartService;
import s3.satelite.common.utils.CookieUtils;
import s3.satelite.common.utils.JsonUtils;
import s3.satelite.common.utils.S3Result;
import s3.satelite.pojo.TbItem;
import s3.satelite.pojo.TbUser;
import s3.satelite.sso.service.TokenService;

public class LoginInterceptor implements HandlerInterceptor{

	@Value("${SSO_URL}")
	private String SSO_URL;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private CartService cartService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		//从cookie中取token
		String token = CookieUtils.getCookieValue(request, "token");
		//判断token是否存在
		if(StringUtils.isBlank(token)){
			//如果token不存在，未登陆状态，跳转到sso系统的登陆页面。用户登陆成功后，跳转到当前请求的url
			response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
			//拦截
			return false;
		}
		//如果token存在，需要 调用sso系统的服务，根据token取用户信息
		S3Result s3Result = tokenService.getUserByToken(token);
		//如果取不到，用户登陆已经过期，需要登陆
		if(s3Result.getStatus() == 200) {
			//如果token不存在，未登陆状态，跳转到sso系统的登陆页面。用户登陆成功后，跳转到当前请求的url
			response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
			//拦截
			return false;
		}
		//如果取到用户信息，是登陆状态，需要把用户信息写入request
		TbUser user = (TbUser) s3Result.getData();
		request.setAttribute("user", user);
		//判断cookie中是否有购物车数据，如果有就合并到服务端
		String jsonCartList = CookieUtils.getCookieValue(request, "cart",true);
		if(StringUtils.isNoneBlank(jsonCartList)){
			//合并购物车
			cartService.mergeCart(user.getId(), JsonUtils.jsonToList(jsonCartList, TbItem.class));
		}
		
		return true;
	}
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	
	

}
