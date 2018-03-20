package com.java.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.java.entity.PageBean;
import com.java.util.ResponseUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 流程定义管理Controller
 * @author user
 *
 */
@Controller
@RequestMapping("/processDefinition")
public class ProcessDefinitionController {

	@Resource
	private RepositoryService repositoryService;
	
	@Resource
	private HistoryService historyService;
	
	/**
	 * 流程定义查询
	 * @param page
	 * @param rows
	 * @param s_name
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public String list(@RequestParam(value="page",required=false)String page,@RequestParam(value="rows",required=false)String rows,String s_name,HttpServletResponse response)throws Exception{
		if(s_name==null){
			s_name="";
		}
		PageBean pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		List<ProcessDefinition> processDefinition=repositoryService.createProcessDefinitionQuery() // 创建流程流程定义查询
					.orderByProcessDefinitionId().desc() // 根据流程定义id降序排列
					.processDefinitionNameLike("%"+s_name+"%") // 根据流程定义名称模糊查询
					.listPage(pageBean.getStart(), pageBean.getPageSize()); // 返回带分页的结果集合
		long total=repositoryService.createProcessDefinitionQuery().processDefinitionNameLike("%"+s_name+"%").count(); // 获取总记录数
		JsonConfig jsonConfig=new JsonConfig();
		jsonConfig.setExcludes(new String[]{"identityLinks","processDefinition"});
		JSONObject result=new JSONObject();
		JSONArray jsonArray=JSONArray.fromObject(processDefinition,jsonConfig);
		result.put("rows", jsonArray);
		result.put("total", total);
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 查看流程图
	 * @param deploymentId
	 * @param diagramResourceName
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/showView")
	public String showView(String deploymentId,String diagramResourceName,HttpServletResponse response)throws Exception{
		InputStream inputStream=repositoryService.getResourceAsStream(deploymentId, diagramResourceName);
		OutputStream out=response.getOutputStream();
		for(int b=-1;(b=inputStream.read())!=-1;){
			out.write(b);
		}
		out.close();
		inputStream.close();
		return null;
	}
	
	/**
	 * 查看流程图
	 * @param deploymentId
	 * @param diagramResourceName
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/showViewByTaskId")
	public String showViewByTaskId(String taskId,HttpServletResponse response)throws Exception{
		HistoricTaskInstance hti=historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
		String processDefinitionId=hti.getProcessDefinitionId(); // 获取流程定义id
		ProcessDefinition pd=repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
		InputStream inputStream=repositoryService.getResourceAsStream(pd.getDeploymentId(), pd.getDiagramResourceName());
		OutputStream out=response.getOutputStream();
		for(int b=-1;(b=inputStream.read())!=-1;){
			out.write(b);
		}
		out.close();
		inputStream.close();
		return null;
	}
}
