package douyu.mvc;

import java.lang.annotation.*;

import douyu.http.HttpMethod;
import static java.lang.annotation.RetentionPolicy.*;
import static java.lang.annotation.ElementType.*;

@Retention(RUNTIME)
@Target(TYPE)
public @interface Controller {
	String defaultAction() default "index";

	/**
	 * Controller所能接受的HTTT请求方法类型，会被Action中的值覆盖
	 */
	HttpMethod[] httpMethods() default { HttpMethod.GET, HttpMethod.POST };
}
