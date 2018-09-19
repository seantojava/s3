package s3.satelite.sso.service;

import s3.satelite.common.utils.S3Result;

public interface LoginService {

	S3Result userLogin(String username, String password);
}
