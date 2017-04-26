package org.activiti.examples;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class NewServiceTask implements JavaDelegate {

	  public void execute(DelegateExecution execution) {
	      System.out.println("NewServiceTask invoked");
	  }
}
