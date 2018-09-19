package s3.satelite.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import s3.satelite.common.jedis.JedisClient;
import s3.satelite.common.utils.JsonUtils;
import s3.satelite.common.utils.S3Result;
import s3.satelite.pojo.TbUser;
import s3.satelite.sso.service.TokenService;

@Service
public class TokenServiceImpl implements TokenService {
	
	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;

	@Override
	public S3Result getUserByToken(String token) {
		
		String json = jedisClient.get("SESSION:" +token);
		//取不到用户信息，表示登陆过期，返回登陆过期
		if(StringUtils.isBlank(json)){
			return S3Result.build(201, "用户登陆已经过期");
		}
		//取用户信息,更新token的过期时间
		jedisClient.expire("SESSION:" +token, SESSION_EXPIRE);
		
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		return S3Result.ok(user);
	}

}
