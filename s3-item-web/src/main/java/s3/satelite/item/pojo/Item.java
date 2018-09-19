package s3.satelite.item.pojo;

import s3.satelite.pojo.TbItem;

public class Item extends TbItem {
	
	
	public Item(TbItem tbItem) {
		this.setId(tbItem.getId());
		this.setTitle(tbItem.getTitle());
		this.setSellPoint(tbItem.getSellPoint());
		this.setPrice(tbItem.getPrice());
		this.setNum(tbItem.getNum());
		this.setBarcode(tbItem.getBarcode());
		this.setImage(tbItem.getImage());
		this.setCid(tbItem.getCid());
		this.setStatus(tbItem.getStatus());
		this.setCreated(tbItem.getCreated());
		this.setUpdated(tbItem.getUpdated());
	}
	

	
	public String[] getImages(){
		String images = this.getImage();
		
		if(images != null && !"".equals(images)){
			String[] strings = images.split(",");
			return strings;
		}
		return null ;
		
	}
}
