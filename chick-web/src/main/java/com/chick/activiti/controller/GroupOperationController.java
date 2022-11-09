package com.chick.activiti.controller;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.NativeHistoricTaskInstanceQuery;
import org.activiti.engine.task.Task;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName GroupOperationController
 * @Author xiaokexin
 * @Date 2022-08-01 17:20
 * @Description GroupOperationController
 * @Version 1.0
 */
@RestController
public class GroupOperationController {
    /**
     * @return
     * @Author xkx
     * @Description 查询组任务
     * @Date 2022-08-01 14:34
     * @Param
     **/
    @RequestMapping("/findGroupTaskList")
    public void findGroupTaskList() {
        String key = "processId";//流程实例id
        String candidateUser = "candidateUser"; //候选人
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 获取 taskService
        TaskService taskService = processEngine.getTaskService();
        // 3、获取组任务
        List<Task> tasks = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskCandidateUser(candidateUser)
                .list();
        for (Task task : tasks) {
            System.out.println("====================");
            System.out.println("流程实例id" + task.getId());
            System.out.println("任务id" + task.getName());
            System.out.println("任务负责人" + task.getAssignee());
        }
    }

    /**
     * @return
     * @Author xkx
     * @Description 拾取任务
     * @Date 2022-08-01 14:34
     * @Param
     **/
    @RequestMapping("/claimTask")
    public void claimTask() {
        String taskId = "taskId";//流程实例id
        String candidateUser = "candidateUser"; //候选人
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 获取 taskService
        TaskService taskService = processEngine.getTaskService();
        // 3、获取组任务
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskCandidateUser(candidateUser)
                .singleResult();
        if (task != null) {
            taskService.claim(taskId, candidateUser);
            System.out.println("taskId-" + taskId + "-用户-" + candidateUser + "-拾取任务完成");
        }
    }

    /**
     * @return
     * @Author xkx
     * @Description 归还
     * @Date 2022-08-01 14:34
     * @Param
     **/
    @RequestMapping("/assigneeToGroupTask")
    public void assigneeToGroupTask() {
        String taskId = "taskId";//流程实例id
        String assignee = "assignee"; //负责人
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 获取 taskService
        TaskService taskService = processEngine.getTaskService();
        // 3、获取组任务
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskAssignee(assignee)
                .singleResult();
        if (task != null) {
            // 4、归还任务就是把负责人设置为空
            taskService.setAssignee(taskId, null);
            System.out.println("taskId-" + taskId + "-用户-" + assignee + "-归还完成");
        }
    }

    /**
     * @return
     * @Author xkx
     * @Description 归还
     * @Date 2022-08-01 14:34
     * @Param
     **/
    @RequestMapping("/assigneeToCandidateUser")
    public void assigneeToCandidateUser() {
        String taskId = "taskId";//流程实例id
        String assignee = "assignee"; //负责人
        String candidateUser = "candidateUser"; //候选人
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 获取 taskService
        TaskService taskService = processEngine.getTaskService();
        // 3、获取组任务
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskAssignee(assignee)
                .singleResult();
        if (task != null) {
            // 4、交接任务就是把负责人设置为其他候选人
            taskService.setAssignee(taskId, candidateUser);
            System.out.println("taskId-" + taskId + "-用户-" + assignee + "-交接给" + candidateUser + "完成");
        }
    }

    /**
     * @return
     * @Author xkx
     * @Description 查询个人任务
     * @Date 2022-08-01 14:34
     * @Param
     **/
    @RequestMapping("/findOwnTaskList")
    public void findOwnTaskList() {
        String key = "processId";//流程实例id
        String candidateUser = "candidateUser"; //候选人
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 获取 runtimeService
        TaskService taskService = processEngine.getTaskService();
        // 3. 根据流程Key 和 任务的负责人查询任务
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey(key)   //流程key
                .taskAssignee(candidateUser)    //要查询的负责人
                .list();
        for (Task task : list) {
            System.out.println("流程实例ID= " + task.getProcessInstanceId());
            System.out.println("任务ID= " + task.getId());
            System.out.println("任务负责人= " + task.getAssignee());
            System.out.println("任务名称= " + task.getName());
        }
    }

    /**
     * @return
     * @Author xkx
     * @Description 查询个人任务
     * @Date 2022-08-01 14:34
     * @Param
     **/
    @RequestMapping("/completeOwnTask")
    public void completeOwnTask() {
        String key = "processId";//流程实例id
        String candidateUser = "candidateUser"; //候选人
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 获取 taskService
        TaskService taskService = processEngine.getTaskService();
        // 3. 根据流程Key 和 任务的负责人查询任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskAssignee(candidateUser)
                .singleResult();
        // 4、完成个人任务
        if (task != null) {
            taskService.complete(task.getId());
        }
    }
}
