package org.douyu.plugins.velocity;

import java.io.File;
import java.io.Writer;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.Template;

import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import douyu.mvc.Context;
import douyu.mvc.ViewException;
import douyu.mvc.ViewManager;

/**
 * 
 * @author ZHH
 *
 */
public class VelocityViewManager implements ViewManager {

	private static final String defaultContentType = "text/html; charset=UTF-8";
	private static final String defaultEncoding = "UTF-8";

	private Context douyuContext;
	private VelocityContext velocityContext = new VelocityContext();

	VelocityViewManager(Context douyuContext) {
		this.douyuContext = douyuContext;

		//如果不加这一行，Velocity会出错:
		//java.lang.UnsupportedOperationException: Could not retrieve ServletContext from application attributes
		//        org.apache.velocity.runtime.log.ServletLogChute.init(ServletLogChute.java:73)
		Velocity.setApplicationAttribute("javax.servlet.ServletContext", douyuContext.getServletContext());

		Velocity.setProperty(org.apache.velocity.runtime.log.ServletLogChute.RUNTIME_LOG_LEVEL_KEY, "info");

		Velocity.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, douyuContext.getApplicationBase());

		Velocity.setProperty(Velocity.FILE_RESOURCE_LOADER_CACHE, true);

		if (douyuContext.getHttpServletResponse().getContentType() == null)
			douyuContext.getHttpServletResponse().setContentType(defaultContentType);

		try {
			Velocity.init();
		} catch (Exception e) {
			throw new ViewException(e);
		}
	}

	private String defaultViewFileName() {
		StringBuilder viewFileName = new StringBuilder(50);
		viewFileName.append(douyuContext.getControllerClassName().replace('.', File.separatorChar));
		viewFileName.append(".");
		viewFileName.append(douyuContext.getActionName());
		viewFileName.append(".vm");

		return viewFileName.toString();
	}

	@Override
	public void out() {
		out(defaultViewFileName());
	}

	@Override
	public void out(String viewFileName) {
		try {
			Template template = Velocity.getTemplate(viewFileName, defaultEncoding);

			if (template != null) {
				Writer writer = douyuContext.getHttpServletResponse().getWriter();
				template.merge(velocityContext, writer);

				//writer.flush();
				//writer.close();
			}
		} catch (ResourceNotFoundException rnfe) {
			throw new ViewException("Velocity : error : cannot find view file " + viewFileName, rnfe);
		} catch (ParseErrorException pee) {
			throw new ViewException("Velocity : error : Syntax error in view file " + viewFileName, pee);
		} catch (Throwable t) {
			throw new ViewException(t);
		}
	}

	@Override
	public void put(String key, Object value) {
		velocityContext.put(key, value);
	}
}