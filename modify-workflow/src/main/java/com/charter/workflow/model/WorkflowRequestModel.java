package com.charter.workflow.model;

import java.util.Map;

import lombok.Data;

@Data
public class WorkflowRequestModel {
	public String businessKey;
	public String processInstanceId;
	public Map<String,Object> variables;
	public String startActivityId;

}
