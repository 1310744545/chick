package com.chick.handle;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.chick.utils.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
* @Author xkx
* @Description
* @Date 2022-05-28 16:57
* @Param
* @return
**/
@Component
public class MetaHandler implements MetaObjectHandler {

    private static final Logger logger = LoggerFactory.getLogger(MetaHandler.class);

    /**
     * 新增数据执行
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        String userId = new String();
        try{
            userId = SecurityUtils.getUserId();
        }catch (Exception e){
            System.out.println("用户注册");
        }
        this.setFieldValByName("createDate",new Date(), metaObject);
        this.setFieldValByName("createBy", userId, metaObject);
        this.setFieldValByName("updateDate", new Date(), metaObject);
        this.setFieldValByName("updateBy", userId, metaObject);
        this.setFieldValByName("delFlag", "0", metaObject);
    }

    /**
     * 更新数据执行
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        String userId = SecurityUtils.getUserId();
        this.setFieldValByName("updateDate", new Date(), metaObject);
        this.setFieldValByName("updateBy", userId, metaObject);
    }

}
