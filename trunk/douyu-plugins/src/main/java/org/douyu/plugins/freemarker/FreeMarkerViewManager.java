package org.douyu.plugins.freemarker;

import java.io.File;
import java.io.Writer;
import java.util.Map;
import java.util.HashMap;

import douyu.mvc.Context;
import douyu.mvc.ViewException;
import douyu.mvc.ViewManager;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * 
 * @author ZHH
 *
 */
public class FreeMarkerViewManager implements ViewManager {

	private static final String defaultContentType = "text/html; charset=UTF-8";
	private static final String defaultEncoding = "UTF-8";

	private Context douyuContext;

	private Configuration config = new Configuration();
	private Map<String, Object> root = new HashMap<String, Object>();

	FreeMarkerViewManager(Context douyuContext) {
		this.douyuContext = douyuContext;
		if (douyuContext.getHttpServletResponse().getContentType() == null)
			douyuContext.getHttpServletResponse().setContentType(defaultContentType);

		try {
			config.setDirectoryForTemplateLoading(new File(douyuContext.getApplicationBase()));
			config.setObjectWrapper(new DefaultObjectWrapper());
		} catch (Throwable t) {
			throw new ViewException(t);
		}
	}

	private String defaultViewFileName() {
		StringBuilder viewFileName = new StringBuilder(50);
		viewFileName.append(douyuContext.getControllerClassName().replace('.', File.separatorChar));
		viewFileName.append(".");
		viewFileName.append(douyuContext.getActionName());
		viewFileName.append(".ftl");

		return viewFileName.toString();
	}

	@Override
	public void out() {
		out(defaultViewFileName());
	}

	@Override
	public void out(String viewFileName) {
		try {
			Template template = config.getTemplate(viewFileName, defaultEncoding);

			if (template != null) {
				Writer writer = douyuContext.getHttpServletResponse().getWriter();

				//TODO
				//template.process内部会自动调用writer.flush，
				//如果在输出ftl文件后又接着输出jsp，那么jsp会出异常:
				//java.lang.IllegalStateException: Cannot create a session after the response has been committed
				//因为调用writer.flush会导致响应头会被提交
				template.process(root, writer);

				//writer.flush();
				//writer.close();
			}
		} catch (Throwable t) {
			throw new ViewException(t);
		}
	}

	@Override
	public void put(String key, Object value) {
		root.put(key, value);
	}
}