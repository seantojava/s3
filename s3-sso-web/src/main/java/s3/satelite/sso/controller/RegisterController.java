package s3.satelite.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import s3.satelite.common.utils.S3Result;
import s3.satelite.pojo.TbUser;
import s3.satelite.sso.service.RegisterService;

@Controller
public class RegisterController {

	@Autowired
	private RegisterService registerService;
	
	
	@RequestMapping("/page/regitster")
	public String showRegister(){
		return "register";
	}
	
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public S3Result checkData(@PathVariable String param , 
			@PathVariable Integer type){
		S3Result result = registerService.checkData(param, type);
		return result;
	}
	
	@RequestMapping(value="/user/register", method=RequestMethod.POST)
	@ResponseBody
	public S3Result register(TbUser user){
		S3Result result = registerService.register(user);
		return result;
	}
	
	
	
	
}
