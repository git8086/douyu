import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import douyu.mvc.Context;
import douyu.mvc.Controller;

@Controller
public class FileUpload {
	public void index(Context c) {
		c.out("/FileUpload.vm");
	}

	//Tomcat7中要在<Context>元素中把属性allowCasualMultipartParsing设为true才能例用文件上传
	public void upload(Part[] uploadedFiles, Part file1, String description, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain");
		response.setCharacterEncoding("gb2312");
		PrintWriter out = response.getWriter();

		out.println("说明: " + description);
		out.println();

		if (uploadedFiles != null) {
			for (Part f : uploadedFiles) {
				//注意这里，file1与uploadedFiles中的某一个元素指向同一个对象
				if (file1 == f) {
					out.println("这是文件1:");
					out.println();
				}

				out.println("大小  : " + f.getSize() + " 字节");
				out.println("类型  : " + f.getContentType());
				out.println("文件名: " + f.getName());

				out.println();
			}
		}
	}
}
