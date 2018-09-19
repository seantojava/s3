package s3.satelite.order.service;

import s3.satelite.common.utils.S3Result;
import s3.satelite.order.pojo.OrderInfo;

public interface OrderService {

	S3Result createOrder(OrderInfo orderInfo);
}
