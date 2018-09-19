package s3.satelite.content.service;

import java.util.List;

import s3.satelite.common.pojo.EasyUITreeNode;
import s3.satelite.common.utils.S3Result;

public interface ContentCategoryService {

	List<EasyUITreeNode> getTreeNodeList(long parentId);
	S3Result addContentCategory(long parentId , String name);
	S3Result deleteContentCategory(long id);
}
