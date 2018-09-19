package s3.satelite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import s3.satelite.common.pojo.EasyUIDataGridResult;
import s3.satelite.common.utils.S3Result;
import s3.satelite.pojo.TbItem;
import s3.satelite.service.TbItemService;

@Controller
public class TbItemController {

	
	@Autowired
	private TbItemService tbItemService;
	
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	
	public TbItem getTbItemById(@PathVariable Long itemId) {
		TbItem item = tbItemService.getTbItemById(itemId);
		return item;
	}
	
	@RequestMapping("/item/list")
	@ResponseBody
	
	public EasyUIDataGridResult getItemList(Integer page ,Integer rows){
		EasyUIDataGridResult list = tbItemService.getItemList(page, rows);
		return list;
	}
	
	@RequestMapping(value="/item/save",method=RequestMethod.POST)
	@ResponseBody
	
	public S3Result addItem(TbItem item , String desc) {
		S3Result result = tbItemService.addTbItem(item, desc);
		return result;
	}
	
	@RequestMapping("/rest/item/delete" )
	@ResponseBody
	
	public S3Result deleteTbitem(@RequestParam("ids") long[] ids){
		S3Result result =null;
		for (long id : ids) {
			result = tbItemService.deleteTbitem(id);
		}
		return result;
		
	}
	
	
	
	
	
}
