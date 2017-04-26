package org.activiti.examples;

import org.activiti.engine.DynamicBpmnService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class ChangeScriptTask {
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
	      .addClasspathResource("dynamic-script-task.bpmn20.xml")
	      .deploy();
	    
	    String processDefinitionId = repositoryService.createProcessDefinitionQuery()
										.singleResult().getId();
	    
	    DynamicBpmnService dynamicBpmnService = processEngine.getDynamicBpmnService();
	    ObjectNode infoNode = dynamicBpmnService
	    					.changeScriptTaskScript("scriptTask", 
	    							"java.lang.System.out.println(\"New output\");");
	    
	    dynamicBpmnService.saveProcessDefinitionInfo(processDefinitionId, infoNode);
	    
	    // start a process instance
	    runtimeService.startProcessInstanceByKey("dynamic-script-task");
	    
	  }

}
