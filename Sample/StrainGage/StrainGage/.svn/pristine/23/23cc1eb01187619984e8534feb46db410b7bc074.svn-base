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

import com.java.entity.Arrange;
import com.java.entity.PageBean;
import com.java.service.ArrangeService;
import com.java.util.ResponseUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/arrange")
public class ArrangeController {
	
	@Resource
	private ArrangeService arrangeService;
	
	/**
	 * 分页查找所有排配信息
	 * @param page
	 * @param rows
	 * @param filename
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/list")
	public String List(@RequestParam(value="page",required=false)String page,@RequestParam(value="rows",required=false)String rows,@RequestParam(value="filename",required=false)String filename,HttpServletRequest request,HttpServletResponse response) {
		
		PageBean pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("start", pageBean.getStart());
		map.put("size", pageBean.getPageSize());
		List<Arrange> list=arrangeService.findAllList(map);
		int total=arrangeService.countAllList(map);
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
	
	/**
	 * 更新选中Id的排配信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/update")
	public String Update(HttpServletRequest request,HttpServletResponse response) {
		
		String arrid=request.getParameter("arrid");
		String starttime1=request.getParameter("starttime");
		String endtime1=request.getParameter("endtime");
		String handlerstate=request.getParameter("handlerstate");
		String applybu=request.getParameter("applybu");
		String applydate=request.getParameter("applydate");
		String projectname=request.getParameter("projectname");
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("arrid", arrid);
		map.put("starttime", starttime1);
		map.put("endtime", endtime1);
		map.put("handlerstate", handlerstate);
		map.put("applybu", applybu);
		map.put("applydate", applydate);
		map.put("projectname", projectname);
		int resultTotal=0;
		if (handlerstate.equals("处理结束")) {
			resultTotal=arrangeService.deleteArrange(map);
		}else {
			resultTotal=arrangeService.updateArrange(map);
		}
		
		JSONObject result=new JSONObject();
		if(resultTotal>0){
			result.put("success", true);
		}else{
			result.put("success", false);
		}
		try {
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
