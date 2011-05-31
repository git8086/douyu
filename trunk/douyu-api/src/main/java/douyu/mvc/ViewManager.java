package douyu.mvc;

/**
 * 
 * 输出视图模板文件，存放模板文件需要用到的参数。
 * 
 * @author ZHH
 * @since 0.6.1
 *
 */
public interface ViewManager {
	/**
	 * 输出默认视图，默认视图的位置取决于ViewManagerProvider，通常会根据控制器类名和Action名来决定
	 */
	public void out();

	public void out(String viewFileName);

	public void put(String key, Object value);
}