package douyu.mvc;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * 执行控制器的Action时，会为这个控制器生成一个ControllerManager，这个ControllerManager用来管理一次请求过程中用到的相关上下文信息。
 * 
 * 此类不是线程安全的，存活期与HttpServletRequest相同。
 * 
 * @author ZHH
 * @since 0.6.1
 *
 */
public interface ControllerManager {
	public ServletContext getServletContext();

	public HttpServletRequest getHttpServletRequest();

	public HttpServletResponse getHttpServletResponse();

	public String getControllerClassName();

	public String getActionName();

	public String getApplicationBase();

	public void executeAction(String actionName) throws ControllerException;
}