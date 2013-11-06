<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/security" prefix="liferay-security" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %> 
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %> 
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>
<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui" %>

<%@ page import="it.italiangrid.portal.dbapi.domain.*" %>
<%@ page import="it.italiangrid.portal.fluka.db.domain.*" %>
<%@ page import="it.italiangrid.portal.fluka.model.*" %>


<%@ page import="com.liferay.portal.kernel.util.ListUtil" %> 
<%@ page import="com.liferay.portal.kernel.dao.search.ResultRow" %>
<%@ page import="com.liferay.portal.kernel.util.WebKeys" %>
<%@ page import="com.liferay.portal.model.User"%> 
<%@ page import="com.liferay.portal.kernel.portlet.LiferayWindowState" %>

<%@ page import= "java.util.List" %>

<%@ page import="javax.portlet.PortletURL" %>

<portlet:defineObjects />	
<liferay-theme:defineObjects />

