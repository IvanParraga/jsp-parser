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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
@Stateless
@Path("/test.jsp")
public class CodeClass {
	@GET
	public String doRun() throws IOException {
		Writer out = new StringWriter();
	// some intereting code
		return out.toString();
	}
}