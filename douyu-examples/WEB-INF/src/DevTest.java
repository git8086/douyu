import java.io.PrintWriter;
import java.io.Writer;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import douyu.mvc.Context;
import douyu.mvc.Controller;
import douyu.mvc.ControllerManager;
import douyu.mvc.ModelManager;
import douyu.mvc.ViewManager;
import models.MyModel;

@Controller
public class DevTest {
	//static {}
	//{ }
	public DevTest() {
		//throw new Error();
	}

	//http://127.0.0.1:8080/douyu/DevTest
	public void index(PrintWriter out) {
		out.println("invoke defaultAction: 'index' at " + new java.util.Date());
	}

	//http://127.0.0.1:8080/douyu/DevTest.haha?name=haha&age=1000
	public void haha(Context c, String name, int age) {

		c.out("/jsp/ViewTest.jsp");
		c.out(); //DevTest.haha.jsp

		c.out("/ViewTest.vm");
		c.out("/ViewTest.ftl");
	}

	//////////////////以下是所有可注入的方法参数类型/////////////////
	public void method0() {
	}

	public void method1(Context context, ModelManager m, ViewManager v, ControllerManager c) {
	}

	public void method2(HttpServletRequest p1, ServletRequest p2, HttpServletResponse p3, ServletResponse p4, ServletContext p5) {
	}

	public void method3(PrintWriter p1, Writer p2) {
	}

	public void method4(int i, long l, float f, double d, boolean bool, byte b, short s, char c) {

	}

	public void method5(Integer i, Long l, Float f, Double d, Boolean bool, Byte b, Short s, Character c) {

	}

	public void method6(String[] strs) {

	}

	//servlet3.0才支持Part
	//如果编译出错，注销掉这个方法
	/*
	public void method7(javax.servlet.http.Part part, javax.servlet.http.Part[] parts, PrintWriter out) {
		out.println("part=" + part);

		if (parts != null) {
			out.println("parts.length=" + parts.length);
		} else {
			out.println("parts=null");
		}
	}
	*/

	//http://127.0.0.1:8080/douyu/DevTest.method8?model.f1=1&model.f2=2&model.subModel.f1=3&model.subModel.f2=4
	public void method8(Object obj, MyModel model, PrintWriter out) {
		out.println("invoke 'method8' at " + new java.util.Date());

		out.println();
		out.println("obj=" + obj); //总是为null，java.lang.Object不是可注入的类型

		out.println();
		out.println("model=" + model);
	}

	//////////////////package、private、protected以及所有static方法都是不能通过URI直接访问的/////////////////
	void package_method() {
	}

	@SuppressWarnings("unused")
	private void private_method() {
	}

	protected void protected_method() {
	}

	public static void static_method() {
	}
}
