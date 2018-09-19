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
import s3.satelite.content.service.ContentService;
import s3.satelite.pojo.TbContent;

@Controller
public class ContentController {

	
	@Autowired
	private ContentService contentService;
	@RequestMapping(value="/content/save",method=RequestMethod.POST)
	@ResponseBody
	public S3Result addContent(TbContent content) {
		S3Result result = contentService.addContent(content);
		return result;
	}
	
	//展示广告页后台列表
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUIDataGridResult getTbContentCat(@RequestParam Long categoryId,int page,int rows){
		EasyUIDataGridResult result = contentService.getTbContentByCatId(categoryId, page, rows);
		return result;
		
	}
	
	
}
