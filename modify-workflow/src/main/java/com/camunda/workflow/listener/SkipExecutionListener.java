package com.camunda.workflow.listener;

import java.util.List;
import java.util.ListIterator;

import org.camunda.bpm.engine.AuthorizationException;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.impl.pvm.PvmActivity;
import org.camunda.bpm.engine.impl.pvm.PvmTransition;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.camunda.bpm.engine.impl.pvm.process.TransitionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class SkipExecutionListener<E> implements ExecutionListener{

	@Autowired
	RuntimeService runtimeService;
	
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
			RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();
			
			TaskService taskService = execution.getProcessEngineServices()
					.getTaskService();
			
			
		try{
			String outTransitionId = null;
				PvmActivity activity = ((ActivityExecution)execution).getActivity();
				List<PvmTransition> outgoingTransitions =   (activity.getOutgoingTransitions());
				
				if(null!= outgoingTransitions && !outgoingTransitions.isEmpty()){
					ListIterator<E> itr = (ListIterator<E>) outgoingTransitions.listIterator();
					
					while(itr.hasNext()){
						TransitionImpl outTransition = (TransitionImpl) itr.next();
						//TODO check outTransition for default flow , then condition of forward execution
						if(null != outTransition){
							outTransitionId =  outTransition.getId();
							break;
						}
					}
				}
				try {
					runtimeService.createProcessInstanceModification(execution.getProcessInstanceId())
					.startTransition(outTransitionId)
					.cancelActivityInstance(execution.getActivityInstanceId())
					.execute();

			      }  catch (AuthorizationException e) {
			        throw e;
			      }
			} catch (Exception e){
				throw e;
			}
			
		}
}
