package com.chick.activiti.controller;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.NativeHistoricTaskInstanceQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.coyote.http11.filters.VoidOutputFilter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * @ClassName AdvancedOperateController
 * @Author xiaokexin
 * @Date 2022-08-01 14:23
 * @Description AdvancedOperateController
 * @Version 1.0
 */
@RestController
public class AdvancedOperateController {

    /**
     * @return void
     * @Author xkx
     * @Description 流程绑定业务id
     * @Date 2022-08-01 14:31
     * @Param []
     **/
    @RequestMapping("/addBusinessKey")
    public void addBusinessKey() {
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取runtimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3、启动流程的过程中，添加businessKey
        // 参数1：流程定义key  参数2：业务id
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Demo", "businessId");
        // 4、输出
        System.out.println("businessKey" + processInstance.getBusinessKey());
    }

    /**
     * @return
     * @Author xkx
     * @Description 全部流程挂起、激活流程  挂起后不能继续处理
     * @Date 2022-08-01 14:34
     * @Param
     **/
    @RequestMapping("/suspendAllProcessInstance")
    public void suspendAllProcessInstance() {
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3、查询流程定义，获取流程定义查询对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("Demo")
                .singleResult();
        // 4、获取当前流程定义的实例是否都是挂起状态
        boolean suspended = processDefinition.isSuspended();
        // 5、获取流程定义的id
        String id = processDefinition.getId();
        if (suspended) {
            // 6、如果是挂起状态，改为激活状态
            // 参数1：流程定义id   参数2：是否激活    参数3：激活时间
            repositoryService.activateProcessDefinitionByKey(id, true, null);
            System.out.println("流程定义ID：" + id + "，已激活");
        } else {
            // 7、如果是激活状态，改为挂起状态
            repositoryService.suspendProcessDefinitionByKey(id, true, null);
            System.out.println("流程定义ID：" + id + "，已挂起");
        }
    }

    /**
     * @return
     * @Author xkx
     * @Description 全部流程挂起、激活流程  挂起后不能继续处理
     * @Date 2022-08-01 14:34
     * @Param
     **/
    @RequestMapping("/suspendSingleProcessInstance")
    public void suspendSingleProcessInstance() {
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取runtimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3、查询流程定义，获取流程实例查询对象
        ProcessInstance instanceId = runtimeService.createProcessInstanceQuery()
                .processInstanceId("instanceId")
                .singleResult();
        // 4、获取当前流程实例的实例是否是挂起状态 true已暂停，false激活
        boolean suspended = instanceId.isSuspended();
        // 5、获取流程实例的id
        String id = instanceId.getId();
        if (suspended) {
            // 6、如果是挂起状态，改为激活状态
            // 参数1：流程定义id   参数2：是否激活    参数3：激活时间
            runtimeService.activateProcessInstanceById(id);
            System.out.println("流程实例ID：" + id + "，已激活");
        } else {
            // 7、如果是激活状态，改为挂起状态
            runtimeService.suspendProcessInstanceById(id);
            System.out.println("流程实例ID：" + id + "，已挂起");
        }
    }

    /**
     * @return
     * @Author xkx
     * @Description 分配任务负责人方式
     * @Date 2022-08-01 14:34
     * @Param
     **/
    @RequestMapping("/startAssigneeUel")
    public void startAssigneeUel() {
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取runtimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3、设定assignee的值
        HashMap<String, Object> assignees = new HashMap<>();
        assignees.put("assignee0", "张三");
        assignees.put("assignee1", "李四");
        assignees.put("assignee2", "王五");
        assignees.put("assignee3", "赵六");
        // 4、启动流程
        runtimeService.startProcessInstanceByKey("Demo", assignees);
    }

