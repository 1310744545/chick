package com.chick.web.dictionary.mapper;


import com.chick.web.dictionary.entity.SysDbInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysDbInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_db_info
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_db_info
     *
     * @mbg.generated
     */
    int insert(SysDbInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_db_info
     *
     * @mbg.generated
     */
    int insertSelective(SysDbInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_db_info
     *
     * @mbg.generated
     */
    SysDbInfo selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_db_info
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(SysDbInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_db_info
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(SysDbInfo record);

    int removeByDataNum(SysDbInfo record);

    /***
    * @Author xiaokexin
    * @Description 查询所有字典
    * @Date 2022/2/16 14:14
    * @Param [record]
    * @return int
    **/
    List<SysDbInfo> selectAll();

    List<SysDbInfo> getChildrenDataForManager(String key);
}
