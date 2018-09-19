package s3.satelite.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import s3.satelite.common.jedis.JedisClient;
import s3.satelite.common.utils.JsonUtils;
import s3.satelite.common.utils.S3Result;
import s3.satelite.mapper.TbUserMapper;
import s3.satelite.pojo.TbUser;
import s3.satelite.pojo.TbUserExample;
import s3.satelite.pojo.TbUserExample.Criteria;
import s3.satelite.sso.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private TbUserMapper tbUserMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	@Override
	public S3Result userLogin(String username, String password) {
		
		//判断用户和密码是否正确
		//根据用户名查询用户信息
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		//执行查询
		List<TbUser> list = tbUserMapper.selectByExample(example);
		if(list == null && list.size() == 0){
			return S3Result.build(400, "用户或密码错误");
		}
		TbUser tbUser = list.get(0);
		if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(tbUser.getPassword())){
			return S3Result.build(400, "用户或密码错误");
		}
		//如果正确，生产token
		String token = UUID.randomUUID().toString();
		tbUser.setPassword(null);
		jedisClient.set("SESSION:" + token, JsonUtils.objectToJson(tbUser));
		//设置session过期时间
		jedisClient.expire("SESSION:" + token, SESSION_EXPIRE);
		
		return S3Result.ok(token);
	}

}
