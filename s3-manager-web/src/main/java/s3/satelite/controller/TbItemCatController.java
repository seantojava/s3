package s3.satelite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import s3.satelite.common.pojo.EasyUITreeNode;
import s3.satelite.service.TbItemCatService;

@Controller
public class TbItemCatController {
	

	
	@Autowired
	private TbItemCatService tbItemCatService;
	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<EasyUITreeNode> getTbItemCatList(
			@RequestParam(name="id",defaultValue="0")Long parentId){
		List<EasyUITreeNode> list = tbItemCatService.getTbItemCatList(parentId);
		return list;
		
	}
}
