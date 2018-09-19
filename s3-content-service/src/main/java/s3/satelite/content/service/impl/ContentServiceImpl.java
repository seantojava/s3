package s3.satelite.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import s3.satelite.common.jedis.JedisClient;
import s3.satelite.common.pojo.EasyUIDataGridResult;
import s3.satelite.common.utils.JsonUtils;
import s3.satelite.common.utils.S3Result;
import s3.satelite.content.service.ContentService;
import s3.satelite.mapper.TbContentMapper;
import s3.satelite.pojo.TbContent;
import s3.satelite.pojo.TbContentExample;
import s3.satelite.pojo.TbContentExample.Criteria;

@Service
public class ContentServiceImpl implements ContentService {
	
	@Autowired
	private JedisClient jedisClient;

	@Autowired
	private TbContentMapper tbContentMapper;

	@Override
	public S3Result addContent(TbContent tbContent) {

		tbContent.setCreated(new Date());
		tbContent.setUpdated(new Date());

		tbContentMapper.insert(tbContent);
		
		jedisClient.hdel("dfg", tbContent.getCategoryId().toString());
		

		return S3Result.ok();
	}
	
	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;

	// 根据CID查出首页轮播图
	@Override
	public List<TbContent> getTbContentByCid(long cid) {

		try {
			String hget = jedisClient.hget(CONTENT_LIST, cid+"");
			if(StringUtils.isNotBlank(hget)){
				List<TbContent> jsonToList = JsonUtils.jsonToList(hget, TbContent.class);
				return jsonToList;
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(cid);

		List<TbContent> ad1List = tbContentMapper.selectByExampleWithBLOBs(example);

		try {
			jedisClient.hset(CONTENT_LIST, cid+"", JsonUtils.objectToJson(ad1List));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ad1List;
	}

	// 根据categoryid查询广告页展示列表
	@Override
	public EasyUIDataGridResult getTbContentByCatId(long categoryId, int page, int rows) {

		PageHelper.startPage(page, rows);

		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = tbContentMapper.selectByExample(example);
		PageInfo<TbContent> pageinfo = new PageInfo<>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();

		if (list != null && list.size() > 0) {
			result.setRows(list);
			result.setTotal(result.getTotal());

			return result;
		}

		return null;
	}

}
