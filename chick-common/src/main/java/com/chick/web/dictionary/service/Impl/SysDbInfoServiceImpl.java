package com.chick.web.dictionary.service.Impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.common.utils.RedisUtil;
import com.chick.config.ChickRunner;
import com.chick.web.dictionary.entity.SysDbInfo;
import com.chick.web.dictionary.mapper.SysDbInfoMapper;
import com.chick.web.dictionary.service.ISysDbInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.chick.common.utils.ChickUtil.DoId;

/**
 * @ClassName SysDbInfoServiceImpl
 * @Author xiaokexin
 * @Date 2022/2/11 16:51
 * @Description SysDbInfoServiceImpl
 * @Version 1.0
 */
@Service
public class SysDbInfoServiceImpl implements ISysDbInfoService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SysDbInfoMapper sysDbInfoMapper;


    @Override
    public R save(SysDbInfo sysDbInfo) {
        sysDbInfo.setId(DoId());
        if (sysDbInfoMapper.insertSelective(sysDbInfo) == 1){
            ChickRunner.loadDictionary();
            return R.ok("保存成功");
        }
        return R.failed("保存失败");
    }

    @Override
    public R update(SysDbInfo sysDbInfo) {
        if (sysDbInfoMapper.updateByPrimaryKeySelective(sysDbInfo) == 1){
            ChickRunner.loadDictionary();
            return R.ok("更新成功");
        }
        return R.failed("更新失败");
    }

    @Override
    public R removeByDataNum(SysDbInfo sysDbInfo) {
        if(sysDbInfoMapper.removeByDataNum(sysDbInfo) > 0){
            ChickRunner.loadDictionary();
            return R.ok("删除成功");
        }
        return R.failed("删除失败");
    }

    @Override
    public R getAllDbInfo() {
        ArrayList<SysDbInfo> sysDbInfos = new ArrayList<SysDbInfo>(redisUtil.getHashEntries(CommonConstants.DICTIONARY).values());
        List<Tree<String>> nodeTreeByRedisInfo = getNodeTreeByRedisInfo(sysDbInfos);
        return R.ok(nodeTreeByRedisInfo);
    }

    @Override
    public R getData(String key) {
        return R.ok(redisUtil.get(CommonConstants.DICTIONARY + ":" + key));
    }

    @Override
    public R getChildrenData(String key) {
        //获取所有子项
        Map<String, Object> magic = redisUtil.getMagic(CommonConstants.DICTIONARY + ":" + key, false, false);

        return  R.ok(redisUtil.getMagic(CommonConstants.DICTIONARY + ":" + key, false, false));
    }

    public List<Tree<String>> getNodeTreeByRedisInfo(ArrayList<SysDbInfo> dbInfoList) {
        //ArrayList<SysDbInfo> dbInfoList = new ArrayList<SysDbInfo>(boundHashDictionary.entries().values());
        // 配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名 ，即返回列表里对象的字段名
        treeNodeConfig.setIdKey("dataNum"); //本id名
        treeNodeConfig.setWeightKey("sort");    //排序
        treeNodeConfig.setParentIdKey("parentNum"); //父id名
        treeNodeConfig.setChildrenKey("children"); //孩子名
        treeNodeConfig.setNameKey("dataInfo"); //字典内容
        treeNodeConfig.setDeep(10);
        //转换器
        List<Tree<String>> treeNodes = TreeUtil.build(dbInfoList, "0", treeNodeConfig,
                (dbInfo, tree) -> {
                    tree.setId(dbInfo.getDataNum());
                    tree.setParentId(dbInfo.getParentNum());
                    tree.setWeight(dbInfo.getSort());
                    tree.setName(dbInfo.getDataInfo());
                    // 扩展属性 ...
                    tree.putExtra("remarks", dbInfo.getRemarks());
                    tree.putExtra("realId", dbInfo.getId());    //真实的uuid
                    tree.putExtra("createTime", dbInfo.getCreateDate());
                    tree.putExtra("updateTime", dbInfo.getUpdateDate());
                    tree.putExtra("createBy", dbInfo.getCreateBy());
                    tree.putExtra("updateBy", dbInfo.getUpdateBy());
                });
        return treeNodes;
    }
}
