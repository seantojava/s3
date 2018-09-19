package s3.satelite.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import s3.satelite.common.pojo.SearchItem;
import s3.satelite.common.utils.S3Result;
import s3.satelite.search.mapper.ItemMapper;
import s3.satelite.search.service.SearchItemService;

@Service
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrServer solrServer;
	@Override
	public S3Result SearchItemList() {
		
		List<SearchItem> itemList = itemMapper.getItemList();
		
		try {
			
			for (SearchItem searchItem : itemList) {
				SolrInputDocument document = new SolrInputDocument();
				document.addField("id", searchItem.getId());
				document.addField("item_title", searchItem.getTitle());
				document.addField("item_sell_point", searchItem.getSell_point());
				document.addField("item_price", searchItem.getPrice());
				document.addField("item_image", searchItem.getImage());
				document.addField("item_category_name", searchItem.getCategory_name());
				
				solrServer.add(document);
			}
			
			solrServer.commit();
			return S3Result.ok();
			
		} catch (Exception e) {
			e.printStackTrace();
			return S3Result.build(500, "数据导入时发生异常");
		}
		
		
		
		
		
	}

}
