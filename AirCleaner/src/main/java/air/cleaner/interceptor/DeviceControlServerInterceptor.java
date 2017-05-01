package air.cleaner.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import air.cleaner.cache.SessionCacheManager;

public class DeviceControlServerInterceptor implements HandlerInterceptor{
	
	public static Logger LOG = LoggerFactory.getLogger(DeviceControlServerInterceptor.class);
	@Autowired
	private SessionCacheManager sessionCacheManager;
	public void setSessionCacheManager(SessionCacheManager sessionCacheManager) {
		this.sessionCacheManager = sessionCacheManager;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		LOG.info("request path :"+request.getRequestURI() + " query :" + request.getQueryString());
		if(!request.getParameterMap().containsKey("token")){
			return true;
		}else{
			long token = Long.parseLong(request.getParameter("token"));
			if (sessionCacheManager.getSession(token) != null) {
				return true;
			}else{
				String server = "localhost:8080";
				response.sendRedirect("http://"+server+request.getRequestURI()+"?"+request.getQueryString());
				return false;
			}
		}
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
	
}
