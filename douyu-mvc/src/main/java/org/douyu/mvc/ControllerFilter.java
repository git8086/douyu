package org.douyu.mvc;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.douyu.core.ClassResource;
import org.douyu.core.Config;
import org.douyu.core.JavacException;
import org.douyu.core.ResourceLoader;

/**
 * 
 * @author ZHH
 *
 */
public class ControllerFilter implements Filter {

	private ResourceLoader.Holder holder;
	private Config config;
	private ServletContext servletContext;

	private static final String viewManagerProviderConfig = "org.douyu.plugins.jsp.JspViewManagerProvider=jsp;"
			+ "org.douyu.plugins.velocity.VelocityViewManagerProvider=vm;"
			+ "org.douyu.plugins.freemarker.FreeMarkerViewManagerProvider=ftl;";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		config = new Config();
		servletContext = filterConfig.getServletContext();
		config.appName = servletContext.getContextPath();
		config.javacEncoding = filterConfig.getInitParameter("javacEncoding");
		config.srcDir = filterConfig.getInitParameter("srcDir");
		config.classesDir = filterConfig.getInitParameter("classesDir");

		if (config.srcDir == null)
			config.srcDir = "src";

		if (config.classesDir == null)
			config.classesDir = "classes";

		if (!new File(config.srcDir).isAbsolute())
			config.srcDir = filterConfig.getServletContext().getRealPath("WEB-INF/" + config.srcDir);

		File f = new File(config.srcDir);
		if (!f.exists())
			f.mkdirs();

		if (!new File(config.classesDir).isAbsolute())
			config.classesDir = filterConfig.getServletContext().getRealPath("WEB-INF/" + config.classesDir);

		f = new File(config.classesDir);
		if (!f.exists())
			f.mkdirs();

		if ("false".equalsIgnoreCase(filterConfig.getInitParameter("isDevMode")))
			config.isDevMode = false;

		String vmpConfig = filterConfig.getInitParameter("viewManagerProviderConfig");
		if (vmpConfig == null)
			vmpConfig = viewManagerProviderConfig;
		config.setViewManagerProviderConfig(vmpConfig);

		try {
			config.addClassPath(config.srcDir);
			config.addClassPath(config.classesDir);
		} catch (Exception e) {
			throw new ServletException(e);
		}
		holder = ResourceLoader.newHolder(config, getClass().getClassLoader());
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest hsr = (HttpServletRequest) request;

		String path = null;

		//if (request.getAttribute("javax.servlet.async.servlet_path") != null)
		//	path = request.getAttribute("javax.servlet.async.servlet_path").toString();
		//异步也相关
		if (request.getAttribute("javax.servlet.include.request_uri") != null)
			path = request.getAttribute("javax.servlet.include.request_uri").toString();

		if (path == null)
			path = hsr.getRequestURI(); //path = hsr.getServletPath();

		String cpath = hsr.getContextPath();
		if (cpath.length() > 1)
			path = path.substring(cpath.length());

		//以'/'结尾说明是一个目录
		if (path.endsWith("/")) {
			chain.doFilter(request, response);
			return;
		}

		//path格式: /packageName/controllerClassName.actionName
		if (path.startsWith("/"))
			path = path.substring(1);

		String controllerClassName = path;
		String actionName = null;
		int dotPos = path.indexOf('.');//谷歌浏览器(Chrome)不支持'|'字符，所以用'."分隔类名和action名
		if (dotPos >= 0) {
			actionName = path.substring(dotPos + 1).trim();
			controllerClassName = path.substring(0, dotPos);
		}

		controllerClassName = controllerClassName.replace('/', '.');

		StringWriter sw = new StringWriter();
		PrintWriter javacOut = new PrintWriter(sw);

		ClassResource cr = null;
		try {
			cr = holder.get().loadContextClassResource(controllerClassName, javacOut);
		} catch (JavacException e) {
			printJavacMessage(sw.toString(), response, e);
			return;
		}

		if (cr != null) {
			AbstractContext ac = null;
			try {
				ac = (AbstractContext) cr.loadedClass.newInstance();
				ac.init(config, controllerClassName, servletContext, hsr, (HttpServletResponse) response);
				ac.executeAction(actionName);

				printJavacMessage(sw.toString(), response, null);
			} catch (Exception e) {
				throw new ServletException(e);
			} finally {
				if (ac != null)
					ac.free();
			}
		} else {
			chain.doFilter(request, response);
		}
	}

	private static void printJavacMessage(String javacMessage, ServletResponse response, JavacException e) throws IOException {
		PrintWriter out = response.getWriter();
		if (javacMessage.length() > 0) {
			out.println();
			out.println("javac message:");
			out.println("-----------------------------------");
			out.println(javacMessage);
		}

		if (e != null) {
			out.println();
			out.println("javac error:");
			out.println("-----------------------------------");
			e.printStackTrace(out);
		}
	}

	@Override
	public void destroy() {
		config = null;
		servletContext = null;
		holder.free();
		holder = null;
	}

}
