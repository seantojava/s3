package s3.satelite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import s3.satelite.common.pojo.EasyUITreeNode;
import s3.satelite.common.utils.S3Result;
import s3.satelite.content.service.ContentCategoryService;

@Controller
public class ContentCatController {

	@Autowired
	private ContentCategoryService contentCategoryService;
	
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getTreeNodeList(
			@RequestParam(name="id" , defaultValue="0")Long parentId){
		List<EasyUITreeNode> list = contentCategoryService.getTreeNodeList(parentId);
		return list;
	}
	
	
	@RequestMapping(value="/content/category/create", method=RequestMethod.POST)
	@ResponseBody
	public S3Result createContentCategory(Long parentId , String name) {
		S3Result s3Result = contentCategoryService.addContentCategory(parentId, name);
		return s3Result;
	}
	
	@RequestMapping(value="/content/category/delete/", method=RequestMethod.POST)
	@ResponseBody
	public S3Result deleteContentCategory(@RequestParam(name="id")Long id){
		S3Result result = contentCategoryService.deleteContentCategory(id);
		return result;
	}
	
	
	
	
}
