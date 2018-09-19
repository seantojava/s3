package s3.satelite.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import s3.satelite.common.pojo.EasyUITreeNode;
import s3.satelite.mapper.TbItemCatMapper;
import s3.satelite.pojo.TbItemCat;
import s3.satelite.pojo.TbItemCatExample;
import s3.satelite.pojo.TbItemCatExample.Criteria;
import s3.satelite.service.TbItemCatService;

@Service
public class TbItemCatServiceImpl implements TbItemCatService {

	@Autowired
	private TbItemCatMapper tbItemCatMapper;
	
	@Override
	public List<EasyUITreeNode> getTbItemCatList(long parentId) {
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat> list = tbItemCatMapper.selectByExample(example);
		
		List<EasyUITreeNode> resultList = new ArrayList<>();
		for (TbItemCat tbItemCat : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbItemCat.getId());
			node.setText(tbItemCat.getName());
			node.setState(tbItemCat.getIsParent()?"closed":"open");
			
			resultList.add(node);
			
		}
		
		return resultList;
	}

}
