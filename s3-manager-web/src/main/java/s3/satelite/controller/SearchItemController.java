package s3.satelite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import s3.satelite.common.utils.S3Result;
import s3.satelite.search.service.SearchItemService;

@Controller
public class SearchItemController {

	@Autowired
	private SearchItemService searchItemService;
	
	@RequestMapping("/index/item/import")
	@ResponseBody
	public S3Result ImportAll(){
		S3Result itemList = searchItemService.SearchItemList();
		return itemList;
		
	}
}
