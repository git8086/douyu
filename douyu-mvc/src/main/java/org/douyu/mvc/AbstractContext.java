package org.douyu.mvc;

import java.util.Map;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import douyu.mvc.Context;
import douyu.mvc.ControllerException;
import douyu.mvc.ViewException;
import douyu.mvc.ViewManager;
import douyu.mvc.ViewManagerProvider;

import org.douyu.core.Config;

/**
 * 
 * 执行控制器的Action时，会为这个控制器生成一个Context，这个Context用来管理一次请求过程中用到的相关上下文信息。
 * 
 * 此类不是线程安全的，存活期与HttpServletRequest相同。
 * 
 * @author ZHH
 *
 */
public abstract class AbstractContext implements Context {
	private Config config;
	private String controllerClassName;

	//编译器在编译带有@Controller的类时，会为它自动生成一个AbstractContext的子类，
	//这4个字段会在自动生成的子类中直接访问，所以用protected.
	//如果修改了这4个字段的名称要记得修改com.sun.tools.javac.processing.ControllerProcessor类
	protected ServletContext servletContext;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected String actionName;

	private Map<String, Object> viewArgs = new HashMap<String, Object>();

	public void init(Config config, String controllerClassName, ServletContext servletContext, HttpServletRequest request,
			HttpServletResponse response) {
		this.config = config;
		this.controllerClassName = controllerClassName;
		this.servletContext = servletContext;
		this.request = request;
		this.response = response;
	}

	public void free() {
		config = null;
		controllerClassName = null;
		servletContext = null;
		request = null;
		response = null;
		actionName = null;
		viewArgs.clear();
		viewArgs = null;
	}

	protected void checkHttpMethods(String... methods) {
		String method = request.getMethod();
		for (String m : methods) {
			if (m.equals(method)) {
				return;
			}
		}

		throw new ControllerException("501 Not Implemented method: " + method);
	}

	// ========================================================
	// 实现douyu.mvc.ControllerManager
	// ========================================================

	@Override
	public ServletContext getServletContext() {
		return servletContext;
	}

	@Override
	public HttpServletRequest getHttpServletRequest() {
		return request;
	}

	@Override
	public HttpServletResponse getHttpServletResponse() {
		return response;
	}

	@Override
	public String getControllerClassName() {
		return controllerClassName;
	}

	@Override
	public String getActionName() {
		return actionName;
	}

	@Override
	public String getApplicationBase() {
		if (config.srcDir == null)
			return config.classesDir;

		if (config.classesDir == null)
			return config.srcDir;

		if (config.isDevMode)
			return config.srcDir;
		else
			return config.classesDir;
	}

	@Override
	public void executeAction(String actionName) throws ControllerException {
		this.actionName = actionName;
		try {
			executeAction();
		} catch (Exception e) {
			throw new ControllerException("failed to execute action: " + actionName, e);
		}
	}

	protected abstract void executeAction() throws Exception;

	// ========================================================
	// 实现douyu.mvc.ViewManager
	// ========================================================

	@Override
	public void put(String key, Object value) {
		viewArgs.put(key, value);
	}

	@Override
	public void out() {
		ViewManagerProvider def = config.getDefaultViewManagerProvider();
		if (def != null) {
			try {
				outView(def, null);
				return;
			} catch (Exception e) {
			}
		}
		for (ViewManagerProvider vmp : config.getViewManagerProviders()) {
			if (vmp != def) {
				try {
					outView(vmp, null);
					return;
				} catch (Exception e) {
				}
			}
		}

		throw new ViewException("No ViewManager for 'ViewManager.out()', controller='" + controllerClassName + "', attion='"
				+ actionName + "'.");
	}

	private void outView(ViewManagerProvider vmp, String viewFileName) {
		ViewManager vm = vmp.getViewManager(this);
		if (vm == null)
			throw new ViewException("No ViewManager for view file: " + viewFileName);

		for (Map.Entry<String, Object> e : viewArgs.entrySet()) {
			vm.put(e.getKey(), e.getValue());
		}

		if (viewFileName == null)
			vm.out();
		else
			vm.out(viewFileName);
	}

	@Override
	public void out(String viewFileName) {
		String extension = null;
		int dotPos = viewFileName.lastIndexOf('.');
		if (dotPos >= 0) {
			extension = viewFileName.substring(dotPos + 1).trim();
		}

		ViewManagerProvider vmp;
		if (extension == null) {
			vmp = config.getDefaultViewManagerProvider();
		} else {
			vmp = config.getViewManagerProvider(extension);
		}

		if (vmp == null) {
			throw new ViewException("No ViewManagerProvider for view file: " + viewFileName);
		}

		outView(vmp, viewFileName);
	}

}