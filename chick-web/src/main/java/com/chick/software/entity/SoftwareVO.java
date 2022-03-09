package com.chick.software.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.chick.common.domin.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

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
public class SoftwareVO extends BaseEntity implements Serializable {

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
    private String describe;

    /**
     * 备注
     */
    private String remarks;

    private String officialWebsite;

    private String githubUrl;

    private String giteeUrl;

    private List<SoftwareDetail> softwareDetails;

    public SoftwareVO(String id, String softwareName, String company, String describe, String remarks) {
        this.id = id;
        this.softwareName = softwareName;
        this.company = company;
        this.describe = describe;
        this.remarks = remarks;
    }
}
