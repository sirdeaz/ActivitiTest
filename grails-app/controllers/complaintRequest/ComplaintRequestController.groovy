package complaintRequest

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService
import org.activiti.engine.TaskService
import org.activiti.engine.impl.RepositoryServiceImpl
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity

class ComplaintRequestController {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	static activiti = true

	HistoryService historyService
	RuntimeService runtimeService
	TaskService taskService
	RepositoryService repositoryService

	def index = {
		redirect(action: "list", params: params)
	}

	def start = { start(params) }

	def list = {
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		[complaintRequestInstanceList: ComplaintRequest.list(params),
			complaintRequestInstanceTotal: ComplaintRequest.count(),
			myTasksCount: assignedTasksCount]
	}

	def create = {
		def complaintRequestInstance = new ComplaintRequest()
		complaintRequestInstance.properties = params
		return [complaintRequestInstance: complaintRequestInstance,
			myTasksCount: assignedTasksCount]
	}

	def save = {
		def complaintRequestInstance = new ComplaintRequest(params)
		if (complaintRequestInstance.save(flush: true)) {
			flash.message = "${message(code: 'default.created.message', args: [message(code: 'complaintRequest.label', default: 'ComplaintRequest'), complaintRequestInstance.id])}"
			params.id = complaintRequestInstance.id
			if (params.complete) {
				completeTask(params)
			} else {
				params.action="show"
				saveTask(params)
			}
			redirect(action: "show", params: params)
		}
		else {
			render(view: "create", model: [complaintRequestInstance: complaintRequestInstance, myTasksCount: assignedTasksCount])
		}
	}

	def solve = {
		def processInstanceId = taskService.createTaskQuery().taskId(params.taskId).singleResult().processInstanceId
		
		def details = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list()

		def complaintRequestInstance = ComplaintRequest.get(params.id)
		if (!complaintRequestInstance) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'complaintRequest.label', default: 'ComplaintRequest'), params.id])}"
			redirect(controller: "task", action: "myTaskList")
		}
		else {
			[complaintRequestInstance: complaintRequestInstance, myTasksCount: assignedTasksCount, myProgress: details]
		}
	}

	def verify = {
		def complaintRequestInstance = ComplaintRequest.get(params.id)
		if (!complaintRequestInstance) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'complaintRequest.label', default: 'ComplaintRequest'), params.id])}"
			redirect(controller: "task", action: "myTaskList")
		}
		else {
			[complaintRequestInstance: complaintRequestInstance, myTasksCount: assignedTasksCount]
		}
	}

	def show = {
		def complaintRequestInstance = ComplaintRequest.get(params.id)
		if (!complaintRequestInstance) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'complaintRequest.label', default: 'ComplaintRequest'), params.id])}"
			redirect(controller: "task", action: "myTaskList")
		}
		else {
			[complaintRequestInstance: complaintRequestInstance, myTasksCount: assignedTasksCount]
		}
	}

	def edit = {
		def complaintRequestInstance = ComplaintRequest.get(params.id)
		if (!complaintRequestInstance) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'complaintRequest.label', default: 'ComplaintRequest'), params.id])}"
			redirect(controller: "task", action: "myTaskList")
		}
		else {
			[complaintRequestInstance: complaintRequestInstance, myTasksCount: assignedTasksCount]
		}
	}

	def update = {
		params.solutionOK = params.solutionOK ? true : false

		def complaintRequestInstance = ComplaintRequest.get(params.id)
		if (complaintRequestInstance) {
			if (params.version) {
				def version = params.version.toLong()
				if (complaintRequestInstance.version > version) {

					complaintRequestInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [
						message(code: 'complaintRequest.label', default: 'ComplaintRequest')] as Object[], "Another user has updated this ComplaintRequest while you were editing")
					render(view: "edit", model: [complaintRequestInstance: complaintRequestInstance, myTasksCount: assignedTasksCount])
					return
				}
			}
			complaintRequestInstance.properties = params
			if (!complaintRequestInstance.hasErrors() && complaintRequestInstance.save(flush: true)) {
				flash.message = "${message(code: 'default.updated.message', args: [message(code: 'complaintRequest.label', default: 'ComplaintRequest'), complaintRequestInstance.id])}"
				Boolean isComplete = params["_action_update"].equals(message(code: 'default.button.complete.label', default: 'Complete'))
				if (isComplete) {
					completeTask(params)
				} else {
					params.action="show"
					saveTask(params)
				}
				redirect(action: "show", id: complaintRequestInstance.id, params: [taskId:params.taskId, complete:isComplete?:null])
			}
			else {
				render(view: "edit", model: [complaintRequestInstance: complaintRequestInstance, myTasksCount: assignedTasksCount])
			}
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'complaintRequest.label', default: 'ComplaintRequest'), params.id])}"
			redirect(controller: "task", action: "myTaskList")
		}
	}

	def delete = {
		def complaintRequestInstance = ComplaintRequest.get(params.id)
		if (complaintRequestInstance) {
			try {
				complaintRequestInstance.delete(flush: true)
				deleteTask(params.taskId)
				flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'complaintRequest.label', default: 'ComplaintRequest'), params.id])}"
				redirect(controller: "task", action: "myTaskList")
			}
			catch (org.springframework.dao.DataIntegrityViolationException e) {
				flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'complaintRequest.label', default: 'ComplaintRequest'), params.id])}"
				redirect(action: "show", id: params.id, params: params)
			}
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'complaintRequest.label', default: 'ComplaintRequest'), params.id])}"
			redirect(controller: "task", action: "myTaskList")
		}
	}

	//FIXME: graphical overview for progress
	def viewImage = {

		String processDefinitionId = taskService.createTaskQuery().taskId(params.taskId).singleResult()
				.getProcessDefinitionId();
		String processInstanceId = taskService.createTaskQuery().taskId(params.taskId).singleResult()
				.getProcessInstanceId();
				
		BpmnModel model = repositoryService.getBpmnModel(processDefinitionId)
		println "information: ${processDefinitionId} ${processInstanceId} ${model}"
		InputStream imageStream = ProcessDiagramGenerator.generateDiagram(model, "png", runtimeService.getActiveActivityIds(processInstanceId));
		
		response.outputStream << imageStream
	}
}
