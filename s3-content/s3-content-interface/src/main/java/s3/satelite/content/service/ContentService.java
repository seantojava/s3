package s3.satelite.content.service;

import java.util.List;

import s3.satelite.common.pojo.EasyUIDataGridResult;
import s3.satelite.common.utils.S3Result;
import s3.satelite.pojo.TbContent;

public interface ContentService {
	
	S3Result addContent(TbContent tbContent);
	List<TbContent> getTbContentByCid(long cid);
	EasyUIDataGridResult getTbContentByCatId(long categoryId,int page,int rows);

}
