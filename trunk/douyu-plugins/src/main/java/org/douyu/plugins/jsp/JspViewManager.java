package org.douyu.plugins.jsp;

import douyu.mvc.Context;
import douyu.mvc.ViewException;
import douyu.mvc.ViewManager;

/**
 * 
 * @author ZHH
 *
 */
public class JspViewManager implements ViewManager {
	private Context douyuContext;

	JspViewManager(Context douyuContext) {
		this.douyuContext = douyuContext;
	}

	private String defaultViewFileName() {
		//不能使用File.separatorChar，必需使用uri规范中的'/'，否则jsp会找不到文件
		StringBuilder viewFileName = new StringBuilder(50);
		viewFileName.append('/'); //相对于应用上下文的绝对路径
		viewFileName.append(douyuContext.getControllerClassName().replace('.', '/'));
		viewFileName.append(".");
		viewFileName.append(douyuContext.getActionName());
		viewFileName.append(".jsp");

		return viewFileName.toString();
	}

	@Override
	public void out() {
		out(defaultViewFileName());
	}

	@Override
	public void out(String viewFileName) {
		try {
			douyuContext.getHttpServletRequest().getRequestDispatcher(viewFileName).include(douyuContext.getHttpServletRequest(),
					douyuContext.getHttpServletResponse());
		} catch (Throwable t) {
			throw new ViewException(t);
		}
	}

	@Override
	public void put(String key, Object value) {
		//TODO 会覆盖己存在的值
		douyuContext.getHttpServletRequest().setAttribute(key, value);
	}
}
