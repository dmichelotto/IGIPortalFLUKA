<%@ include file="/WEB-INF/jsp/init.jsp"%>

<script type="text/javascript">

	var list = new Array();
	
	/**
	 * Function that show the delete button if some virtual machine are selected, or hide the button if none.
	 * @param jobId - The virtual machine identifier selected.
	 */
	function viewOrHideDeleteButton(jobId) {
		var i = 0;
		var newlist = new Array();
		var isPresent = false;
		for (i = 0; i < list.length; i++) {
			if (list[i] != jobId) {
				newlist.push(list[i]);
			} else {
				isPresent = true;
			}
		}
	
		if (isPresent == false)
			list.push(jobId);
		else
			list = newlist;
	
		if (list.length == 0) {
			$(".operationButton").hide("slow");
		} else {
			$(".operationButton").show("slow");
		}
	}
	
	function setAll(element){
		var val = element.attr('checked');
		if(val == "checked"){
			$(".deleteCheckbox").attr('checked', true);
			$(".operationButton").show("slow");
		}else{
			$(".deleteCheckbox").attr('checked', false);
			$(".operationButton").hide("slow");
		}
		
	}
	
	$(document).ready(function() {
		
		if(${isAllJobsTerminate}==false){
			setTimeout(function(){
				location.href='${reloadPage}';
		    }, 60000);
		}
		
		$('.search-results').hide();
	});
	
	
</script>

<style type="text/css">
<!--

-->

.adminButton{ 
	border: 1px solid Yellow;
	background-color: LightYellow; 
	margin-bottom: 10px; 
	padding-left: 10px;  
	margin-top: 10px;
}

</style>

