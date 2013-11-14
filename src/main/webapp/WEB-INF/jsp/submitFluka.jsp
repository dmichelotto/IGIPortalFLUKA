<%@ include file="/WEB-INF/jsp/init.jsp"%>

<script type="text/javascript" src="https://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/jquery.dataTables.min.js"></script>

<script type="text/javascript">
	var count = 0;
	function deleteFile(divName){
		$("#"+divName).remove();
		count = count - 1;
		if(count == 0){
			$("#inputSanboxDiv").hide();
		}
	}
	
	$('#addedFile_${count }').remove();
	function addFile(){
		$("#inputSanboxDiv").show();
		$("#inputSanboxDiv").append("<div id=\"addFile_"+count+"\"><input id=\"uploadFile_"+count+"\" class=\"multi\" type=\"file\" name=\"uploadFile_"+count+"\"> <a href=\"#addFile\" onclick=\"deleteFile('addFile_"+count+"');\"><img src=\"<%=request.getContextPath()%>/images/NewDelete.png\" width=\"14\" height=\"14\" /></a></div>");
		count = count + 1;
	}
	function deleteAll(){
		var i;
		for(i=0; i<=count; i++){
			$("#addFile_"+i).remove();
		}
	}
	function uploadExecutable(){
		$("#executableDiv .aui-field-element").html("<input type=\"file\" value=\"\" name=\"_IGIPortalDIRAC_WAR_IGIPortalDIRAC001_INSTANCE_mpwer7lWR8f9_executableFile\" id=\"_IGIPortalDIRAC_WAR_IGIPortalDIRAC001_INSTANCE_mpwer7lWR8f9_executableFile\" class=\"aui-field-input aui-field-input-text\">  or <a href=\"#executableDiv\" onClick=\"specifyExecutable();\">Specify your executable</a>");	
	}
	function specifyExecutable(){
		$("#executableDiv .aui-field-element").html("<input id=\"_IGIPortalDIRAC_WAR_IGIPortalDIRAC001_INSTANCE_mpwer7lWR8f9_executable\" class=\"aui-field-input aui-field-input-text\" type=\"text\" value=\"/bin/ls\" name=\"_IGIPortalDIRAC_WAR_IGIPortalDIRAC001_INSTANCE_mpwer7lWR8f9_executable\">  or <a href=\"#executableDiv\" onClick=\"uploadExecutable(); $('#argumentsDiv input').val('');\">Upload your executable</a>");
	}
	
	function appendExecutable(){
		$("#executableDiv .aui-field-element").append("  or <a href=\"#executableDiv\" onClick=\"uploadExecutable(); $('#argumentsDiv input').val('');\">Upload your executable</a>");
	}
	
	function appendInputSandbox(){
		$("#inputSanboxDiv .aui-field-element").append(" <a href=\"#inputSanboxDiv\" onClick=\"$('#inputSanboxDiv').hide(); deleteAll(); count = -1;\"><img src=\"<%=request.getContextPath()%>/images/NewDelete.png\" width=\"14\" height=\"14\" /></a><div id=\"addFile\"> </div>");
	}
	
	function changeMoreHelpVisibility(divId, linkText){
		if ($('#'+divId).css('display') == 'none') {
			$('#'+divId).show();
			linkText.text('Less');
		}else{
			$('#'+divId).hide();
			linkText.text('More');
		}
	}
	
	function changeCheckbox(input, share, saveOnly){
		if(input.attr('checked')=='checked'){
			$("#"+share).removeAttr("disabled");
			$("#"+saveOnly).removeAttr("disabled");
			$('#submitButton').attr('value','Save & Submit');
			$('#checks').show('slow');
		}else{
			$("#"+share).attr("disabled", true);
			$("#"+share).removeAttr("checked");
			$("#"+saveOnly).attr("disabled", true);
			$("#"+saveOnly).removeAttr("checked");
			$('#submitButton').attr('value','Submit');
			$('#checks').hide('slow');
		}
	}
	
	function changeTemplate(){
		if ($('.templates').css('display') == 'none') {
			$('.templates').show('slow');
			$('.jdlDiv').hide('slow');
		}else{
			$('.templates').hide('slow');
			$('.jdlDiv').show('slow');
			$("#saveAsTemplate").attr('checked', true);
			$("#saveOnly").attr('checked', true);
			changeCheckbox($('#saveAsTemplate'), 'shareTemplate', 'saveOnly');
			changeSubmitButton($('#saveOnly'))
		}
	}
	
	function changeSubmitButton(el){
		if(el.attr('checked')=='checked'){
			$('#submitButton').attr('value','Save');
		}else{
			$('#submitButton').attr('value','Save & Submit');
		}
	}
	
	var list = new Array();
	
	/**
	 * Function that show the delete button if some virtual machine are selected, or hide the button if none.
	 * @param jobId - The virtual machine identifier selected.
	 */
	function viewOrHideOperationButton(jobId) {
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
			$(".operationCheckbox").attr('checked', true);
			$(".operationButton").show("slow");
		}else{
			$(".operationCheckbox").attr('checked', false);
			$(".operationButton").hide("slow");
		}
		
	}
	
	$(document).ready(function() {
		appendExecutable();
		$('#template_table').dataTable();
		//appendInputSandbox();
		});
