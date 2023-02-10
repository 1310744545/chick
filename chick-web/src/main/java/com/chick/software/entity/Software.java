package com.chick.software.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.List;

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
 * @since 2022-03-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Software extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件id
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 软件名
     */
    private String softwareName;

    /**
     * 软件所属公司
     */
    private String company;

    /**
     * 描述
     */
    private String description;

    /**
     * 备注
     */
    private String remarks;

    private String officialWebsite;

    private String githubUrl;

    private String giteeUrl;

    private String type;

    private String imageUrl;

    @TableField(exist = false)
    private List<SoftwareDetail> softwareDetails;

    public Software(String id, String softwareName, String company, String description, String remarks) {
        this.id = id;
        this.softwareName = softwareName;
        this.company = company;
        this.description = description;
        this.remarks = remarks;
    }
}
