package s3.satelite.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import s3.satelite.common.utils.S3Result;
import s3.satelite.mapper.TbUserMapper;
import s3.satelite.pojo.TbUser;
import s3.satelite.pojo.TbUserExample;
import s3.satelite.pojo.TbUserExample.Criteria;
import s3.satelite.sso.service.RegisterService;

@Service
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	private TbUserMapper tbUserMapper;
	@Override
	public S3Result checkData(String param, int type) {
		
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		//1：用户名 2：手机号 3：邮箱
		if(type == 1){
			criteria.andUsernameEqualTo(param);
		}else if(type == 2){
			criteria.andPhoneEqualTo(param);
		}else if(type == 3){
			criteria.andEmailEqualTo(param);
		}else{
			return S3Result.build(400, "数据类型错误");
		}
		
		List<TbUser> list = tbUserMapper.selectByExample(example);
		//判断结果中是否含有数据
		if(list != null && list.size() > 0){
			return S3Result.ok(false);
		}
		
		return S3Result.ok(true);
		
	}
	@Override
	public S3Result register(TbUser user) {
		//数据有效性
		if(StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword()) || 
				StringUtils.isBlank(user.getPhone())){
			return S3Result.build(400, "数据不完整，请检查您的数据");
		}
		//1：用户名 2：手机号 3：邮箱
		S3Result result = checkData(user.getUsername(), 1);
		if(!(boolean) result.getData()){
			return S3Result.build(400, "此用户已经被占用");
		}
		result = checkData(user.getPhone(), 2);
		if(!(boolean) result.getData()){
			return S3Result.build(400, "手机已经被占用");
		}
		user.setCreated(new Date());
		user.setUpdated(new Date());
		
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass);
		
		tbUserMapper.insert(user);
		return S3Result.ok();
	}

}
