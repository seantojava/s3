package s3.satelite.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import s3.satelite.common.pojo.EasyUITreeNode;
import s3.satelite.common.utils.S3Result;
import s3.satelite.content.service.ContentCategoryService;
import s3.satelite.mapper.TbContentCategoryMapper;
import s3.satelite.pojo.TbContentCategory;
import s3.satelite.pojo.TbContentCategoryExample;
import s3.satelite.pojo.TbContentCategoryExample.Criteria;
import s3.satelite.pojo.TbContentExample;


@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	
	@Autowired
	private TbContentCategoryMapper tbContentCategoryMapper;
	@Override
	public List<EasyUITreeNode> getTreeNodeList(long parentId) {
		
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> catList = tbContentCategoryMapper.selectByExample(example);
		
		List<EasyUITreeNode> nodeList = new ArrayList<>();
		
		for (TbContentCategory tbContentCategory : catList) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			
			nodeList.add(node);
		}
		
		return nodeList;
		
	}
	@Override
	public S3Result addContentCategory(long parentId, String name) {
		
		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		
		contentCategory.setStatus(1);
		contentCategory.setSortOrder(1);
		
		contentCategory.setIsParent(false);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		
		tbContentCategoryMapper.insert(contentCategory);
		
		TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(parentId);
		
		if(!parent.getIsParent()){
			parent.setIsParent(true);
			
			tbContentCategoryMapper.updateByPrimaryKey(parent);
		}
		
		
		return S3Result.ok(contentCategory);
	}
	
	
	
	
	@Override
	public S3Result deleteContentCategory(long id) {
		//根据主键ID删除数据
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(id);
		
		List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
		//有BUG，子节点无的时候，不能更改父节点状态
		if(list != null & list.size() > 0 ){
			if(list.get(0).getIsParent()==false){
				tbContentCategoryMapper.deleteByPrimaryKey(id);
			}
			return S3Result.ok();
		}
		
		
		return null;
	}
	
	

}
