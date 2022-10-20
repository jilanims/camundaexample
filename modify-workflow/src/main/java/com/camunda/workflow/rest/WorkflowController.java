package com.camunda.workflow.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.camunda.workflow.model.WorkflowRequestModel;

@RestController
@RequestMapping("/workflow/modify")
public class WorkflowController {
	
	@Autowired RuntimeService runtimeService;
	
	@PostMapping("/updateOrderWF")
	@Consumes(MediaType.APPLICATION_JSON)
    public String updateOrder(@RequestBody WorkflowRequestModel workflowRequestModel) {
		
		//runtimeService.suspendProcessInstanceById(workflowRequestModel.processInstanceId);
		
		//ActivityInstance activityInstance = runtimeService.getActivityInstance(workflowRequestModel.processInstanceId);
		
		
		runtimeService.createProcessInstanceModification(workflowRequestModel.processInstanceId)
		.startBeforeActivity("reload_task_decision_task_id")
		.setVariables(workflowRequestModel.variables)
		.execute();
		
       return "Order updated successfully";
    }
	
	
	@PostMapping("/genericUpdateOrderWF")
	@Consumes(MediaType.APPLICATION_JSON)
    public String genericUpdateOrder(@RequestBody WorkflowRequestModel workflowRequestModel) {
		
		//runtimeService.suspendProcessInstanceById(workflowRequestModel.processInstanceId);
		
		//ActivityInstance activityInstance = runtimeService.getActivityInstance(workflowRequestModel.processInstanceId);
		
		
		runtimeService.createProcessInstanceModification(workflowRequestModel.processInstanceId)
		.startBeforeActivity(workflowRequestModel.startActivityId)
		.setVariables(workflowRequestModel.variables)
		.execute();
		
       return "Order updated successfully";
    }
	
	
	private void getActiveIds(String processId, String currentActivityId) {
		List<String> activityIds = runtimeService.getActiveActivityIds(processId);
		for(String activityId : activityIds) {
			if (!activityId.equalsIgnoreCase(currentActivityId)) {
				//cancel activityId
				runtimeService.createProcessInstanceModification(processId).cancelAllForActivity(activityId).execute();
			}
		}
	}

}
