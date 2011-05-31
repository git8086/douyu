package douyu.mvc;

/**
 * 
 * 执行控制器的Action时，会为这个控制器生成一个Context，这个Context用来管理一次请求过程中用到的相关上下文信息。
 * 
 * 此类不是线程安全的，存活期与HttpServletRequest相同。
 * 
 * @author ZHH
 * @since 0.6.1
 *
 */
public interface Context extends ModelManager, ViewManager, ControllerManager {
}