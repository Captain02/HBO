package io.renren.modules.corporation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 面向人群
 */
@TableName("corcrowd")
public class CorcrowdEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * ID
     */
    @TableId(type= IdType.INPUT)
    private Long id;
    private Long corid;
    private Long crowdid;
}
