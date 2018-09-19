package s3.satelite.sso.service;

import s3.satelite.common.utils.S3Result;
import s3.satelite.pojo.TbUser;

public interface RegisterService {

	S3Result checkData(String param,int type);
	S3Result register(TbUser user);
}
