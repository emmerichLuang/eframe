package ddlGenerate;

public class VelocityColumn {
	public VelocityColumn(String dBFieldName, String fieldType, String fieldName) {
		super();
		this.dBFieldName = dBFieldName;
		this.fieldType = fieldType;
		this.fieldName = fieldName;
	}

	public boolean isPK = false;
	
	public String dBFieldName;
	
	public String fieldType;
	
	public String fieldName;

	public boolean isPK() {
		return isPK;
	}

	public void setPK(boolean isPK) {
		this.isPK = isPK;
	}

	public String getdBFieldName() {
		return dBFieldName;
	}

	public void setdBFieldName(String dBFieldName) {
		this.dBFieldName = dBFieldName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}
