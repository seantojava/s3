package s3.satelite.service.impl;

import java.util.Date;
import java.util.List;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import s3.satelite.common.jedis.JedisClient;
import s3.satelite.common.pojo.EasyUIDataGridResult;
import s3.satelite.common.utils.IDUtils;
import s3.satelite.common.utils.JsonUtils;
import s3.satelite.common.utils.S3Result;
import s3.satelite.mapper.TbItemDescMapper;
import s3.satelite.mapper.TbItemMapper;
import s3.satelite.pojo.TbItem;
import s3.satelite.pojo.TbItemDesc;
import s3.satelite.pojo.TbItemExample;
import s3.satelite.pojo.TbItemExample.Criteria;
import s3.satelite.service.TbItemService;

@Service
public class TbItemServiceImpl implements TbItemService {

	@Autowired
	private TbItemMapper tbItemMapper;
	@Autowired
	private TbItemDescMapper tbItemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private Destination topicDestination;
	@Autowired
	private JedisClient jedisClient;

	@Value("${REDIS_ITEM_PRE}")
	private String REDIS_ITEM_PRE;
	@Value("${ITEM_CACHE_EXPIRE}")
	private Integer ITEM_CACHE_EXPIRE;

	@Override
	public TbItem getTbItemById(long itemId) {

		// 查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":BASE");
			if (StringUtils.isNotBlank(json)) {
				TbItem TbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				return TbItem;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);
		List<TbItem> list = tbItemMapper.selectByExample(example);
		if (list != null && list.size() > 0) {

			// 添加缓存
			try {
				jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":BASE", JsonUtils.objectToJson(list.get(0)));
				jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":BASE", ITEM_CACHE_EXPIRE);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return list.get(0);
		}
		return null;
	}

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		PageHelper.startPage(page, rows);

		TbItemExample example = new TbItemExample();
		List<TbItem> list = tbItemMapper.selectByExample(example);

		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal((int) pageInfo.getTotal());
		result.setRows(list);

		return result;
	}

	@Override
	public S3Result addTbItem(TbItem item, String desc) {
		final long itemId = IDUtils.genItemId();
		item.setId(itemId);
		// 1-正常，2-下架，3-删除
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		tbItemMapper.insert(item);
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());

		tbItemDescMapper.insert(itemDesc);

		// 发送商品添加成功消息
		jmsTemplate.send(topicDestination, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(itemId + "");
				return textMessage;
			}
		});
		
		//发送商品添加成功消息后，再把新增的产品加入到solr中   80635035
		
		

		return S3Result.ok();
	}

	@Override
	public S3Result deleteTbitem(long itemId) {
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);
		tbItemMapper.deleteByExample(example);

		return S3Result.ok();
	}

	@Override
	public TbItemDesc getTbItemDescById(long itemId) {
		// 查询缓存
				try {
					String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":DESC");
					if (StringUtils.isNotBlank(json)) {
						TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
						return tbItemDesc;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
		TbItemDesc itemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
		// 添加缓存
					try {
						jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":DESC", JsonUtils.objectToJson(itemDesc));
						jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":DESC", ITEM_CACHE_EXPIRE);
					} catch (Exception e) {
						e.printStackTrace();
					}
		return itemDesc;
				
	}
}
