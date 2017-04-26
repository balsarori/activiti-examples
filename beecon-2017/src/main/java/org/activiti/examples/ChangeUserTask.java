package org.activiti.examples;

import org.activiti.engine.DynamicBpmnService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.activiti.engine.task.Task;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class ChangeUserTask {
	public static void main(String[] args) throws InterruptedException {
		// Build process enigne
		ProcessEngine processEngine = new StandaloneInMemProcessEngineConfiguration()
										// enable process definition info cache
										 .setEnableProcessDefinitionInfoCache(true)
										 .buildProcessEngine();
	    
	    RepositoryService repositoryService = processEngine.getRepositoryService();
	    RuntimeService runtimeService = processEngine.getRuntimeService();
	    
	    // deploy process definition
	    repositoryService.createDeployment()
	      .addClasspathResource("dynamic-user-task.bpmn20.xml")
	      .deploy();
	    
	    String processDefinitionId = repositoryService.createProcessDefinitionQuery()
										.singleResult().getId();
	    
	    // change user task candidate groups
	    DynamicBpmnService dynamicBpmnService = processEngine.getDynamicBpmnService();
	    ObjectNode infoNode = dynamicBpmnService
	    					.changeUserTaskCandidateGroup("userTask", 
	    							"management", true);
	    
	    dynamicBpmnService.saveProcessDefinitionInfo(processDefinitionId, infoNode);
	    
	    // start a process instance
	    runtimeService.startProcessInstanceByKey("dynamic-user-task");

	    TaskService taskService = processEngine.getTaskService();
	    
	    // tasks claimable by users group
	    long count = taskService.createTaskQuery()
				.taskCandidateGroup("users").count();
	    
	    System.out.println("tasks claimable by users group " + count);
	    
	    // fetch the task (tasks claimable by management group)
	    Task task = taskService.createTaskQuery()
	    				.taskCandidateGroup("management")
	    				.singleResult();
	    
	    // claim task and complete
	    taskService.claim(task.getId(), "manager");
	    taskService.complete(task.getId());
	    
	  }

}
