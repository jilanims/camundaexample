package com.camunda.workflow.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ReloadDelegate implements JavaDelegate{
	
	final static Logger logger = LoggerFactory.getLogger(ReloadDelegate.class);
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String taskName = (String) execution.getVariable("reload");
		
		logger.info("ReloadDelegate task started for "+execution.getCurrentActivityName());
		if(taskName!=null && taskName.equals("non-pay-account-task-1")) {
			taskName = (String)execution.getVariable("task");
			logger.info("TaskName:"+taskName);
		}
		
	}

}
