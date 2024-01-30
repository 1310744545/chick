package com.chick.activiti.controller;

import com.chick.base.R;
import com.chick.controller.BaseController;
import lombok.SneakyThrows;
import org.activiti.api.process.runtime.ProcessAdminRuntime;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.*;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * @ClassName testcontroller
 * @Author xiaokexin
 * @Date 2022-07-28 17:17
 * @Description testcontroller
 * @Version 1.0
 */
@RestController
public class BaseOperateController extends BaseController {

    @Resource
    private ProcessAdminRuntime processAdminRuntime;

    @Resource
    private RuntimeService runtimeService;

    /**
     * @return com.chick.base.R
     * insert：
     * ACT_RE_DEPLOYMENT：流程部署表，每部署一个就增加一个2131
     * ACT_RE_PROCDEF：流程定义表，存放我们定义的流程信息，KEY是唯一标识
     * ACT_GE_BYTEARRAY：流程资源表，存放流程部署文件和图片文件123
     * update：
     * ACT_GE_PROPERTY：每次操作都有更新，GE是通用表
     * @Author xkx
     * @Description 部署流程
     * @Date 2022-08-01 10:51
     * @Param []
     **/
    @SneakyThrows
    @PostMapping("/activiti/deploy")
    public R deploy() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 获取 RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3. 获取输入流
        String filePath = "D:\\IdeaProjects\\chick\\chick-web\\src\\main\\resources\\processes\\Demo.bpmn20.xml";
        FileInputStream fileIns = new FileInputStream(filePath);
        // 4. 使用 service 进行流程部署
        Deployment deploy = repositoryService.createDeployment()
                .name("请假申请流程")
                .addInputStream("bpmn/Demo.bpmn20.xml", fileIns)
                .deploy();

