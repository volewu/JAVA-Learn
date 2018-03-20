package com.java.controller;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.IdentityService;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.java.entity.Group;
import com.java.entity.MemberShip;
import com.java.entity.PageBean;
import com.java.entity.User;
import com.java.service.GroupService;
import com.java.service.MemberShipService;
import com.java.service.UserService;
import com.java.util.ResponseUtil;
import com.java.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * �û�Controller
 * @author user
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {
	
	@Resource
	private UserService userService;
	
	@Resource
	private MemberShipService memberShipService;
	
	@Resource
	private GroupService groupService;
	
	//private IdentityService identityService;
	
	/**
	 * �û���¼
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/login")
	public void login(HttpServletRequest request,HttpServletResponse response) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		
		String userName=request.getParameter("userName");
		String password=request.getParameter("password");
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("userName", userName);
		map.put("password", password);
		Subject subject=SecurityUtils.getSubject();
		UsernamePasswordToken token=new UsernamePasswordToken(userName,password);
		JSONObject result=new JSONObject();
		try{
			subject.login(token);
			Session session=subject.getSession();
			MemberShip memberShip=memberShipService.findMemberShip(map);
			if(memberShip==null){
				result.put("success", false);
				result.put("errorInfo", "�û������������ɫ����");
			}else{
				result.put("success", true);
				session.setAttribute("currentMemberShip", memberShip);
				session.setAttribute("rev", memberShip.getUser().getRev()); 
			}
		}catch(Exception e){
			
			e.printStackTrace();
			result.put("success", false);
			result.put("errorInfo", "�û������������ɫ����");
		}
		try {
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * ����������ҳ��ѯ�û�����
	 * @param page
	 * @param rows
	 * @param s_user
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public String list(@RequestParam(value="page",required=false)String page,@RequestParam(value="rows",required=false)String rows,User s_user,HttpServletResponse response)throws Exception{
		PageBean pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("id",StringUtil.formatLike(s_user.getId())); // ��ѯ�û�����ȡ
		map.put("start", pageBean.getStart());
		map.put("size", pageBean.getPageSize());
		List<User> userList=userService.find(map);
		Long total=userService.getTotal(map);
		JSONObject result=new JSONObject();
		JSONArray jsonArray=JSONArray.fromObject(userList);
		result.put("rows", jsonArray);
		result.put("total", total);
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * ����������ҳ��ѯ�û��Լ��û��Ľ�ɫ��Ϣ����
	 * @param page
	 * @param rows
	 * @param s_user
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listWithGroups")
	public String listWithGroups(@RequestParam(value="page",required=false)String page,@RequestParam(value="rows",required=false)String rows,User s_user,HttpServletResponse response)throws Exception{
		PageBean pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("id",StringUtil.formatLike(s_user.getId())); // ��ѯ�û�����ȡ
		map.put("start", pageBean.getStart());
		map.put("size", pageBean.getPageSize());
		List<User> userList=userService.find(map);
		for(User user:userList){
			StringBuffer groups=new StringBuffer();
			List<Group> groupList=groupService.findByUserId(user.getId());
			for(Group g:groupList){
				groups.append(g.getName()+",");
			}
			if(groups.length()>0){
				user.setGroups(groups.deleteCharAt(groups.length()-1).toString()); //ȥ�����ģ���
			}else{
				user.setGroups(groups.toString());
			}
		}
		Long total=userService.getTotal(map);
		JSONObject result=new JSONObject();
		JSONArray jsonArray=JSONArray.fromObject(userList);
		result.put("rows", jsonArray);
		result.put("total", total);
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * ɾ���û�
	 * @param ids
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	public String delete(@RequestParam(value="ids",required=false)String ids,HttpServletResponse response)throws Exception{
		String []idsStr=ids.split(",");
		for(int i=0;i<idsStr.length;i++){
			userService.delete(idsStr[i]);
		}
		JSONObject result=new JSONObject();
		result.put("success", true);
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * ��ӻ����޸��û�
	 * @param user
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	public String save(User user,HttpServletResponse response,Integer flag)throws Exception{
		int resultTotal=0;
		if(flag==1){
			
			/*org.activiti.engine.identity.User user1=new UserEntity();//ʵ�����û�ʵ��
			user1.setId(user.getId());
			user1.setFirstName(user.getFirstName());
			user1.setPassword(user.getPassword());
			user1.setEmail(user.getEmail());
			identityService.saveUser(user1);
			resultTotal=1;*/
			resultTotal=userService.add(user);
		}else{
			resultTotal=userService.update(user);
		}
		JSONObject result=new JSONObject();
		if(resultTotal>0){
			result.put("success", true);
		}else{
			result.put("success", false);
		}
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * �������пμ�����
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/selectKjZg")
	public String SelectKjZg(@RequestParam(value="page",required=false)String page,@RequestParam(value="rows",required=false)String rows,HttpServletRequest request,HttpServletResponse response){
		PageBean pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		Map<String,Object> map=new HashMap<String,Object>();
		String kjname =request.getParameter("kjname");
		if(StringUtil.isNotEmpty(kjname)){
			
			map.put("kjname","%"+kjname+"%" );
		}
		
		map.put("start", pageBean.getStart());
		map.put("size", pageBean.getPageSize());
		List<User> userList=userService.findAllKjZg(map);
		int total=userService.getTotalKjZg(map);
		JSONObject result=new JSONObject();
		JSONArray jsonArray=JSONArray.fromObject(userList);
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
	 * �������в�������
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/selectBjZg")
	public String SelectBjZg(@RequestParam(value="page",required=false)String page,@RequestParam(value="rows",required=false)String rows,HttpServletRequest request,HttpServletResponse response){
		PageBean pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		Map<String,Object> map=new HashMap<String,Object>();
		String bjname =request.getParameter("bjname");
		if(StringUtil.isNotEmpty(bjname)){
			
			map.put("bjname", "%"+bjname+"%");
		}
		map.put("start", pageBean.getStart());
		map.put("size", pageBean.getPageSize());
		List<User> userList=userService.findAllBjZg(map);
		int total=userService.getTotalBjZg(map);
		JSONObject result=new JSONObject();
		JSONArray jsonArray=JSONArray.fromObject(userList);
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
	 * �ж��Ƿ����ָ���û���
	 * @param userName
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/existUserName")
	public String existUserName(String userName,HttpServletResponse response)throws Exception{
		JSONObject result=new JSONObject();
		if(userService.findById(userName)==null){
			result.put("exist", false);
		}else{
			result.put("exist", true);
		}
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * �޸��û�����
	 * @param id
	 * @param newPassword
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/modifyPassword")
	public String modifyPassword(String userId,String newPassword,HttpServletResponse response)throws Exception{
		User user=new User();
		user.setId(userId);
		user.setPassword(newPassword);
		JSONObject result=new JSONObject();
		int resultTotal=userService.update(user);
		if(resultTotal>0){
			result.put("success", true);
		}else{
			result.put("success", false);
		}
		ResponseUtil.write(response, result);
		return null;
	}
	
	
	/**
	 * �û�ע��
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/logout")
	public String logout()throws Exception{
		SecurityUtils.getSubject().logout();   
		return "redirect:/login.jsp";
	}
}
