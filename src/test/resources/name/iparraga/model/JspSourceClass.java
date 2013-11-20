/*
 * This class was automatically generated when transforming PPI to a JEE app
 * on November 2013.
 * 
 * The code of the generator can be found at:
 * https://github.com/ivanator/jsp-parser
 * 
 * Class derived from this source JSP:
 * modules/war/jsp/tests.jsp
 */
package com.test;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
@Stateless
@Path("/test.jsp")
public class JspSourceClass {
	@GET
	public String doRun(
			@Context HttpServletRequest request,
			@Context HttpSession session) throws IOException {
		Writer out = new StringWriter();
		return out.toString();
	}
}
/*
Original JSP code as follows:
---- ---- ----
<% // original code %>
---- ---- ----
*/