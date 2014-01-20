
<%@ page import="complaintRequest.ComplaintRequest" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'complaintRequest.label', default: 'ComplaintRequest')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
			<div class="nav" role="navigation">
			 <ul> 
            <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	          <li><g:link class="list" controller="task" action="myTaskList"><g:message code="myTasks.label" default="My Tasks ({0})" args="[myTasksCount]" /></g:link></li>
            <li><g:link class="create" action="start"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			  </ul>
			</div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'complaintRequest.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="description" title="${message(code: 'complaintRequest.description.label', default: 'Description')}" />
                        
                            <g:sortableColumn property="solution" title="${message(code: 'complaintRequest.solution.label', default: 'Solution')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${complaintRequestInstanceList}" status="i" var="complaintRequestInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${complaintRequestInstance.id}">${fieldValue(bean: complaintRequestInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: complaintRequestInstance, field: "description")}</td>
                        
                            <td>${fieldValue(bean: complaintRequestInstance, field: "solution")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${complaintRequestInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
