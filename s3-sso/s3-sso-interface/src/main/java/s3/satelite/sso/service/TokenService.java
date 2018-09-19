package s3.satelite.sso.service;

import s3.satelite.common.utils.S3Result;

public interface TokenService {

	S3Result getUserByToken(String token);
}
