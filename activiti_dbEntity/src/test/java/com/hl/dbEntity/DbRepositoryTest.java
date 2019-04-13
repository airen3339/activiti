package com.hl.dbEntity;

import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ByteArrayEntity;
import org.activiti.engine.impl.persistence.entity.ByteArrayEntityImpl;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbRepositoryTest {
    
    private static final Logger logger = LoggerFactory.getLogger(DbRepositoryTest.class);
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti-mysql.cfg.xml");

    @Test
    public void testDeploy(){
        activitiRule.getRepositoryService().createDeployment()
                .name("二级审批流程")
                .addClasspathResource("com/hl/second_approve.bpmn20.xml")
                .deploy();
    }

    @Test
    public void testSuspend(){
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        repositoryService.suspendProcessDefinitionById("second_approve:2:7504");

        boolean processDefinitionSuspended = repositoryService.isProcessDefinitionSuspended("second_approve:2:7504");
        logger.info("processDefinitionSuspended={}",processDefinitionSuspended);
    }
}
