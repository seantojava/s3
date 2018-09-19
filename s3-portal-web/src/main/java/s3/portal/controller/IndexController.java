package s3.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import s3.satelite.content.service.ContentService;
import s3.satelite.pojo.TbContent;

@Controller
public class IndexController {
	
	@Value("${IMAGE_LUNBO_SERVER}")
	private Long IMAGE_LUNBO_SERVER;
	
	@Autowired
	private ContentService contentService;

	
	@RequestMapping("/index")
	public String showIndex(Model model) {
		List<TbContent> ad1List = contentService.getTbContentByCid(IMAGE_LUNBO_SERVER);
		model.addAttribute("ad1List", ad1List);
		
		return "index";
	}
}
