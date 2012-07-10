package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;

public final class index_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n");
      out.write("<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n");
      out.write("<head>\r\n");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\r\n");
      out.write("<title>Mongo Ds Mvc 例子</title>\r\n");
      out.write("</head>\r\n");
      out.write("\r\n");
      out.write("<frameset id=\"frameset1\" border=\"0\" frameSpacing=\"0\" rows=\"30,*,50\" frameBorder=\"no\" cols=\"*\">\r\n");
      out.write("  <frame name=\"header\" src=\"page/header.jsp\" noResize scrolling=\"no\" frameSpacing=\"0\" frameBorder=\"no\">\r\n");
      out.write("  <frameset id=\"frameset2\" border=\"0\" frameSpacing=\"0\" frameBorder=\"no\" cols=\"180,*\">\r\n");
      out.write("   <frame name=\"left\" src=\"page/left.jsp\" scrolling=\"yes\">\r\n");
      out.write("   <frame name=\"right\" src=\"page/right.jsp\">\r\n");
      out.write("  </frameset>\r\n");
      out.write("<frame name=\"bottomFrame\" src=\"page/footer.jsp\" noResize scrolling=\"no\" frameSpacing=\"0\" frameBorder=\"NO\">\r\n");
      out.write(" </frameset>\r\n");
      out.write("\r\n");
      out.write("<noframes>\r\n");
      out.write("<body>\r\n");
      out.write("</body>\r\n");
      out.write("</noframes></html>\r\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
