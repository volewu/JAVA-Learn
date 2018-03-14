package com.vole.multiassign;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class MyTaskListener implements TaskListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void notify(DelegateTask delegateTask) {
		delegateTask.addCandidateUser("vole");
		delegateTask.addCandidateUser("gakki");
		delegateTask.addCandidateUser("wuji");//指定办理人
	}

}
