package s3.satelite.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import s3.satelite.cart.service.CartService;
import s3.satelite.common.jedis.JedisClient;
import s3.satelite.common.utils.JsonUtils;
import s3.satelite.common.utils.S3Result;
import s3.satelite.mapper.TbItemMapper;
import s3.satelite.pojo.TbItem;

@Service
public class CartServiceImpl implements CartService {
	@Autowired
	private JedisClient jedisClient;
	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;
	@Autowired
	private TbItemMapper tbItemMapper;

	@Override
	public S3Result addCart(long userId, long itemId, int num) {
		//判断商品是否在购物车
		Boolean hexists = jedisClient.hexists(REDIS_CART_PRE + ":" +userId, itemId +"");
		if(hexists){
			String json = jedisClient.hget(REDIS_CART_PRE + ":" +userId, itemId +"");
			TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
			tbItem.setNum(tbItem.getNum()+ num);
			jedisClient.hset(REDIS_CART_PRE + ":" +userId, itemId + "", JsonUtils.objectToJson(tbItem));
			return S3Result.ok();
		}
		//如果不存在，根据商品Id取商品信息
		TbItem item = tbItemMapper.selectByPrimaryKey(itemId);
		item.setNum(num);
		//取一张图片
		String image = item.getImage();
		if(StringUtils.isNotBlank(image)){
			item.setImage(image.split(",")[0]);
		}
		//添加到购物车列表
		jedisClient.hset(REDIS_CART_PRE +":" + userId, itemId +"", JsonUtils.objectToJson(item));
		
		return S3Result.ok();
	}

	@Override
	public S3Result mergeCart(long userId, List<TbItem> itemList) {
		
		//遍历商品列表
		//如果没有，添加新的商品
		for (TbItem tbItem : itemList) {
			addCart(userId, tbItem.getId(), tbItem.getNum());
		}
		
		return S3Result.ok();
	}

	@Override
	public List<TbItem> getCartList(long userId) {
		
		//根据用户Id查询购物车列表
		List<String> jsonList = jedisClient.hvals(REDIS_CART_PRE +":" + userId);
		List<TbItem> itemList = new ArrayList<>();
		
		for (String string : jsonList) {
			TbItem item = JsonUtils.jsonToPojo(string, TbItem.class);
			itemList.add(item);
		}
		return itemList;
	}

	@Override
	public S3Result updateCartNum(long userId, long itemId, int num) {
		//从redis中取商品信息
		String json = jedisClient.hget(REDIS_CART_PRE + ":" +userId, itemId + "");
		//更新商品数量
		TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
		tbItem.setNum(num);
		//写入redis
		jedisClient.hset(REDIS_CART_PRE + ":" +userId, itemId + "", JsonUtils.objectToJson(tbItem));
		return S3Result.ok();
	}

	@Override
	public S3Result deleteCartItem(long userId, long itemId) {
		jedisClient.hdel(REDIS_CART_PRE +":" + userId, itemId + "");
		return S3Result.ok();
	}

	@Override
	public S3Result clearCartItem(long userId) {
		jedisClient.del(REDIS_CART_PRE + ":" +userId);
		return S3Result.ok();
	}

}
