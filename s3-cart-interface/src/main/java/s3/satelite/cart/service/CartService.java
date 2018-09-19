package s3.satelite.cart.service;

import java.util.List;

import s3.satelite.common.utils.S3Result;
import s3.satelite.pojo.TbItem;

public interface CartService {
	S3Result addCart(long userId, long itemId , int num);
	S3Result mergeCart(long userId, List<TbItem> itemList);
	List<TbItem> getCartList(long userId);
	S3Result updateCartNum(long userId, long itemId, int num);
	S3Result deleteCartItem(long userId, long itemId);
	S3Result clearCartItem(long userId);
	

}
