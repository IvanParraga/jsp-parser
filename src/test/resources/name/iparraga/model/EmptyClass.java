package com.test;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
@Stateless
@Path("/test.jsp")
public class EmptyClass {
	@GET
	public String doRun() throws IOException {
		Writer out = new StringWriter();
		return out.toString();
	}
}