    /**
     * @return
     * @Author xkx
     * @Description global-启动设置流程变量、负责人
     * @Date 2022-08-01 14:34
     * @Param
     **/
    @RequestMapping("/startProcessSetVariableGlobal")
    public void startProcessSetVariable() {
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取runtimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3、设置流程变量
        String key = "Demo";
        // 4、流程变量map
        HashMap<String, Object> variables = new HashMap<>();
        // 5、流程变量
        Integer days = 3;
        variables.put("days", days);
        // 6、设置负责人
        variables.put("assignee0", "张三");
        variables.put("assignee1", "李四");
        variables.put("assignee2", "王五");
        variables.put("assignee3", "赵六");
        // 7、启动流程
        runtimeService.startProcessInstanceByKey(key, variables);
    }

    /**
     * @return
     * @Author xkx
     * @Description global-完成时设置流程变量
     * @Date 2022-08-01 14:34
     * @Param
     **/
    @RequestMapping("/completeProcessSetVariableGlobal")
    public void completeProcessSetVariable() {
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 获取 taskService
        TaskService taskService = processEngine.getTaskService();
        // 3、流程变量map
        HashMap<String, Object> variables = new HashMap<>();
        // 4、流程变量
        Integer days = 3;
        variables.put("days", days);
        // 5. 根据任务id完成任务、设置流程变量
        taskService.complete("taskId", variables);
    }

    /**
    * @Author xkx
    * @Description global-通过当前流程实例设置流程变量
    * @Date 2022-08-01 16:31
    * @Param
    * @return
    **/
    @RequestMapping("/byProcessIdSetVariableGlobal")
    public void byProcessIdSetVariable() {
        String processId = "processId";
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 获取 runtimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3、通过流程实例id 设置流程变量
        runtimeService.setVariable(processId, "days", 3);
        // 4. 一次设置多个值
        HashMap<String, Object> variables = new HashMap<>();
        Integer days = 3;
        variables.put("days", days);
        runtimeService.setVariables(processId, variables);
    }

    /**
     * @Author xkx
     * @Description global-通过当前任务实例设置流程变量
     * @Date 2022-08-01 16:31
     * @Param
     * @return
     **/
    @RequestMapping("/byTaskIdSetVariableGlobal")
    public void byTaskIdSetVariable() {
        String taskId = "taskId";
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 获取 taskService
        TaskService taskService = processEngine.getTaskService();
        // 3、通过流程实例id 设置流程变量
        taskService.setVariable(taskId, "days", 3);
        // 4. 一次设置多个值
        HashMap<String, Object> variables = new HashMap<>();
        Integer days = 3;
        variables.put("days", days);
        taskService.setVariables(taskId, variables);
    }

    /**
     * @return
     * @Author xkx
     * @Description local-完成时设置流程变量
     * @Date 2022-08-01 14:34
     * @Param
     **/
    @RequestMapping("/completeProcessSetVariableLocal")
    public void completeProcessSetVariableLocal() {
        String taskId = "taskId";
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 获取 taskService
        TaskService taskService = processEngine.getTaskService();
        // 3、通过流程实例id 设置流程变量
        taskService.setVariableLocal(taskId, "days", 3);
        // 4. 一次设置多个值
        HashMap<String, Object> variables = new HashMap<>();
        Integer days = 3;
        variables.put("days", days);
        taskService.setVariablesLocal(taskId, variables);
        // 5、办理
        taskService.complete(taskId);
    }

    /**
     * @return
     * @Author xkx
     * @Description local-查询历史变量
     * @Date 2022-08-01 14:34
     * @Param
     **/
    @RequestMapping("/getLocalVariable")
    public void getLocalVariable() {
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 获取 historyService
        HistoryService historyService = processEngine.getHistoryService();
        // 3、获取历史任务查询对象
        NativeHistoricTaskInstanceQuery nativeHistoricTaskInstanceQuery = historyService.createNativeHistoricTaskInstanceQuery();
        // 4、查询结果
        List<HistoricTaskInstance> list = nativeHistoricTaskInstanceQuery.list();
        for (HistoricTaskInstance hi : list){
            System.out.println("====================");
            System.out.println("任务id" + hi.getId());
            System.out.println("任务名称" + hi.getName());
            System.out.println("任务负责人" + hi.getAssignee());
            System.out.println("任务local变量" + hi.getTaskLocalVariables());
        }
    }
}
