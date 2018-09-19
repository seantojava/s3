package s3.satelite.item.listener;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;
import s3.satelite.item.pojo.Item;
import s3.satelite.pojo.TbItem;
import s3.satelite.pojo.TbItemDesc;
import s3.satelite.service.TbItemService;

public class HtmlGenListener implements MessageListener{

	@Autowired
	private TbItemService tbItemService;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Value("${HTML_GEN_PATH}")
	private String HTML_GEN_PATH;
	
	
	@Override
	public void onMessage(Message message) {
		
		TextMessage textMessage = (TextMessage) message;
		String text;
		try {
			text = textMessage.getText();
			Long itemId = new Long(text);
			
			//等待事务提交
			Thread.sleep(1000);
			
			TbItem tbItem = tbItemService.getTbItemById(itemId);
			Item item = new Item(tbItem);
			TbItemDesc itemDesc = tbItemService.getTbItemDescById(itemId);
			
			Map data = new HashMap<>();
			data.put("item", item);
			data.put("itemDesc", itemDesc);
			
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			Writer out = new FileWriter(HTML_GEN_PATH + itemId + ".html");
			template.process(data, out);
			
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
