

<%@ page import="complaintRequest.ComplaintRequest" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'complaintRequest.label', default: 'ComplaintRequest')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
			<div class="nav" role="navigation">
			 <ul>   
          <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	        <li><g:link class="list" controller="task" action="myTaskList"><g:message code="myTasks.label" default="My Tasks ({0})" args="[myTasksCount]" /></g:link></li>
          <li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
			  </ul>
			</div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${complaintRequestInstance}">
            <div class="errors">
                <g:renderErrors bean="${complaintRequestInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="description"><g:message code="complaintRequest.description.label" default="Description" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: complaintRequestInstance, field: 'description', 'errors')}">
                                    <g:textField name="description" value="${complaintRequestInstance?.description}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="solution"><g:message code="complaintRequest.solution.label" default="Solution" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: complaintRequestInstance, field: 'solution', 'errors')}">
                                    <g:textField name="solution" value="${complaintRequestInstance?.solution}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="solutionOK"><g:message code="complaintRequest.solutionOK.label" default="Solution OK" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: complaintRequestInstance, field: 'solutionOK', 'errors')}">
                                    <g:checkBox name="solutionOK" value="${complaintRequestInstance?.solutionOK}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                    <span class="button"><g:submitButton name="complete" class="save" value="${message(code: 'default.button.complete.label', default: 'Complete')}" /></span>
                </div>
                <g:hiddenField name="taskId" value="${params.taskId}" />
            </g:form>
        </div>
    </body>
</html>
