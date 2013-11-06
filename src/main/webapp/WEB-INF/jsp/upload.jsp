<%@ include file="/WEB-INF/jsp/init.jsp"%>

<div id="containerDirac2">
	<div id="presentationDirac">Upload Proxy</div>
	<div id="contentDirac" style="margin-left:30px;">
	
		<liferay-ui:error key="upload-error"
			message="upload-error" />
		<liferay-ui:error key="no-certificate-found"
			message="no-certificate-found" />
		<liferay-ui:error key="certificate-not-corresponding"
			message="certificate-not-corresponding" />
		<liferay-ui:error key="check-certificate-password"
			message="check-certificate-password" />
			
		<portlet:actionURL var="uploadCertUrl">
			<portlet:param name="myaction" value="uploadCert" />
		</portlet:actionURL>
		<div class="portlet-msg-alert" >Your certificate isn't already uploaded for the selected VO or are expired. <br/>Please upload your certificate (.p12 or pfx format) and insert the password used to encrypt it.</div>
		<aui:form name="newCert" action="${uploadCertUrl }" commandName="uploadCert" enctype="multipart/form-data">
			<aui:input name="certificate" type="file" label="Import Certificate (.p12 or pfx format)"/>
			<aui:input name="password" type="password" label="Import certificate password"/>
			<aui:button-row>
				<aui:button type="submit" value="Upload"/>
			</aui:button-row>
		</aui:form>
		
	</div>
</div>