package com.hl.coreApi;

import com.google.common.collect.Maps;
import org.activiti.engine.FormService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * FormService 解析流程定义中表单项的配置
 *              提交表单的方式驱动用户节点流转
 *              类似于TaskService-->complete
 *               formService-->submit
 */
public class FormServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(FormServiceTest.class);
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
	@Deployment(resources = {"com/hl/my-process-form.bpmn20.xml"})
    public void test () {
        FormService formService = activitiRule.getFormService();
        ProcessDefinition processDefinition = activitiRule.getRepositoryService().createProcessDefinitionQuery().singleResult();

        String startFormKey = formService.getStartFormKey(processDefinition.getId());
        logger.info("startFormKey={}", startFormKey);

        StartFormData startFormData = formService.getStartFormData(processDefinition.getId());
        List<FormProperty> formProperties = startFormData.getFormProperties();
        for (FormProperty formProperty : formProperties) {
            logger.info("formProperty={}", ToStringBuilder.reflectionToString(formProperty,ToStringStyle.JSON_STYLE));
        }

        Map<String, String> properties = Maps.newHashMap();
        properties.put("message", "my test message");
        ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), properties);


        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        TaskFormData taskFormData = formService.getTaskFormData(task.getId());

        List<FormProperty> taskFormDataFormProperties = taskFormData.getFormProperties();
        for (FormProperty taskFormDataFormProperty : taskFormDataFormProperties) {
            logger.info("taskFormDataFormProperty={}", ToStringBuilder.reflectionToString(taskFormDataFormProperty,ToStringStyle.JSON_STYLE));
        }


        Map<String, String> taskProperties = Maps.newHashMap();
        taskProperties.put("yesORno", "y");
        formService.submitTaskFormData(task.getId(),taskProperties);
        Task task1 = activitiRule.getTaskService().createTaskQuery().singleResult();
        logger.info("task1={}", task1);
    }
}

