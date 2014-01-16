package complaintRequest

class ComplaintRequestController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    static activiti = true

    def index = {
        redirect(action: "list", params: params)
    }

    def start = {
        start(params)
    }
	
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
        def complaintRequestInstance = ComplaintRequest.get(params.id)
        if (complaintRequestInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (complaintRequestInstance.version > version) {
                    
                    complaintRequestInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'complaintRequest.label', default: 'ComplaintRequest')] as Object[], "Another user has updated this ComplaintRequest while you were editing")
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
}
