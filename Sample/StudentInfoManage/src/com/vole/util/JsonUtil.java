package com.vole.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * User: vole
 * date: 2017年9月21日下午5:47:55
 * Function: ResultSet 转化为 JSONArray
 */
public class JsonUtil {

	public static JSONArray formatRsToJsonArray(ResultSet rs)throws Exception{
		ResultSetMetaData md=  rs.getMetaData();
		int num =md.getColumnCount();
		JSONArray array = new JSONArray();
		while(rs.next()){
			JSONObject mapOfColValue =new JSONObject();
			for (int i = 1; i <= num; i++) {
				Object o = rs.getObject(i);
				if(o instanceof Date){
					mapOfColValue.put(md.getColumnName(i), DateUtil.formatDate((Date)o, "yyyy-MM-dd"));
				}else{
					mapOfColValue.put(md.getColumnName(i), rs.getObject(i));					
				}
			}
			array.add(mapOfColValue);
		}
		return array;
	}
}
