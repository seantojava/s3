package s3.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;


public class GblobalExceptionResolver  implements HandlerExceptionResolver{

	private static final Logger logger = LoggerFactory.getLogger(GblobalExceptionResolver.class);
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		
		ex.printStackTrace();
		//写日志
				logger.debug("测试输出的日志。。。。。。。");
				logger.info("系统发生异常了。。。。。。。");
				logger.error("系统发生异常", ex);
		
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("error/exception");
			
				
		return modelAndView;
	}

}
