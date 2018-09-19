package s3.satelite.service;

import java.util.List;

import s3.satelite.common.pojo.EasyUITreeNode;

public interface TbItemCatService {

	List<EasyUITreeNode> getTbItemCatList(long parentId);
}
