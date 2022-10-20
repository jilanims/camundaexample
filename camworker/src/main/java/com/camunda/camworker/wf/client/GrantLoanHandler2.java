package com.camunda.camworker.wf.client;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@ExternalTaskSubscription("loanGranter2") // create a subscription for this topic name
public class GrantLoanHandler2 implements ExternalTaskHandler {

  @Override
  public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
    // get variables
    String customerId = externalTask.getVariable("customerId");
    int creditScore = externalTask.getVariable("creditScore");
    Map<String, Object> variables = new HashMap<>();
    if(externalTask.getVariable("restApiStatus")==null) {
	   
	    //variables.put("restApiStatus", "error");
	    // we could call an external service to create the loan documents here
	   // externalTaskService.setVariables(externalTask, variables);
	 
    }

    // complete the external task
    externalTaskService.complete(externalTask, variables);

    Logger.getLogger("loanGranter")
        .log(Level.INFO, "Loan for customer {0} with credit score {1} granted!", new Object[]{customerId, creditScore});
  }

}
