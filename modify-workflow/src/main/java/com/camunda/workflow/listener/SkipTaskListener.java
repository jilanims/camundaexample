package com.camunda.workflow.listener;

import org.camunda.bpm.engine.AuthorizationException;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.stereotype.Component;

@Component
public class SkipTaskListener implements TaskListener{

	@Override
	public void notify(DelegateTask delegateTask) {

			TaskService taskService = delegateTask.getExecution().getProcessEngineServices()
					.getTaskService();
			try {
				ExecutionEntity executionEntity = (ExecutionEntity) delegateTask.getExecution();
				String taskId = executionEntity.getTasks().get(0).getId();
				taskService.complete(taskId);
			}  catch (AuthorizationException e) {
				throw e;
			} catch (NullPointerException ne){
				throw ne;
			}
	}

}
