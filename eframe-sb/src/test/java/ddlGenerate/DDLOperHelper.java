package ddlGenerate;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class DDLOperHelper {
	/**
	 * metaData变为表结构
	 * 
	 * @param metaData
	 * @return
	 * @param
	 * @throws Exception 
	 */
	public static List<VelocityColumn> getDDL(ResultSet rs) throws Exception {
		ResultSetMetaData metaData = rs.getMetaData();
		
		List<VelocityColumn> columnList = new ArrayList<VelocityColumn>();

		int count = metaData.getColumnCount();

		Method method = com.mysql.jdbc.ResultSetMetaData.class.getDeclaredMethod("getField", int.class);
        method.setAccessible(true);
        
		for(int i=1;i<=count;i++){
			String cName = metaData.getColumnName(i);
			String columnType = metaData.getColumnTypeName(i);
			//System.err.println(cName+", "+columnType);
			
			com.mysql.jdbc.Field field = (com.mysql.jdbc.Field) method.invoke(metaData, i);
			
			VelocityColumn vc = new VelocityColumn(cName,mapFieldType(columnType),camelCaseName(cName));
			if(field.isPrimaryKey()){
				vc.setPK(true);
			}
			
			columnList.add(vc);
		}
		
		return columnList;
	}
	
    /** 
     * 转换为驼峰 
     *  
     * @param underscoreName 
     * @return 
     */  
    private static String camelCaseName(final String underscoreName) {  
    	String temp = underscoreName.toLowerCase();
        StringBuilder result = new StringBuilder();  
        if (temp != null && temp.length() > 0) {  
            boolean flag = false;  
            for (int i = 0; i < temp.length(); i++) {  
                char ch = temp.charAt(i);  
                if ("_".charAt(0) == ch) {  
                    flag = true;  
                } else {  
                    if (flag) {  
                        result.append(Character.toUpperCase(ch));  
                        flag = false;  
                    } else {  
                        result.append(ch);  
                    }  
                }  
            }  
        }  
        return result.toString();  
    }
	
    /**
     * 命名规则， 映射字段类型
     * @param columnType
     * @return
     * @param
     */
	private static String mapFieldType(String columnType){
		if(StringUtils.equalsIgnoreCase("NUMBER", columnType)||
				StringUtils.equalsIgnoreCase("INT", columnType)||
				StringUtils.equalsIgnoreCase("TINYINT", columnType)){
			return "Integer";
		}else if(StringUtils.equalsIgnoreCase("DATE", columnType)||
				StringUtils.equalsIgnoreCase("TIMESTAMP", columnType)||
				StringUtils.equalsIgnoreCase("DATETIME", columnType)){
			return "Date";
		}
		return "String";
	}
}