        System.out.println(deploy);
        return R.ok();
    }

    /**
     * @return com.chick.base.R
     * insert：
     * act_hi_ actinst：流程实例执行历史
     * act_ hi_ identitylink：流程的参与用户信息历史
     * act_ hi_ procinst：流程实例历史信息
     * act_ hi_ taskinst：流程任务历史信息
     * act_ ru_ execut ion：流程正在执行信息
     * act_ ru_ ldentitylink：流程的参与用户信息
     * act_ ru_ task：任务信息
     * update：
     * ACT_GE_PROPERTY：每次操作都有更新，GE是通用表
     * @Author xkx
     * @Description 流程启动
     * @Date 2022-08-01 10:52
     * @Param []
     **/
    @SneakyThrows
    @PostMapping("/activiti/start")
    public R start() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 1. 获取 runtimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 2. 启动
        ProcessInstance demo = runtimeService.startProcessInstanceByKey("Demo");

        System.out.println("流程定义ID" + demo.getProcessDefinitionId());
        System.out.println("流程实例ID" + demo.getId());
        System.out.println("当前活动的ID" + demo.getActivityId());
        return R.ok();
    }

    /**
     * @return com.chick.base.R
     * @Author xkx
     * @Description 查询个人待执行的任务
     * @Date 2022-08-01 10:53
     * @Param []
     * select：
     * act_ru_task：当前任务表，进行查询
     **/
    @SneakyThrows
    @PostMapping("/activiti/findPersonTask")
    public R findPersonTask() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 1. 获取 taskService
        TaskService taskService = processEngine.getTaskService();
        // 2. 根据流程Key 和 任务的负责人查询任务
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey("Dome")   //流程key
                .taskAssignee("q1310744545")    //要查询的负责人
                .list();
        for (Task task : list) {
            System.out.println("流程实例ID= " + task.getProcessInstanceId());
            System.out.println("任务ID= " + task.getId());
            System.out.println("任务负责人= " + task.getAssignee());
            System.out.println("任务名称= " + task.getName());
        }
        return R.ok();
    }

    /**
     * @return com.chick.base.R
     * insert：
     * ACT_ HI_ TASKIHST
     * ACT_ HI_ACTINST
     * ACT_ HI_ IDENTITYLINK
     * ACT_RU _TASK
     * ACT_ RU_ IDENTITYLINK
     * update：
     * ACT_ HI_TASKINST -- 2505
     * ACT_ RU_ EXECUTION -- 1d=2502 - rev =1
     * ACT_ HI_ACTINST -- 1d-2504
     * delete：
     * ACT_ RU_ TASK DELETE 	-- 1d =
     * @Author xkx
     * @Description 完成任务
     * @Date 2022-08-01 11:07
     * @Param []
     **/
    @SneakyThrows
    @PostMapping("/activiti/completeTask")
    public R completeTask() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 1. 获取 taskService
        TaskService taskService = processEngine.getTaskService();
        // 2. 根据任务id完成任务
        taskService.complete("taskId");
        // 当完成最后一个环节后 会结束流程
        return R.ok();
    }

    /**
     * @return com.chick.base.R
     * @Author xkx
     * @Description 通过zip方式部署
     * @Date 2022-08-01 13:20
     * @Param []
     **/
    @SneakyThrows
    @PostMapping("/activiti/deployProcessByZip")
    public R deployProcessByZip() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 1. 获取 RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 2. 流程部署
        // getResourceAsStream 获取resource目录
        InputStream path = this.getClass().getClassLoader().getResourceAsStream("path");
        ZipInputStream zipInputStream = new ZipInputStream(path);
        repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();
        return R.ok();
    }

    /**
     * @return
     * @Author xkx
     * @Description 查询流程定义
     * @Date 2022-08-01 13:20
     * @Param
     **/
    @SneakyThrows
    @PostMapping("/activiti/queryProcessDefinition")
    public R queryProcessDefinition() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 1. 获取 RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 2. 获取 processDefinitionQuery 对象
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        // 2. 查询流程定义
        List<ProcessDefinition> processDefinitions = processDefinitionQuery.processDefinitionKey("key")
                .orderByProcessDefinitionVersion()  // 通过version排序
                .desc() //倒序
                .list(); //获取list
        for (ProcessDefinition processDefinition : processDefinitions) {
            System.out.println("流程定义ID" + processDefinition.getId());
            System.out.println("流程定义名称" + processDefinition.getName());
            System.out.println("流程定义key" + processDefinition.getKey());
            System.out.println("流程定义版本" + processDefinition.getVersion());
            System.out.println("流程部署id" + processDefinition.getDeploymentId());
        }
        return R.ok();
    }

    /**
     * delete:
     * act_ge_bytearray
     * act_re_deployment
     * act_re_procdef
     *
     * @Author xkx
     * @Description 删除流程定义
     * @Date 2022-08-01 13:20
     * @Param
     **/
    @SneakyThrows
    @PostMapping("/activiti/deleteProcessDefinition")
    public R deleteProcessDefinition() {
        String deploymentId = "1";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 1. 获取 RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 2 普通删除 有流程正在执行无法删除
        repositoryService.deleteDeployment(deploymentId);
        // 3 级联删除 有流程正在执行也会删除
        repositoryService.deleteDeployment(deploymentId, true);
        return R.ok();
    }

    /**
    * @Author xkx
    * @Description 下载资源文件
    * @Date 2022-08-01 13:40
    * @Param
    * @return
     * 方案1： 使用Activiti提供的api，来下载文件
     * 方案2： 自己写代码冲数据库中下载文件，使用jdbc对blob类型，clob类型的数据读取处理出来，保存到文件目录
     *
    **/
    @SneakyThrows
    @PostMapping("/activiti/getDeployment")
    public R getDeployment() {
        // 1、得到引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取api，RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3、获取查询对象ProcessDefinitionQuery，查询流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("Demo")
                .singleResult();
        // 4、通过流程定义信息，获取部署ID
        String deploymentId = processDefinition.getDeploymentId();
        // 5、通过RepositoryService，传递部署id参数，读取资源信息（png和bpmn）
            // 5.1 获取图片流
        String diagramResourceName = processDefinition.getDiagramResourceName();
        InputStream pngInput = repositoryService.getResourceAsStream(deploymentId, diagramResourceName);
            // 5.2 获取bpmn流
        String resourceName = processDefinition.getResourceName();
        InputStream bpmnInput = repositoryService.getResourceAsStream(deploymentId, resourceName);
        // 6、构造OutputStream流
        File pngFile = new File("path.png");
        File bpmnFile = new File("path.bpmn");
        FileOutputStream pngOutputStream = new FileOutputStream(pngFile);
        FileOutputStream bpmnOutputStream = new FileOutputStream(bpmnFile);
        // 7、输入、输出流转换
        IOUtils.copy(pngInput, pngOutputStream);
        IOUtils.copy(bpmnInput, bpmnOutputStream);
        // 8、关闭流
        pngInput.close();
        bpmnInput.close();
        pngOutputStream.close();
        bpmnOutputStream.close();
        return R.ok();
    }

    /**
     * @Author xkx
     * @Description 查看历史信息
     * @Date 2022-08-01 13:20
     * @Param
     **/
    @SneakyThrows
    @PostMapping("/activiti/findHistoryInfo")
    public R findHistoryInfo() {
        // 1、得到引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取api，historyService
        HistoryService historyService = processEngine.getHistoryService();
        // 3、获取查询对象
        HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService.createHistoricActivityInstanceQuery();
        // 4、查询actinst表
        historicActivityInstanceQuery.processInstanceId("instanceId");
        historicActivityInstanceQuery.processDefinitionId("definitionId");
        // 增加排序
        historicActivityInstanceQuery.orderByHistoricActivityInstanceStartTime().asc();
        // 5、查询所有内容
        List<HistoricActivityInstance> list = historicActivityInstanceQuery.list();
        for (HistoricActivityInstance hi : list){
            System.out.println(hi.getActivityId());
            System.out.println(hi.getActivityName());
            System.out.println(hi.getProcessDefinitionId());
            System.out.println(hi.getProcessInstanceId());
        }
        return R.ok();
    }
}
