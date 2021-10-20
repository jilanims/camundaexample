# camundaexample

Camunda user Task, pre and post listeners

https://github.com/camunda/camunda-bpm-examples/tree/master/process-engine-plugin/bpmn-parse-listener-on-user-task

Create a Process Engine Plugin Implementation
Extend the org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin abstract class:

public class InformAssigneeParseListenerPlugin extends AbstractProcessEnginePlugin {

  @Override
  public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
    List<BpmnParseListener> preParseListeners = processEngineConfiguration.getCustomPreBPMNParseListeners();
    if(preParseListeners == null) {
      preParseListeners = new ArrayList<BpmnParseListener>();
      processEngineConfiguration.setCustomPreBPMNParseListeners(preParseListeners);
    }
    preParseListeners.add(new InformAssigneeParseListener());
  }

}

---

Create a BPMN Parse Listener Implementation
Extend the org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener abstract class:

public class InformAssigneeParseListener extends AbstractBpmnParseListener {

  @Override
  public void parseUserTask(Element userTaskElement, ScopeImpl scope, ActivityImpl activity) {
    ActivityBehavior activityBehavior = activity.getActivityBehavior();
    if(activityBehavior instanceof UserTaskActivityBehavior ){
      UserTaskActivityBehavior userTaskActivityBehavior = (UserTaskActivityBehavior) activityBehavior;
      userTaskActivityBehavior
        .getTaskDefinition()
        .addTaskListener("assignment", InformAssigneeTaskListener.getInstance());
    }
  }
}

--


Create a Task Listener Implementation
Implement the org.camunda.bpm.engine.delegate.TaskListener interface:

public class InformAssigneeTaskListener implements TaskListener {

  private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
  public static List<String> assigneeList = new ArrayList<String>();

  private static InformAssigneeTaskListener instance = null;

  protected InformAssigneeTaskListener() { }

  public static InformAssigneeTaskListener getInstance() {
    if(instance == null) {
      instance = new InformAssigneeTaskListener();
    }
    return instance;
  }

  public void notify(DelegateTask delegateTask) {
    String assignee = delegateTask.getAssignee();
    assigneeList.add(assignee);
    LOGGER.info("Hello " + assignee + "! Please start to work on your task " + delegateTask.getName());
  }

}


---


Activate the BPMN Parse Listener Plugin
The BPMN Parse Listener can be activated in the camunda.cfg.xml:

<!-- activate bpmn parse listener as process engine plugin -->
<property name="processEnginePlugins">
  <list>
    <bean class="org.camunda.bpm.example.parselistener.InformAssigneeParseListenerPlugin" />
  </list>
</property>

