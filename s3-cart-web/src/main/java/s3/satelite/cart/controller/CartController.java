package s3.satelite.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import s3.satelite.cart.service.CartService;
import s3.satelite.common.utils.CookieUtils;
import s3.satelite.common.utils.JsonUtils;
import s3.satelite.common.utils.S3Result;
import s3.satelite.pojo.TbItem;
import s3.satelite.pojo.TbUser;
import s3.satelite.service.TbItemService;

@Controller
public class CartController {
	
	@Autowired
	private CartService cartService;
	@Autowired
	private TbItemService tbItemService;
	@Value("${COOKIE_CART_EXPIRE}")
	private Integer COOKIE_CART_EXPIRE;
	
	@RequestMapping("/cart/add/{itemId}")
	public String addCart(@PathVariable Long itemId, @RequestParam(defaultValue="1")Integer num ,
			HttpServletRequest request, HttpServletResponse response) {
		//判断是否登陆
		TbUser	user = (TbUser) request.getAttribute("user");
		//如果登陆，把购物车信息写入redis
		if(user != null){
			cartService.addCart(user.getId(), itemId, num);
			//返回逻辑视图
			return "cartSuccess";
		}
		//如果未登陆
		//从cookie中取购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		boolean flag = false;
		for (TbItem tbItem : cartList) {
			//如果存在，数量相加
			if(tbItem.getId() == itemId.longValue()) {
				flag = true;
				//找到商品，数量相加
				tbItem.setNum(tbItem.getNum() + num);
				//跳出循环
				break;
			}
		}
		//如果不存在
		if(!flag){
			//根据商品Id查询商品信息，得到一个tbitem对象
			TbItem tbItem = tbItemService.getTbItemById(itemId);
			//设置商品数量
			tbItem.setNum(num);
			//取一张图片
			String image = tbItem.getImage();
			if(StringUtils.isNotBlank(image)){
				tbItem.setImage(image.split(",")[0]);
			}
			//把商品添加到商品列表
			cartList.add(tbItem);
		}
		//写入cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
		
		return "cartSuccess";
		
	}
	
	/**
	 * 从cookie中取购物车列表的处理
	 */
	private List<TbItem> getCartListFromCookie(HttpServletRequest request) {
		String json = CookieUtils.getCookieValue(request, "cart", true);
		//判断json是否为空
		if(StringUtils.isBlank(json)) {
			return new ArrayList<>();
		}
		//把json转换成商品列表
		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}
	
	/**
	 * 展示购物车列表
	 */
	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request, HttpServletResponse response){
		//从cookie中取购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		//判断用户的登陆状态
		TbUser tbuser = (TbUser) request.getAttribute("user");
		//如果是登陆状态
		if(tbuser != null){
			cartService.mergeCart(tbuser.getId(), cartList);
			//从服务端取购物车列表
			cartList = cartService.getCartList(tbuser.getId());
		}
		//把列表传递给页面
		request.setAttribute("cartList", cartList);
		//返回逻辑视图
		return "cart";
	}
	
	/**
	 * 更新购物车商品数量
	 */
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public S3Result updateCartNum(@PathVariable Long itemId, @PathVariable Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		//判断用户是否为登陆状态
		TbUser tbUser = (TbUser) request.getAttribute("user");
		if(tbUser != null ){
			cartService.updateCartNum(tbUser.getId(), itemId, num);
			return S3Result.ok();
		}
		//从cookie中取购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		//遍历商品列表找到对应的商品
		for (TbItem tbItem : cartList) {
			if(tbItem.getId().longValue() == itemId){
				tbItem.setNum(num);
				break;
			}
		}
		//把购物车列表写回cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList),COOKIE_CART_EXPIRE, true);
		//返回成功
		return S3Result.ok();
	}
	
	/**
	 * 删除购物车商品
	 */
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request,
			HttpServletResponse response) {
		//判断用户是否为登陆状态
		TbUser tbUser = (TbUser) request.getAttribute("user");
		if(tbUser != null){
			cartService.deleteCartItem(tbUser.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		//从cookie中取购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		for (TbItem tbItem : cartList) {
			if(tbItem.getId().longValue() == itemId){
				cartList.remove(tbItem);
				break;
			}
		}
		//把购物车列表写入cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
		return "redirect:/cart/cart.html";
	}
	
}
