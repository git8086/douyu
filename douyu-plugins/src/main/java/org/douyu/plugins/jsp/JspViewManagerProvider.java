package org.douyu.plugins.jsp;

import douyu.mvc.Context;
import douyu.mvc.ViewManager;
import douyu.mvc.ViewManagerProvider;

/**
 * 
 * @author ZHH
 *
 */
public class JspViewManagerProvider implements ViewManagerProvider {
	@Override
	public ViewManager getViewManager(Context context) {
		return new JspViewManager(context);
	}
}
