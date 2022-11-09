package com.chick.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @ClassName MyTaskListener
 * @Author xiaokexin
 * @Date 2022-08-01 15:12
 * @Description MyTaskListener
 * @Version 1.0
 */
public class MyTaskListener implements TaskListener {
    /**
     * @return void
     * @Author xkx
     * @Description 通过监听指定负责人
     * @Date 2022-08-01 15:13
     * @Param [delegateTask]
     **/
    @Override
    public void notify(DelegateTask delegateTask) {
        // 如果任务是创建申请且为create，则设置负责人
        if ("创建申请".equals(delegateTask.getName())
                && "create".equals(delegateTask.getEventName())) {
            delegateTask.setAssignee("张三");
        }
    }
}

