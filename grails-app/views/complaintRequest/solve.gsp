

<%@ page import="complaintRequest.ComplaintRequest"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<g:set var="entityName"
	value="${message(code: 'complaintRequest.label', default: 'ComplaintRequest')}" />
<title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
	<div class="nav" role="navigation">
		<ul>
			<li><a class="home" href="${createLink(uri: '/')}"><g:message
						code="default.home.label" /></a></li>
			<li><g:link class="list" controller="task" action="myTaskList">
					<g:message code="myTasks.label" default="My Tasks ({0})"
						args="[myTasksCount]" />
				</g:link></li>
			<li><g:link class="list" action="list">
					<g:message code="default.list.label" args="[entityName]" />
				</g:link></li>
			<li><g:link class="create" action="start">
					<g:message code="default.new.label" args="[entityName]" />
				</g:link></li>
		</ul>
	</div>
	<div class="body">
		<h1>
			<g:message code="default.edit.label" args="[entityName]" />
		</h1>
		<g:if test="${flash.message}">
			<div class="message">
				${flash.message}
			</div>
		</g:if>
		<g:hasErrors bean="${complaintRequestInstance}">
			<div class="errors">
				<g:renderErrors bean="${complaintRequestInstance}" as="list" />
			</div>
		</g:hasErrors>
		<g:form method="post">
			<g:hiddenField name="id" value="${complaintRequestInstance?.id}" />
			<g:hiddenField name="version"
				value="${complaintRequestInstance?.version}" />
			<g:hiddenField name="taskId" value="${params.taskId}" />
			<div class="dialog">
				<table>
					<tbody>

						<tr class="prop">
							<td valign="top" class="name"><label for="description"><g:message
										code="complaintRequest.description.label"
										default="Description" /></label></td>
							<td valign="top"
								class="value ${hasErrors(bean: complaintRequestInstance, field: 'description', 'errors')}">
								<g:textField name="description"
									value="${complaintRequestInstance?.description}" />
							</td>
						</tr>

						<tr class="prop">
							<td valign="top" class="name"><label for="solution"><g:message
										code="complaintRequest.solution.label" default="Solution" /></label>
							</td>
							<td valign="top"
								class="value ${hasErrors(bean: complaintRequestInstance, field: 'solution', 'errors')}">
								<g:textField name="solution"
									value="${complaintRequestInstance?.solution}" />
							</td>
						</tr>
					</tbody>
				</table>
				<h1>History information:</h1>
				<div style="margin: 0px; padding; 0px">
					<ul>
						<g:each var="detail" in="${myProgress}">
							<li>
								${detail.getActivityName()} from <b>${detail.getStartTime()}</b> to <b>${detail.getEndTime()}</b>
							</li>
						</g:each>
					</ul>
				</div>
			</div>
			<div class="buttons">
				<span class="button"><g:actionSubmit class="save"
						action="update"
						value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
				<span class="button"><g:actionSubmit class="save"
						action="update"
						value="${message(code: 'default.button.complete.label', default: 'Complete')}" /></span>
				<span class="button"><g:actionSubmit class="delete"
						action="delete"
						value="${message(code: 'default.button.delete.label', default: 'Delete')}"
						onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
			</div>
		</g:form>
	</div>
</body>
</html>
