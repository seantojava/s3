package s3.satelite.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import s3.satelite.item.pojo.Item;
import s3.satelite.pojo.TbItem;
import s3.satelite.pojo.TbItemDesc;
import s3.satelite.service.TbItemService;

@Controller
public class IndexController {
	
	@Autowired
	private TbItemService tbItemService;

	@RequestMapping("/item/{itemId}")
	public String showIndex(@PathVariable Long itemId,Model model){
		TbItem tbItem = tbItemService.getTbItemById(itemId);
		Item item = new Item(tbItem);
		
		TbItemDesc itemDesc = tbItemService.getTbItemDescById(itemId);
		
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);
		
		
		return "item";
	}
}
