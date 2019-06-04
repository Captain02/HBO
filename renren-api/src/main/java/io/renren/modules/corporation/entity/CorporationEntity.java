package io.renren.modules.corporation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 社团
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@TableName("corporation")
public class CorporationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 社团ID
	 */
	@TableId(type= IdType.INPUT)
	private Long id;
	private String corname;
	private String corleading;
	private String cortercher;
	private String corworkspace;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	private Long corcollege;
	private Long corfaculty;
	private Long corforcollege;
	private Long corscale;
	private Long fileid;
	private String descs;
	private Long videofile;
	private Long bannerid;
	private Long iscor;
}
