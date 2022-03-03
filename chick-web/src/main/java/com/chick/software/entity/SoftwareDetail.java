package com.chick.software.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import com.chick.common.domin.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 *
 * </p>
 *
 * @author xiaokexin
 * @since 2022-03-03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SoftwareDetail extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 软件详情id
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 软件id
     */
    private String softwareId;

    /**
     * 软件名称
     */
    private String fileName;

    /**
     * 软件原名
     */
    private String fileOriginalName;

    /**
     * 软件版本
     */
    private String version;

    /**
     * 软件操作系统
     */
    private String operatingSystem;

    /**
     * 软件操作系统版本
     */
    private String osVersion;

    /**
     * 软件最后更新时间
     */
    private String lastModified;

    /**
     * 软件类型
     */
    private String type;

    /**
     * 软件在windows系统中的本地路径
     */
    private String windowsPath;

    /**
     * 软件在linux系统中的本地路径
     */
    private String linuxPath;

    /**
     * 软件大小单位b
     */
    private String size;

    /**
     * 备注
     */
    private String remarks;


    private String sourceOrCompile;


    private String downloadUrl;

}