</script>

<div id="containerDirac">
	<div id="presentationDirac">My Jobs</div>
	<div id="contentDirac">
	
		<liferay-ui:success key="save-successufully"
			message="save-successufully" />
		<liferay-ui:success key="shared-successufully"
			message="shared-successufully" />
		<liferay-ui:success key="unshared-successufully"
			message="unshared-successufully" />
		<liferay-ui:success key="deleting-template-successufully"
			message="deleting-template-successufully" />
	
		<liferay-ui:error key="submit-error"
			message="submit-error" />
		<liferay-ui:error key="check-jdl"
			message="check-jdl" />	
		<liferay-ui:error key="deleting-template-error"
			message="deleting-template-error" />
		<liferay-ui:error key="save-error"
			message="save-error" />
		<liferay-ui:error key="shared-error"
			message="shared-error" />
		<liferay-ui:error key="operation-error"
			message="operation-error" />
			
		<portlet:actionURL var="submitUrl">
			<portlet:param name="myaction" value="submitJob" />
		</portlet:actionURL>
		<portlet:actionURL var="goHome">
			<portlet:param name="myaction" value="goHome"></portlet:param>
			<portlet:param name="settedPath" value="${jdl.path }"></portlet:param>
		</portlet:actionURL>
		
		<jsp:useBean id="vos"
				type="java.util.List<it.italiangrid.portal.dbapi.domain.Vo>"
				scope="request" />
		
		<div  class="jdlDiv">
		<aui:form name="newJdl" action="${submitUrl }" commandName="jdl" enctype="multipart/form-data">
			<div id="myJdl">
				<aui:fieldset label="JDL">
					<div>
						<aui:input type="text" label="Job Name" name="jobName" value="${jdl.jobName }"/>
						<div class="help" style="float: left; width: 100%; margin-bottom: 10px;">
							<strong>Help:</strong> The "%s" indicate the nuober of the job.</a>
						</div>
					</div>
					<div id="executableDiv" style="display:none">
						<aui:input type="text" id="executable" label="Executable" name="executable" value="${jdl.executable }"/>
					</div>
					<div id="argumentsDiv" style="display:none">
						<aui:input type="text" label="Arguments" name="arguments" value="${jdl.arguments }"/>
					</div>
					<c:if test="${fn:length(vos)>1 }">
					<label for="selectVO"><strong>VO</strong></label><br/>
					<select id="selectVO" name="vo">
					
						<c:forEach var="vo" items="${vos }">
							<c:if test="${vo.vo == defaultVo }">
								<option selected="true" value="${vo.vo }">${vo.vo } (Default)</option>
							</c:if>
							<c:if test="${vo.vo != defaultVo }">
								<option value="${vo.vo }">${vo.vo }</option>
							</c:if>
						</c:forEach>
					
					</select>
					</c:if>
					<c:if test="${fn:length(vos)==1 }">
						<aui:input type="hidden" name="vo" value="${vos[0].vo }"/>
					</c:if>
					
					<aui:input type="hidden" name="myProxyServer" value="${jdl.myProxyServer }"/>
					<aui:input type="hidden" name="settedPath" value="${jdl.path }"/>
					
					<div id="stdOutputDiv" style="display: none;">
						
						<div style="float: left;">
							<aui:input type="text" label="Standard Output" name="stdOutput" value="${jdl.stdOutput }"/>
						</div>
						<div style="float: left; margin-top: 32px; margin-left: 4px; display: none;">
							<a href="#addFile" onclick="$('#stdOutputDiv').hide(); $('#stdoutadd').show(); $('#stdoutremove').hide();"><img src="<%=request.getContextPath()%>/images/NewDelete.png" width="14" height="14" /></a>
						</div>
						<div style="clear: both;"></div>
					</div>
					<div id="stdErrorDiv" style="display: none;">
						
						<div style="float: left;">
							<aui:input type="text" label="Standard Error" name="stdError" value="${jdl.stdError }"/>
						</div>
						<div style="float: left; margin-top: 32px; margin-left: 4px; display: none;">
							<a href="#addFile" onclick="$('#stdErrorDiv').hide(); $('#stderradd').show(); $('#stderrremove').hide();"><img src="<%=request.getContextPath()%>/images/NewDelete.png" width="14" height="14" /></a>
						</div>
						<div style="clear: both;"></div>
					</div>
					<div id="parametersDiv" style="display: none;">
						
						<div style="float: left;">
							<aui:input type="text" label="Number of Jobs" name="parameters" value="${jdl.parameters }" />
						</div>
						<div style="float: left; margin-top: 32px; margin-left: 4px; display: none;">
							<a href="#addFile" onclick="$('#parametersDiv').hide(); $('#parametersadd').show();  $('#parametersremove').hide(); $('#parameterStartDiv').hide(); $('#parameterStartadd').show(); $('#parameterStartremove').hide(); $('#parameterStepDiv').hide(); $('#parameterStepadd').show(); $('#parameterStepremove').hide(); $('#parametersDiv input').val(''); $('#parameterStartDiv input').val(''); $('#parameterStepDiv input').val('');"><img src="<%=request.getContextPath()%>/images/NewDelete.png" width="14" height="14" /></a>
						</div>
						<div style="clear: both;"></div>
						<div class="help" style="float: left; width: 80%;">
							<strong>Help:</strong> The number of job that you want to submit. Max value corrispond at the number of inputs included into the input tar archive.</a>
						</div>
						
						
					</div>
					
					<div id="parameterStartDiv" style="display: none;">
						
						<div style="float: left;">
							<aui:input type="text" label="Start Number" name="parameterStart" value="${jdl.parameterStart }"/>
						</div>
						<div style="float: left; margin-top: 32px; margin-left: 4px; display: none;">
							<a href="#addFile" onclick="$('#parameterStartDiv').hide(); $('#parameterStartadd').show(); $('#parameterStartremove').hide(); $('#parameterStartDiv input').val(''); $('#parameterStepDiv').hide(); $('#parameterStepadd').show(); $('#parameterStepremove').hide(); $('#parameterStepDiv input').val('');"><img src="<%=request.getContextPath()%>/images/NewDelete.png" width="14" height="14" /></a>
						</div>
						<div style="clear: both;"></div>
						<div class="help" style="float: left; width: 80%;">
							<strong>Help:</strong> The number of first job. <a href="#moreHelpParameters" onclick="changeMoreHelpVisibility('moreHelpParameters', $(this));">More</a>
							<div id="moreHelpParameters" class="moreHelp">
								Eg.
								<ul>
									<li>Number of Jobs 5, Start Number 1 products job from 1 to 5</li>
									<li>Number of Jobs 5, Start Number 3 products job from 3 to 7</li>
								</ul>
							</div>
						</div>
					</div>

					<div id="parameterStepDiv" style="display: none;">
						
						<div style="float: left;">
							<aui:input type="text" label="Parameter Step" name="parameterStep" value="${jdl.parameterStep }"/>
						</div>
						<div style="float: left; margin-top: 32px; margin-left: 4px; display: none;">
							<a href="#addFile" onclick="$('#parameterStartDiv').hide(); $('#parameterStartadd').show(); $('#parameterStartremove').hide(); $('#parameterStartDiv input').val(''); $('#parameterStepDiv').hide(); $('#parameterStepadd').show(); $('#parameterStepremove').hide(); $('#parameterStepDiv input').val('');"><img src="<%=request.getContextPath()%>/images/NewDelete.png" width="14" height="14" /></a>
						</div>
						<div style="clear: both;"></div>
					</div>
					
					<div id="cpuNumberDiv" style="display: none;">
						
						<div style="float: left;">
							<aui:input type="text" label="CPU Number (MPI)" name="cpuNumber" value="${jdl.cpuNumber }"/>
						</div>
						<div style="float: left; margin-top: 32px; margin-left: 4px; display: none;">
							<a href="#addFile" onclick="$('#cpuNumberDiv').hide(); $('#cpuadd').show(); $('#cpuremove').hide(); $('#cpuNumberDiv input').val('');"><img src="<%=request.getContextPath()%>/images/NewDelete.png" width="14" height="14" /></a>
						</div>
						<div style="clear: both;"></div>
					</div>
					
					<div id="hostNumberDiv" style="display: none;">
						
						<div style="float: left;">
							<aui:input type="text" label="Host Number (MPI)" name="hostNumber" value="${jdl.hostNumber }"/>
						</div>
						<div style="float: left; margin-top: 32px; margin-left: 4px; display: none;">
							<a href="#addFile" onclick="$('#hostNumberDiv').hide(); $('#hostadd').show(); $('#hostremove').hide(); $('#hostNumberDiv input').val('');"><img src="<%=request.getContextPath()%>/images/NewDelete.png" width="14" height="14" /></a>
						</div>
						<div style="clear: both;"></div>
					</div>
					
					<div id="wholeNodesDiv" style="display: none;">
						
						<div style="float: left;">
							<aui:input type="text" label="Whole Nodes (MPI)" name="wholeNodes" value="${jdl.wholeNodes }"/>
						</div>
						<div style="float: left; margin-top: 32px; margin-left: 4px; display: none;">
							<a href="#addFile" onclick="$('#wholeNodesDiv').hide(); $('#wholeadd').show(); $('#wholeremove').hide(); $('#wholeNodesDiv input').val('');"><img src="<%=request.getContextPath()%>/images/NewDelete.png" width="14" height="14" /></a>
						</div>
						<div style="clear: both;"></div>
						<div class="help">
							<strong>Help:</strong> True or False. 
						</div>
					</div>
					
					<div id="smpGranularityDiv" style="display: none;">
						
						<div style="float: left;">
							<aui:input type="text" label="SMP Granularity (MPI)" name="smpGranularity" value="${jdl.smpGranularity }"/>
						</div>
						<div style="float: left; margin-top: 32px; margin-left: 4px; display: none;">
							<a href="#addFile" onclick="$('#smpGranularityDiv').hide(); $('#smpadd').show(); $('#smpremove').hide(); $('#smpGranularityDiv input').val('');"><img src="<%=request.getContextPath()%>/images/NewDelete.png" width="14" height="14" /></a>
						</div>
						<div style="clear: both;"></div>
					</div>
					
					<div id="requirementsDiv" style="display: none;">
						
						<div style="float: left;">
							<aui:input type="textarea" cols="80" rows ="5" label="Requirements" name="requirements" value="${jdl.requirements }"/>
						</div>
						<div style="float: left; margin-top: 32px; margin-left: 4px; display: none;">
							<a href="#addFile" onclick="$('#requirementsDiv').hide(); $('#requirementsadd').show(); $('#requirementsremove').hide(); $('#requirementsDiv textarea').val('');"><img src="<%=request.getContextPath()%>/images/NewDelete.png" width="14" height="14" /></a>
						</div>
						<div style="clear: both;"></div>
						<div class="help">
							<strong>Example:</strong> (other.GlueHostMainMemoryRAMSize>1024)&&(GlueCEStateFreeCPUs>2)</br>
							Do not specify a target CE or queue, but use the <a href="#siteDiv" onclick="$('#sitesDiv').show(); setTimeout( function() { $('#sitesDiv input').focus(); }, 200 ); $('#sitesremove').show(); $('#sitesadd').hide();">Site</a> field.
						</div>
					</div>
					
					<div id="sitesDiv" style="display: none; margin-top: 10px;">
						
						<div style="float: left;">
							<label for="selectSite"><strong>Site</strong></label><br/>
							<select id="selectSite" name="site">
						
							<c:forEach var="site" items="${sites }">
								<c:if test="${site == jdl.site }">
									<option selected="true" value="${site }">${site }</option>
								</c:if>
								<c:if test="${site != jdl.site }">
									<option value="${site }">${site }</option>
								</c:if>
							</c:forEach>
						
							</select>
						</div>
						<div style="float: left; margin-top: 20px; margin-left: 4px;">
							<a href="#addFile" onclick="$('#sitesDiv').hide(); $('#sitesadd').show(); $('#sitesremove').hide(); $('#sitesDiv select').val('ANY')"><img src="<%=request.getContextPath()%>/images/NewDelete.png" width="14" height="14" /></a>
						</div>
						<div style="clear: both;"></div>
					</div>
								
					<div id="outputSandboxDiv" style="display: none;">
						<div style="float: left;">
							<aui:input type="text" label="Output Sandbox" name="outputSandboxRequest" value="${jdl.outputSandboxRequest }"/>
						</div>
						<div style="float: left; margin-top: 32px; margin-left: 4px; display: none;">
							<a href="#addFile" onclick="$('#outputSandboxDiv').hide(); $('#outadd').show(); $('#outremove').hide(); $('#outputSandboxDiv input').val('');"><img src="<%=request.getContextPath()%>/images/NewDelete.png" width="14" height="14" /></a>
						</div>
						<div style="clear: both;"></div>
						<div class="help">
							<strong>Help:</strong> Separate with <strong>";"</strong> for multiple files. 
						</div>
					</div>
					
					<div id="inputSanboxDiv" style="display: none;">
						<label style="margin-top: 10px;" id="aui_3_4_0_1_1045" class="aui-field-label" for="_IGIPortalDIRAC_WAR_IGIPortalDIRAC001_INSTANCE_mpwer7lWR8f9_inputSandbox"> Input SandBox </label>
						<div class="help">
							<strong>Help:</strong> If the file name contains white spaces, these will be replaced with '_'.
						</div>
						<c:set var="count" value="0"/>
						
						<c:forTokens items="${jdl.inputSandbox }" delims="," var="input">
							<c:forTokens items="${input }" delims="/" var="last">
								<c:set var="fileName" value="${last }"/>
							</c:forTokens>
							<c:if test="${fn:replace(fn:replace(fileName, ']', ''), '[', '')!=jdl.executable }">
								<div id="addedFile_${count }"><input id="uploadedFile_${count }" type="input" name="uploadedFile_${count }" value="${fn:replace(fn:replace(fileName, ']', ''), '[', '') }"  readonly> <a href="#addFile" onclick="deleteFile('addedFile_${count }');"><img src="<%=request.getContextPath()%>/images/NewDelete.png" width="14" height="14" /></a></div>
								<c:set var="count" value="${count + 1 }"/>
							</c:if>
						</c:forTokens>
						<script type="text/javascript">
							
							count=${count};
							if(count>0){
								$("#inputSanboxDiv").show();
							}
						</script>
						
					</div>
				</aui:fieldset>
				
				
			</div>
			<div id="addMenu" style="display:none">
				<aui:fieldset label="Add or Remove fields">
					<div>
					<a id="stdoutadd" href="#stdOutputDiv" onclick="$('#stdOutputDiv').show(); setTimeout( function() { $('#stdOutputDiv input').focus(); }, 200 ); $('#stdoutremove').show(); $('#stdoutadd').hide();"><img src="<%=request.getContextPath()%>/images/NewAdd.png" width="14" height="14" /> Standard Output</a>
					<a id="stdoutremove" style="display: none;" href="#stdOutputDiv" onclick="$('#stdOutputDiv').hide(); $('#stdoutadd').show(); $('#stdoutremove').hide();"><img src="<%=request.getContextPath()%>/images/NewDelete.png" width="14" height="14" /> Standard Output</a>
					</div>
					<div>
					<a id="stderradd" href="#stdErrorDiv" onclick="$('#stdErrorDiv').show(); setTimeout( function() { $('#stdErrorDiv input').focus(); }, 200 ); $('#stderrremove').show(); $('#stderradd').hide();"><img src="<%=request.getContextPath()%>/images/NewAdd.png" width="14" height="14" /> Standard Error</a>
					<a id="stderrremove" style="display: none;" href="#stdErrorDiv" onclick="$('#stdErrorDiv').hide(); $('#stderradd').show(); $('#stderrremove').hide();"><img src="<%=request.getContextPath()%>/images/NewDelete.png" width="14" height="14" /> Standard Error</a>
					</div>
					<div>
					<a id="outadd" href="#outputSandboxDiv" onclick="$('#outputSandboxDiv').show(); setTimeout( function() { $('#outputSandboxDiv input').focus(); }, 200 ); $('#outremove').show(); $('#outadd').hide();"><img src="<%=request.getContextPath()%>/images/NewAdd.png" width="14" height="14" /> Output Sandbox</a>
					<a id="outremove" style="display: none;" href="#outputSandboxDiv" onclick="$('#outputSandboxDiv').hide(); $('#outadd').show(); $('#outremove').hide(); $('#outputSandboxDiv input').val('');"><img src="<%=request.getContextPath()%>/images/NewDelete.png" width="14" height="14" /> Output Sandbox</a>
					</div>
					<div>
					<a href="#inputSanboxDiv" onclick="addFile();"><img src="<%=request.getContextPath()%>/images/NewAdd.png" width="14" height="14" /> Input Sandbox</a>
					</div>	
					<hr/>
					<div style="display: none;">
					<label id="aui_3_4_0_1_1045" class="aui-field-label" for="_IGIPortalDIRAC_WAR_IGIPortalDIRAC001_INSTANCE_mpwer7lWR8f9_inputSandbox"> MPI </label>
					<div>
					<a id="cpuadd" href="#cpuNumberDiv" onclick="$('#cpuNumberDiv').show(); setTimeout( function() { $('#cpuNumberDiv input').focus(); }, 200 ); $('#cpuremove').show(); $('#cpuadd').hide();"><img src="<%=request.getContextPath()%>/images/NewAdd.png" width="14" height="14" /> CPU Number</a>
					<a id="cpuremove" style="display: none;" href="#cpuNumberDiv" onclick="$('#cpuNumberDiv').hide(); $('#cpuadd').show(); $('#cpuremove').hide(); $('#cpuNumberDiv input').val('');"><img src="<%=request.getContextPath()%>/images/NewDelete.png" width="14" height="14" /> CPU Number</a>
					</div>
					<div>
					<a id="hostadd" href="#hostNumberDiv" onclick="$('#hostNumberDiv').show(); setTimeout( function() { $('#hostNumberDiv input').focus(); }, 200 ); $('#hostremove').show(); $('#hostadd').hide();"><img src="<%=request.getContextPath()%>/images/NewAdd.png" width="14" height="14" /> Host Number</a>
					<a id="hostremove" style="display: none;" href="#hostNumberDiv" onclick="$('#hostNumberDiv').hide(); $('#hostadd').show(); $('#hostremove').hide(); $('#hostNumberDiv input').val('');"><img src="<%=request.getContextPath()%>/images/NewDelete.png" width="14" height="14" /> Host Number</a>
					</div>
					<div>
					<a id="wholeadd" href="#wholeNodesDiv" onclick="$('#wholeNodesDiv').show(); setTimeout( function() { $('#wholeNodesDiv input').focus(); }, 200 ); $('#wholeremove').show(); $('#wholeadd').hide();"><img src="<%=request.getContextPath()%>/images/NewAdd.png" width="14" height="14" /> Whole Nodes</a>
					<a id="wholeremove" style="display: none;" href="#wholeNodesDiv" onclick="$('#wholeNodesDiv').hide(); $('#wholeadd').show(); $('#wholeremove').hide(); $('#wholeNodesDiv input').val('');"><img src="<%=request.getContextPath()%>/images/NewDelete.png" width="14" height="14" /> Whole Nodes</a>
					</div>
					<div>
					<a id="smpadd" href="#smpGranularityDiv" onclick="$('#smpGranularityDiv').show(); setTimeout( function() { $('#smpGranularityDiv input').focus(); }, 200 ); $('#smpremove').show(); $('#smpadd').hide();"><img src="<%=request.getContextPath()%>/images/NewAdd.png" width="14" height="14" /> SMP Granularity</a>
					<a id="smpremove" style="display: none;" href="#smpGranularityDiv" onclick="$('#smpGranularityDiv').hide(); $('#smpadd').show(); $('#smpremove').hide(); $('#smpGranularityDiv input').val('');"><img src="<%=request.getContextPath()%>/images/NewDelete.png" width="14" height="14" /> SMP Granularity</a>
					</div>
					<hr/>
					</div>
					<label id="aui_3_4_0_1_1045" class="aui-field-label" for="_IGIPortalDIRAC_WAR_IGIPortalDIRAC001_INSTANCE_mpwer7lWR8f9_inputSandbox"> PARAMETRIC </label>
					<div>
					<a id="parametersadd" href="#parametersDiv" onclick="$('#parametersDiv').show(); $('#parametersremove').show(); $('#parametersadd').hide(); setTimeout( function() { $('#parametersDiv input').focus(); }, 200 );"><img src="<%=request.getContextPath()%>/images/NewAdd.png" width="14" height="14" /> Parameters</a>
					<a id="parametersremove" style="display: none;" href="#parametersDiv" onclick="$('#parametersDiv').hide(); $('#parametersadd').show();  $('#parametersremove').hide(); $('#parameterStartDiv').hide(); $('#parameterStartadd').show(); $('#parameterStartremove').hide(); $('#parameterStepDiv').hide(); $('#parameterStepadd').show(); $('#parameterStepremove').hide(); $('#parametersDiv input').val(''); $('#parameterStartDiv input').val(''); $('#parameterStepDiv input').val('');"><img src="<%=request.getContextPath()%>/images/NewDelete.png" width="14" height="14" /> Parameters</a>
					</div>
					<div>
					<a id="parameterStartadd" href="#parameterStartDiv" onclick="$('#parametersDiv').show(); $('#parametersremove').show(); $('#parametersadd').hide(); $('#parameterStepDiv').show(); $('#parameterStepremove').show(); $('#parameterStepadd').hide(); $('#parameterStartDiv').show(); setTimeout( function() { $('#parameterStartDiv input').focus(); }, 200 ); $('#parameterStartremove').show(); $('#parameterStartadd').hide();"><img src="<%=request.getContextPath()%>/images/NewAdd.png" width="14" height="14" /> Parameter Start</a>
					<a id="parameterStartremove" style="display: none;" href="#parameterStartDiv" onclick="$('#parameterStartDiv').hide(); $('#parameterStartadd').show(); $('#parameterStartremove').hide(); $('#parameterStartDiv input').val(''); $('#parameterStepDiv').hide(); $('#parameterStepadd').show(); $('#parameterStepremove').hide(); $('#parameterStepDiv input').val('');"><img src="<%=request.getContextPath()%>/images/NewDelete.png" width="14" height="14" /> Parameter Start</a>
					</div>
					<div>
					<a id="parameterStepadd" href="#parameterStepDiv" onclick="$('#parametersDiv').show(); $('#parametersremove').show(); $('#parametersadd').hide(); $('#parameterStartDiv').show(); $('#parameterStartremove').show(); $('#parameterStartadd').hide(); $('#parameterStepDiv').show(); setTimeout( function() { $('#parameterStepDiv input').focus(); }, 200 ); $('#parameterStepremove').show(); $('#parameterStepadd').hide();"><img src="<%=request.getContextPath()%>/images/NewAdd.png" width="14" height="14" /> Parameter Step</a>
					<a id="parameterStepremove" style="display: none;" href="#parameterStepDiv" onclick="$('#parameterStartDiv').hide(); $('#parameterStartadd').show(); $('#parameterStartremove').hide(); $('#parameterStartDiv input').val(''); $('#parameterStepDiv').hide(); $('#parameterStepadd').show(); $('#parameterStepremove').hide(); $('#parameterStepDiv input').val('');"><img src="<%=request.getContextPath()%>/images/NewDelete.png" width="14" height="14" /> Parameter Step</a>
					</div>
					<hr/>
					<label id="aui_3_4_0_1_1045" class="aui-field-label" for="_IGIPortalDIRAC_WAR_IGIPortalDIRAC001_INSTANCE_mpwer7lWR8f9_inputSandbox"> REQUIREMENTS </label>
					<div>
					<a id="requirementsadd" href="#requirementsDiv" onclick="$('#requirementsDiv').show(); setTimeout( function() { $('#requirementsDiv input').focus(); }, 200 ); $('#requirementsremove').show(); $('#requirementsadd').hide();"><img src="<%=request.getContextPath()%>/images/NewAdd.png" width="14" height="14" /> Requirements</a>
					<a id="requirementsremove" style="display: none;" href="#requirementsDiv" onclick="$('#requirementsDiv').hide(); $('#requirementsadd').show(); $('#requirementsremove').hide(); $('#requirementsDiv textarea').val('');"><img src="<%=request.getContextPath()%>/images/NewDelete.png" width="14" height="14" /> Requirements</a>
					</div>
					<div>
					<a id="sitesadd" href="#sitesDiv" onclick="$('#sitesDiv').show(); setTimeout( function() { $('#sitesDiv input').focus(); }, 200 ); $('#sitesremove').show(); $('#sitesadd').hide();"><img src="<%=request.getContextPath()%>/images/NewAdd.png" width="14" height="14" /> Sites</a>
					<a id="sitesremove" style="display: none;" href="#sitesDiv" onclick="$('#sitesDiv').hide(); $('#sitesadd').show(); $('#sitesremove').hide();"><img src="<%=request.getContextPath()%>/images/NewDelete.png" width="14" height="14" /> Sites</a>
					</div>
				</aui:fieldset>	
			</div>
			<div id="reset"></div>
			<div id="checks" style="margin: 0 0 0 15px; display: none;">
			<input id="saveAsTemplate" name="saveAsTemplate" type="hidden" value="false" onClick="changeCheckbox($(this), 'shareTemplate', 'saveOnly');"/> <Strong>Save As Template</Strong><br/>
			<input id="saveOnly" name="saveOnly" type="checkbox"  disabled="disabled" onClick="changeSubmitButton($(this));"/> <Strong>Don't Submit Now</Strong><br/>
			<input id="shareTemplate" name="shareTemplate" type="checkbox"  disabled="disabled"/> <Strong>Share Template</Strong>
			</div>
			<aui:button-row>
			<aui:button id="submitButton" type="submit" value="Submit"/>
			<aui:button type="button" value="Job List" onClick="${goHome }"/>
			</aui:button-row>
		</aui:form>
		</div>
	</div>
