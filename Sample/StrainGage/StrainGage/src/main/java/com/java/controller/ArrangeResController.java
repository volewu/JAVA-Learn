package com.java.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.java.entity.ArrangeRes;
import com.java.entity.PageBean;
import com.java.service.ArrangeResService;
import com.java.util.ResponseUtil;
import com.java.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/arrangeRes")
public class ArrangeResController {
	
	@Resource
	private ArrangeResService arrangeResService;
	
	/**
	 * 分页查找所有排配结果信息
	 * @param page
	 * @param rows
	 * @param filename
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/list")
	public String List(@RequestParam(value="page",required=false)String page,@RequestParam(value="rows",required=false)String rows,@RequestParam(value="filename",required=false)String filename,@RequestParam(value="projectname",required=false)String projectname,HttpServletRequest request,HttpServletResponse response) {
		
		PageBean pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("start", pageBean.getStart());
		map.put("size", pageBean.getPageSize());
		
		if (StringUtil.isNotEmpty(projectname)) {
			map.put("projectname", "%"+projectname+"%");
		}
		List<ArrangeRes> list=arrangeResService.findAllList(map);
		int total=arrangeResService.countAllList(map);
		JSONObject result=new JSONObject();
		JSONArray jsonArray=JSONArray.fromObject(list);
		result.put("rows", jsonArray);
		result.put("total", total);
		try {
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	

}
