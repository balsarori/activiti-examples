package org.activiti.examples;

import org.activiti.engine.DynamicBpmnService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.activiti.engine.runtime.ProcessInstance;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class ChangeServiceClass {
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
	      .addClasspathResource("dynamic-service-task.bpmn20.xml")
	      .deploy();
	    
	    // this will invoke OldServiceTask
	    ProcessInstance processInstance = 
	    		runtimeService.startProcessInstanceByKey("dynamic-service-task");

	    String processDefinitionId = processInstance.getProcessDefinitionId();
	    
	    // change service task class name
	    System.out.println("==== changing service task class name ====");
	    
	    DynamicBpmnService dynamicBpmnService = processEngine.getDynamicBpmnService();
	    ObjectNode infoNode = dynamicBpmnService
	    					.changeServiceTaskClassName("dynamicServiceTask", 
	    							"org.activiti.examples.NewServiceTask");
	    
	    dynamicBpmnService.saveProcessDefinitionInfo(processDefinitionId, infoNode);
	    
	    // this will invoke NewServiceTask
	    processInstance = runtimeService.startProcessInstanceByKey("dynamic-service-task");
	  }

}