</div>

<script type="text/javascript">
/*
	if("${jdl.stdOutput }"!="StdOut"){
		$("#stdOutputDiv").show();
		$('#stdoutremove').show();
		$('#stdoutadd').hide();
	}
	
	if("${jdl.stdError }"!="StdErr"){
		$("#stdErrorDiv").show();
		$('#stderrremove').show();
		$('#stderrremove').hide();
	}
*/
	if("${jdl.parameters }"!=""){
		$("#parametersDiv").show();
		$('#parametersremove').show();
		$('#parametersadd').hide();
	}

	if("${jdl.parameterStart }"!=""){
		$("#parameterStartDiv").show();
		$('#parameterStartremove').show();
		$('#parameterStartadd').hide();
	}
/*
	if("${jdl.parameterStep }"!=""){
		$("#parameterStepDiv").show();
		$('#parameterStepremove').show();
		$('#parameterStepadd').hide();
	}

	if("${jdl.cpuNumber }"!=""){
		$("#cpuNumberDiv").show();
		$('#cpuremove').show();
		$('#cpuadd').hide();
	}

	if("${jdl.hostNumber }"!=""){
		$("#hostNumberDiv").show();
		$('#hostremove').show();
		$('#hostadd').hide();
	}

	if("${jdl.wholeNodes }"!=""){
		$("#wholeNodesDiv").show();
		$('#wholeremove').show();
		$('#wholeadd').hide();
	}

	if("${jdl.smpGranularity }"!=""){
		$("#smpGranularityDiv").show();
		$('#smpremove').show(); 
		$('#smpadd').hide();
	}

	if("${jdl.requirements }"!=""){
		$("#requirementsDiv").show();
		$('#requirementsremove').show();
		$('#requirementsadd').hide();
	}
*/
	if("${jdl.site }"!=""){
		$("#sitesDiv").show();
		$('#sitesremove').show(); 
		$('#sitesadd').hide();
	}
	if("${jdl.outputSandboxRequest }"!=""){
		$("#outputSandboxDiv").show();
		$('#outremove').show(); 
		$('#outadd').hide();
	}

	if("${viewTemplate}"=='true'){
		$('.templates').show();
	}else{
		$('.jdlDiv').show();
	}
	
</script>


<c:if test="${showUploadCert==true}">
	<liferay-portlet:renderURL windowState="<%= LiferayWindowState.POP_UP.toString() %>" var="popUpUrl">
		<portlet:param name="myaction" value="showUploadCert" />
	</liferay-portlet:renderURL>
	
	<liferay-portlet:renderURL var="homeUrl">
		<portlet:param name="myaction" value="showHome" />
	</liferay-portlet:renderURL>

	<div style="display:none">
		<form id="submitThis" action="javascript:$(this).modal3({width:800, height:400, message:true, redirect:'${homeUrl}', src: '${popUpUrl}'}).open(); return false;"></form>
	</div>

	<script>
		$(this).modal3({width:800, height:400, message:true, redirect:'${homeUrl}', src: '${popUpUrl}'}).open();
	</script>
</c:if>