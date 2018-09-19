package s3.satelite.service;

import s3.satelite.common.pojo.EasyUIDataGridResult;
import s3.satelite.common.utils.S3Result;
import s3.satelite.pojo.TbItem;
import s3.satelite.pojo.TbItemDesc;

public interface TbItemService {

	TbItem getTbItemById(long itemId);
	EasyUIDataGridResult getItemList(int page , int rows);
	S3Result addTbItem(TbItem item , String desc);
	S3Result deleteTbitem(long itemId);
	TbItemDesc getTbItemDescById(long itemId);
	
}
