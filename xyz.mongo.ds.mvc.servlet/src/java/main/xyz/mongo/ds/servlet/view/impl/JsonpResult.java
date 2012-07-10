package xyz.mongo.ds.servlet.view.impl;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import xyz.mongo.ds.servlet.utils.TransUtil;
import xyz.mongo.ds.servlet.view.IResult;

public class JsonpResult implements IResult{
    
	/**
	 * 生成jsoncallback({name:'1',path:'ssss'}),这样的js，而jsoncallback由path确定
	 * jsonp比json快，而且可以解决跨域问题，缺陷是只能get
	 */
	@Override
	public void process(HttpServletRequest request, HttpServletResponse resp,
			String path, Object objs)throws IOException,ServletException {
		//那个Object怎么放入request里面呢
		int index=path.lastIndexOf(".");
		path=path.substring(0,index)+"_jsonp";
		resp.setContentType(IResult.CON_TYPE_JS);
		resp.setCharacterEncoding(IResult.CON_ENCODING_UTF_8);
		StringBuilder sb=new StringBuilder(path).append("(");//path是jsoncallback的名字
		Object json=TransUtil.obj2Json(objs, IResult.ROOT_NAME);
		sb.append(json.toString());
		sb.append(")");
		Writer writer = new OutputStreamWriter(resp.getOutputStream(),IResult.CON_ENCODING_UTF_8);
		writer.write(sb.toString());
		writer.flush();
	}

}
