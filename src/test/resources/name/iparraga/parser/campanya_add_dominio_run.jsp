<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.rmi.NoSuchObjectException"%>
<%@page import="netquest.generic.lang.LogicalExceptionType"%>
<%@page import="netquest.generic.daolao.Factory"%>
<%@page import="netquest.generic.servlet.ErrorInfo"%>
<%@page import="netquest.generic.servlet.http.HttpRequestUtils"%>
<%@page import="netquest.generic.servlet.jsp.PageContextFactory"%>
<%@page import="netquest.ppi.dominio.DominioFraudulento"%>
<%@page import="netquest.ppi.dominio.DominioFraudulentoLAO"%>
<%@page import="netquest.ppi.security.LoginLAO"%>
<%@page import="netquest.ppi.security.SecurityLAO"%>
<%@page import="netquest.ppi.usuario.Usuario"%>
<%@page import="netquest.ppi.usuario.UsuarioCuentaPanelizacion"%>
<%@page import="net.sf.json.JSONSerializer"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<fmt:setBundle basename="locales.campanyas.campanyas_params" scope="page"/>
<fmt:message key="dominio_ya_existe" var="dominio_ya_existe" scope="page"/>
<%
	PageContextFactory.setPageContext(pageContext);

	ErrorInfo tuple = new ErrorInfo();
	tuple.setName("");
	tuple.setValue("");
	tuple.setType(LogicalExceptionType.ERROR.getId()); // gets overwritten in case of no error

	LoginLAO loginLAO = Factory.getLAO(LoginLAO.class);
	if(!loginLAO.isLogged(UsuarioCuentaPanelizacion.class, session)) {
		tuple.setType(LogicalExceptionType.NOT_LOGGED.getId());
		out.println(JSONSerializer.toJSON(tuple));
		return;
	}
	Usuario usuario = loginLAO.getUsuario(session);
	SecurityLAO securityLAO = Factory.getLAO(SecurityLAO.class);
	securityLAO.hasAccess(securityLAO.getAreaAccion("Campañas"), usuario);

	String dominio = HttpRequestUtils.getParameter(request, "dominio");

	DominioFraudulentoLAO dominioFraudulentoLAO = Factory.getLAO(DominioFraudulentoLAO.class);
	try {
		dominioFraudulentoLAO.getDominioFraudulentoByDominio(dominio);

		tuple.setName("dominio");
		tuple.setValue((String)pageContext.getAttribute("dominio_ya_existe"));
		out.println(JSONSerializer.toJSON(tuple));
		return;

	} catch (NoSuchObjectException nsoe) {
		//Ignoramos, ya que interesa que sea así.
	}

	DominioFraudulento dominioFraudulento = new DominioFraudulento();
	dominioFraudulento.setDominio(dominio);

	dominioFraudulentoLAO.insertDominioFraudulento(dominioFraudulento);

	tuple.setValue((String)pageContext.getAttribute("ok_msg_crear"));
	tuple.setType(LogicalExceptionType.OK.getId());
	out.println(JSONSerializer.toJSON(tuple));
%>
