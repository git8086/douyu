package douyu.mvc;

import java.lang.annotation.*;

import douyu.http.HttpMethod;
import static java.lang.annotation.RetentionPolicy.*;
import static java.lang.annotation.ElementType.*;

@Retention(RUNTIME)
@Target(METHOD)
public @interface Action {
	/**
	 * Action所能接受的HTTT请求方法类型
	 */
	HttpMethod[] httpMethods() default { HttpMethod.GET, HttpMethod.POST };
}
