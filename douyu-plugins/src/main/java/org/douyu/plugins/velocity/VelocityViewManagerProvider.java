package org.douyu.plugins.velocity;

import douyu.mvc.Context;
import douyu.mvc.ViewManager;
import douyu.mvc.ViewManagerProvider;

/**
 * 
 * @author ZHH
 *
 */
public class VelocityViewManagerProvider implements ViewManagerProvider {
	@Override
	public ViewManager getViewManager(Context context) {
		return new VelocityViewManager(context);
	}
}