package io.renren.modules.corporation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


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
	private String deptname;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createtime;
	private Long corcollege;
	private Long corfaculty;
	private Long corforcollege;
	private Long corscale;
	private String fileid;
	private String descs;
	private String videofile;
	private String bannerid;
	private Long iscor;
	private String corcollegeName;
	private String corfacultyName;
	private List<CorcrowdEntity> corcrowdList; //面向人群list
	private String userfilepath;
}
