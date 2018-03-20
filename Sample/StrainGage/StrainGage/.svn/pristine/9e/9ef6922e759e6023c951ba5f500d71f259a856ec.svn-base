package com.java.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.java.entity.Group;
import com.java.entity.MemberShip;
import com.java.entity.PageBean;
import com.java.entity.User;
import com.java.service.FileService;
import com.java.service.GroupService;
import com.java.service.MemberShipService;
import com.java.service.UserService;
import com.java.util.ResponseUtil;
import com.java.util.StringUtil;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 用户Controller
 * @author user
 *
 */
@Controller
@RequestMapping("/file")
public class FileController {
	
	@Resource
	private FileService fileService;
	
	
	@RequestMapping("/list")
	public String List(@RequestParam(value="page",required=false)String page,@RequestParam(value="rows",required=false)String rows,@RequestParam(value="filename",required=false)String filename,HttpServletRequest request,HttpServletResponse response) {
		
		PageBean pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("start", pageBean.getStart());
		map.put("size", pageBean.getPageSize());

		if (StringUtil.isNotEmpty(filename)) {
			map.put("filename", "%"+filename+"%");
		}
		
		List<com.java.entity.File> list=fileService.findAllFile(map);
		int total=fileService.countAllFile(map);
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
	 * 文件上传
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@RequestMapping("/fileupload")
	public String FileUpload(@RequestParam(value="file",required=false) MultipartFile file,HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException{
		Map<String, Object> map=new HashMap<String, Object>();
		if (!file.isEmpty()) {
			String filePath=request.getServletContext().getRealPath("/");  //获取当期那项目文件夹地址
			//System.out.println(filePath);
			Date now=new Date();
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
			String currentTime=simpleDateFormat.format(now);
			String subSting=file.getOriginalFilename().split("\\.")[1]; //获取文件后缀名
			File pathfile=new File(filePath+"Fileupload/");
			if (!pathfile.exists()) {
				pathfile.mkdirs();
			}
			file.transferTo(new File(filePath+"Fileupload/"+currentTime+"."+subSting));
		//	System.out.println("这是完全的文件名："+filePath+"Fileupload/"+currentTime+"."+subSting);
			map.put("fileName", currentTime+"."+subSting);
			map.put("realName", file.getOriginalFilename());
		}else {
			map.put("fileName", "");
			map.put("realName", "");
		}
		int saveNums=fileService.addFile(map);

		JSONObject result=new JSONObject();
		if(saveNums>0){
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
	
	
	
	
	
	/**
	 * 文件下载，
	 * @param response
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/fileDownload")
	
	 public String downLoadFile(String fileid, HttpServletResponse response,HttpServletRequest request ) throws IOException  {
		 
		 response.setCharacterEncoding("utf-8");
		
		 com.java.entity.File file1=fileService.findById(fileid);
		 
		 
		 String filePath=request.getServletContext().getRealPath("/")+"Fileupload/"; //获取当期那项目文件夹地址
		 String fullName=file1.getFilename();
		 String fileName=file1.getRealname();//MimeUtility.encodeWord(request.getParameter("fileName")); //MimeUtility编码解决火狐浏览器空格问题，造成无法识别文件名
		 String fileStorePath=filePath+fullName;
		 
		 File file=new File(fileStorePath);
	     OutputStream out = null;
	     response.reset();
	 	String dfileName=null;
	     String agent =request.getHeader("User-Agent");
			boolean isMSIE=(agent!=null && agent.indexOf("MSIE")!=-1); //判断当前浏览器是否为IE浏览器
			if (isMSIE) {
				
				String dfileName1 =URLEncoder.encode(fileName,"UTF-8"); 
				dfileName=dfileName1.replace("+","%20");  //+号的UTF-8为%20
				
			}else {
				dfileName = new String(fileName.getBytes("utf-8"), "iso8859-1");
			}
	     response.setContentType("application/octet-stream");
	     response.setHeader("Content-Disposition", "attachment;fileName=\"" + dfileName + "\"");//如果是 firefox ，则使用 MimeUtility 对文件名进行编码，并将文件名使用双引号引起来
        out = response.getOutputStream();
        out.write(FileUtils.readFileToByteArray(file));  
        out.flush(); 
        out.close(); 
		
		 return null;
	 }
	
	/**
	 * Ajax检查当前文件是否还存在
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@RequestMapping("/fileCheck")
	 public void checkFile(String fileid,HttpServletResponse response,HttpServletRequest request ) throws IOException  {
		 com.java.entity.File file1=fileService.findById(fileid);	
		 String filePath=request.getServletContext().getRealPath("/")+"Fileupload/"; //获取当期那项目文件夹地址
		 String fullName=file1.getFilename();
		 String fileStorePath=filePath+fullName;
		 JSONObject result=new JSONObject();
		 File file=new File(fileStorePath);
		 if (file == null || !file.exists()) {
			 
			 result.put("success", true);
		 }else {
			
			 result.put("success", false);
		}
			try {
				ResponseUtil.write(response, result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 }
	
	/**
	 * 批量删除文件
	 * @param ids
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/deletefile")
	public String FileDelete(@RequestParam(value="ids",required=false) String ids,HttpServletResponse response,HttpServletRequest request){
		JSONObject result=new JSONObject();
		String str[]=ids.split(",");
		int resNum=fileService.deleteFiles(str);
		
		if (resNum>0) {
			 result.put("success", true);
		 }else {
			
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
