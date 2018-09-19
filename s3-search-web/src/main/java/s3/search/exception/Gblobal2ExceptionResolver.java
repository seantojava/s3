package s3.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class Gblobal2ExceptionResolver implements HandlerExceptionResolver{

	private static final Logger logger = LoggerFactory.getLogger(Gblobal2ExceptionResolver.class);
	
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		
		logger.debug("错误");
		logger.info("123");
		logger.error("1221", ex);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("error/exception");
		
		return modelAndView;
	}

}
