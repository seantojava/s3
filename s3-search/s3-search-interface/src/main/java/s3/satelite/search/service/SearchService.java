package s3.satelite.search.service;

import s3.satelite.common.pojo.SearchResult;

public interface SearchService {

	SearchResult search(String keyword , int page, int rows) throws Exception;
}
