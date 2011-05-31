package org.douyu.plugins.freemarker;

import douyu.mvc.Context;
import douyu.mvc.ViewManager;
import douyu.mvc.ViewManagerProvider;

/**
 * 
 * @author ZHH
 *
 */
public class FreeMarkerViewManagerProvider implements ViewManagerProvider {
	@Override
	public ViewManager getViewManager(Context context) {
		return new FreeMarkerViewManager(context);
	}
}