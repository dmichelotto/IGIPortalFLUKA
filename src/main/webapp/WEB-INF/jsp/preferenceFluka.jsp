<%@ include file="/WEB-INF/jsp/init.jsp"%>

<div id="containerDirac">
	<div id="presentationDirac">Preference</div>
	<div id="contentDirac">
		<liferay-ui:error key="portal-problem"
			message="portal-problem" />
		<liferay-ui:error key="no-proxy-found"
			message="no-proxy-found" />
		<liferay-ui:error key="lfc-execution-problem"
			message="lfc-execution-problem" />
		<liferay-ui:error key="properties-not-found"
			message="properties-problem" />
		<liferay-ui:error key="properties-file-not-found"
			message="properties-problem" />
		<liferay-ui:error key="preferences-not-saved"
			message="preferences-not-saved" />
		<liferay-ui:error key="saving-properties-problem"
			message="saving-properties-problem" />	
		<liferay-ui:error key="home-already-exists"
			message="home-already-exists" />	
			
			
		<aui:fieldset>
		<aui:column columnWidth="20">
		<br/>
		</aui:column>
		<aui:column columnWidth="25">
		<portlet:actionURL var="savePreferenceUrl">
			<portlet:param name="myaction" value="savePreference"/>
		</portlet:actionURL>
		
		<aui:form name="preferenceForm" action="${savePreferenceUrl }">
		
		
		<aui:select id="selectLFCHome" label="Select your LFC Home folder" name="LFCHome">
		
			<c:forTokens var="home" items="${homes }" delims=";">
				<c:if test="${home == selectedHome }">
					<aui:option selected="true" value="${home}">${home} (Default)</aui:option>
				</c:if>
				<c:if test="${home != selectedHome }">
					<aui:option value="${home}">${home}</aui:option>
				</c:if>
			</c:forTokens>
		
		</aui:select>
		
		<aui:button-row>
		
			<aui:button type="submit" value="Save Preference"/>
		</aui:button-row>
		
		</aui:form>
		</aui:column>
		
		<aui:column columnWidth="15">
		<br/><strong>OR</strong>
		</aui:column>
		
		
		<aui:column columnWidth="25">
		<portlet:actionURL var="createHomeUrl">
			<portlet:param name="myaction" value="createHome"/>
		</portlet:actionURL>
		<aui:form name="createForm" action="${createHomeUrl }">
		
		<aui:input name="home" label="Create a LFC Fluka Home Dir Name"/>
		
		<aui:button-row>
		
			<aui:button type="submit" value="Create Dir"/>
		</aui:button-row>
		
		</aui:form>
		</aui:column>
		
		
		</aui:fieldset>
		
	</div>
</div>