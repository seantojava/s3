package s3.satelite.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import s3.satelite.common.utils.CookieUtils;
import s3.satelite.common.utils.S3Result;
import s3.satelite.sso.service.LoginService;

@Controller
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;

	@RequestMapping("/page/login")
	public String showLogin(){
		return "login";
	}
	
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	@ResponseBody
	public S3Result login(String username, String password,
			HttpServletRequest request, HttpServletResponse response){
		S3Result s3Result = loginService.userLogin(username, password);
		if(s3Result.getStatus() == 200){
			String token = s3Result.getData().toString();
			CookieUtils.setCookie(request, response, TOKEN_KEY, token);
		}
		
		
		return s3Result;
	}
	
	
	
	
	
}