<div id="containerDirac">
	<div id="presentationDirac">My Jobs</div>
	<div id="contentDirac">
	
		<liferay-ui:success key="submit-successufully"
			message="submit-successufully" />
		<liferay-ui:success key="upload-successufully"
			message="upload-successufully" />
		<liferay-ui:success key="resheduling-successufully"
			message="resheduling-successufully" />
		<liferay-ui:success key="deleting-successufully"
			message="deleting-successufully" />
		<liferay-ui:success key="save-successufully"
			message="save-successufully" />
		<liferay-ui:success key="shared-successufully"
			message="shared-successufully" />
		<liferay-ui:success key="preferences-saved-successfully"
			message="preferences-saved-successfully" />	
			
		<liferay-ui:error key="rescheduling-error"
			message="rescheduling-error" />
		<liferay-ui:error key="deleting-error"
			message="deleting-error" />
		
		<liferay-portlet:renderURL var="homeUrl">
			<portlet:param name="myaction" value="showHome" />
		</liferay-portlet:renderURL>
		
		
		<c:if test="<%= (themeDisplay.isSignedIn()) && (request.isUserInRole("FlukaAdministrator")) %>">
			<div id="buttonBar" class="adminButton">
			<portlet:renderURL var="manageTemplateUrl">
				<portlet:param name="myaction" value="showSubmitJob" />
				<portlet:param name="viewTemplate" value="true" />
			</portlet:renderURL>
			<aui:fieldset >
			
				<aui:form action="${manageTemplateUrl }">
					<aui:button-row>
						<aui:button type="submit" value="Manage FLUKA Template"/>
					</aui:button-row>
				</aui:form>
			
			</aui:fieldset>
			</div>
		</c:if>
		<div id="buttonBar">
		<aui:fieldset >
		<aui:column columnWidth="50">
			<c:if test="${template != null }">
			<portlet:renderURL var="useTemplateURL">
				<portlet:param name="myaction" value="showSubmitFlukaJob"/>
				<portlet:param name="path" value="${template }"/>
			</portlet:renderURL>
			<portlet:renderURL var="showPreferenceURL">
				<portlet:param name="myaction" value="showPreferences"/>
			</portlet:renderURL>
			<aui:form action="${useTemplateURL }">
				<aui:button-row>
					<aui:button type="submit" value="Submit New FLUKA Job"/>
					<aui:button type="button" value="Edit Preference" onClick="${showPreferenceURL }"/>
				</aui:button-row>
			</aui:form>
			</c:if>
			<c:if test="${template == null }">
			FLUKA not configured. Contact Administrators.
			</c:if>
		</aui:column>
		
		<aui:column columnWidth="50">
			<aui:form id="refresh" name="refresh">
				<aui:button-row >
					<aui:button style="float: right;" type="button" value="Refresh" onClick="location.href='${reloadPage}';"/>
				</aui:button-row>
			</aui:form>
		</aui:column>
		</aui:fieldset>
		</div>
		
		<jsp:useBean id="jobs"
				type="java.util.List<it.italiangrid.portal.fluka.db.domain.Jobs>"
				scope="request" />
		<portlet:actionURL var="operationOnMultipleJobUrl">
			<portlet:param name="myaction" value="operationOnMultipleJob"/>
		</portlet:actionURL>
		<portlet:resourceURL var="getMultipleOutputZipFileURL" escapeXml="false" id="getMultipleOutputZipFile"/>	

		<form id="multipleJobForm" name="delMultForm" action="${operationOnMultipleJobUrl}" method="POST">
			<input id="operation" type="hidden" name="operation" value="delete"/>
			
			<div class="operationButton">
			
				
				<aui:button-row>
					
					<aui:button type="submit" value="Delete Jobs"
						onClick="return confirm('Are you sure you want to delete these jobs?');" />
					<aui:button type="button" value="Reschedule Jobs" onClick="$('#operation').val('reschedule'); $('#multipleJobForm').submit();"/>
					<aui:button type="button" value="Get Outputs" onClick="$('#multipleJobForm').get(0).setAttribute('action', '${getMultipleOutputZipFileURL }'); $('#multipleJobForm').submit();"/>
				</aui:button-row>
				
			</div>
			
			<liferay-ui:search-container
				emptyResultsMessage="No Jobs" delta="20">
				<liferay-ui:search-container-results>
	
					<%
						results = ListUtil.subList(jobs,
										searchContainer.getStart(),
										searchContainer.getEnd());
						total = jobs.size();
	
						pageContext.setAttribute("results", results);
						pageContext.setAttribute("total", total);
					%>
	
				</liferay-ui:search-container-results>
				<liferay-ui:search-container-row
					className="it.italiangrid.portal.fluka.db.domain.Jobs"
					keyProperty="jobId" modelVar="job">
					
					<liferay-ui:search-container-column-text name="Sel <input type='checkbox' onclick='setAll($(this));'/>">
						<input class="deleteCheckbox" name="jobs" type="checkbox"
							value="${job.jobId }"
							onchange="viewOrHideDeleteButton('${job.jobId }');"></input>
					</liferay-ui:search-container-column-text>
					<liferay-ui:search-container-column-text name="Job ID"
						property="jobId" />
					<liferay-ui:search-container-column-text name="Name">
					<portlet:renderURL windowState="<%= LiferayWindowState.POP_UP.toString() %>" var="getJdlURLonName">
								<portlet:param name="myaction" value="getJDL"/>
								<portlet:param name="jobId" value="${job.jobId }"/>
							</portlet:renderURL>
					<a href="#" onclick="$(this).modal3({width:600, height:450, message:true, redirect:'${homeUrl}', src: '${getJdlURLonName}'}).open();">${job.jobName }</a>
					</liferay-ui:search-container-column-text>
					<liferay-ui:search-container-column-text name="Submitted"
						>${job.submissionTime } </liferay-ui:search-container-column-text>	
					<liferay-ui:search-container-column-text name="Site"
						property="site" />
							
						
					<liferay-ui:search-container-column-text name="Status">
						<c:choose>
							<c:when test="${job.status == 'Failed' }">
								<div id="failedStatus" class="status"></div>
							</c:when>
							<c:when test="${job.status == 'Done' }">
								<div id="doneStatus" class="status"></div>
							</c:when>
							<c:when test="${job.status == 'Received' }">
								<div id="receivedStatus" class="status"></div>
							</c:when>
							<c:when test="${job.status == 'Waiting' }">
								<div id="waitingStatus" class="status"></div>
							</c:when>
							<c:when test="${job.status == 'Deleted' }">
								<div id="deletedStatus" class="status"></div>
							</c:when>
							<c:otherwise>
								<div id="runningStatus" class="status"></div>
							</c:otherwise>
						</c:choose>
						<div id="status value"><strong>${job.status }</strong> at ${job.heartBeatTime } <br/> <span class="minorStatus">${job.minorStatus }</span></div>
						<div id="reset"></div>
					</liferay-ui:search-container-column-text>
						
					<liferay-ui:search-container-column-text name="Actions">
					
						<liferay-ui:icon-menu>
							<c:if test="${job.status == 'Done' || job.minorStatus == 'Application Finished With Errors'}">
								<portlet:resourceURL  var="outpuURL" escapeXml="false" id="getOutputZipFile">
									<portlet:param name="jobId" value="${job.jobId }"/>
								</portlet:resourceURL>
								<liferay-ui:icon image="download" message="GetOutput" url="${outpuURL}" />
							</c:if>
							
							<portlet:renderURL windowState="<%= LiferayWindowState.POP_UP.toString() %>" var="getJdlURL">
								<portlet:param name="myaction" value="getJDL"/>
								<portlet:param name="jobId" value="${job.jobId }"/>
							</portlet:renderURL>
							<liferay-ui:icon image="history" message="JDL" url="javascript:$(this).modal3({width:600, height:450, message:true, redirect:'${homeUrl}', src: '${getJdlURL}'}).open();" />
							
							<portlet:actionURL var="rescheduleURL">
								<portlet:param name="myaction" value="rescheduleJob"/>
								<portlet:param name="jobId" value="${job.jobId }"/>
							</portlet:actionURL>
							<liferay-ui:icon image="configuration" message="Reschedule" url="${rescheduleURL}" />
							
							<c:if test="<%= request.isUserInRole("FlukaAdministrator") %>">
							
								<portlet:renderURL var="duplicateURL">
									<portlet:param name="myaction" value="showSubmitJob"/>
									<portlet:param name="jobId" value="${job.jobId }"/>
								</portlet:renderURL>
								<liferay-ui:icon image="copy" message="Duplicate" url="${duplicateURL}" />
							</c:if>
						
							<portlet:actionURL var="deleteURL">
								<portlet:param name="myaction" value="deleteJob"/>
								<portlet:param name="jobId" value="${job.jobId }"/>
							</portlet:actionURL>
							<liferay-ui:icon-delete url="${deleteURL}" />
						
						</liferay-ui:icon-menu>
					</liferay-ui:search-container-column-text>
					
				</liferay-ui:search-container-row>
				<liferay-ui:search-iterator />
			</liferay-ui:search-container>
			<c:if test="${fn:length(jobs) > 15 }">
				<div class="operationButton">
					<aui:button-row>
						<aui:button type="submit" value="Delete Jobs"
							onClick="return confirm('Are you sure you want to delete these jobs?');" />
						<aui:button type="button" value="Reschedule Jobs" onClick="$('#operation').val('reschedule'); $('#multipleJobForm').submit();"/>
						<aui:button type="button" value="Get Outputs" onClick="$('#multipleJobForm').get(0).setAttribute('action', '${getMultipleOutputZipFileURL }'); $('#multipleJobForm').submit();"/>
					</aui:button-row>
				</div>
			</c:if>
		</form>
	</div>
</div>