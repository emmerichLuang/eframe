package init.emp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * 说明， 从oa拷贝过来的entity。用于网关oa.getEmpList接口
 * @Date 2018年10月30日
 * @author E.E.
 *
 */
public class EmployeeDto implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	public String id;

	@Column(name="CODE")
	private String code;
	
	@Column(name="MOBILE")
	private String mobile;
	
	@Column(name="CHNALIAS")
	public  String chnalias;  
	
	@Column(name="yyNumber")
	private String yyNumber;
	
	@Column(name="PRIVATE_YYNUMBER")
	private String privateYYNumber;
	
	@Column(name="yyAcount")
	private String yyAcount;
	
	//一级部门名称
	@Column(name="TOPNAME")
	private String topName;

	@Column(name="DEPTNAME")
	private String deptName;

	
	@Column(name="dept_id")
	private String deptId;
	
	@Column(name="PARENT_DEPT_ID")
	private String parentDeptId;
	
	@Column(name="full_dept_id")
	private String fullDeptIdPath;
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="STATE")
	private String state;
	
	@Column(name="supervisorCode")
	private String supervisorCode;
	@Column(name="supervisorName")
	private String supervisorName;
	@Column(name="supervisoryyaccount")
	private String supervisoryyaccount;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getYyNumber() {
		return yyNumber;
	}

	public void setYyNumber(String yyNumber) {
		this.yyNumber = yyNumber;
	}

	public String getPrivateYYNumber() {
		return privateYYNumber;
	}

	public void setPrivateYYNumber(String privateYYNumber) {
		this.privateYYNumber = privateYYNumber;
	}

	public String getYyAcount() {
		return yyAcount;
	}

	public void setYyAcount(String yyAcount) {
		this.yyAcount = yyAcount;
	}

	public String getTopName() {
		return topName;
	}

	public void setTopName(String topName) {
		this.topName = topName;
	}

	public String getChnalias() {
		return chnalias;
	}

	public void setChnalias(String chnalias) {
		this.chnalias = chnalias;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSupervisorCode() {
		return supervisorCode;
	}

	public void setSupervisorCode(String supervisorCode) {
		this.supervisorCode = supervisorCode;
	}

	public String getSupervisorName() {
		return supervisorName;
	}

	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}

	public String getSupervisoryyaccount() {
		return supervisoryyaccount;
	}

	public void setSupervisoryyaccount(String supervisoryyaccount) {
		this.supervisoryyaccount = supervisoryyaccount;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getParentDeptId() {
		return parentDeptId;
	}

	public void setParentDeptId(String parentDeptId) {
		this.parentDeptId = parentDeptId;
	}

	public String getFullDeptIdPath() {
		return fullDeptIdPath;
	}

	public void setFullDeptIdPath(String fullDeptIdPath) {
		this.fullDeptIdPath = fullDeptIdPath;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
