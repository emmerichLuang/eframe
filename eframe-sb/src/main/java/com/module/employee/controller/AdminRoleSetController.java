package com.module.employee.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.controller.BaseController;
import com.module.employee.service.AdminRoleSetService;

/**
 * by E.E's code Generator
 * @Date 2019-05-14
 * @author liangrl
 *
 */
@Controller
@RequestMapping(value={"/employee/adminRoleSet/*"})
public class AdminRoleSetController extends BaseController{
	private static Logger logger = LoggerFactory.getLogger(AdminRoleSetController.class);
	
	@Autowired
	private AdminRoleSetService service;
	
	/**
	 * 
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/editPage")
	public ModelAndView editPage(HttpServletRequest request, HttpServletResponse response)
			throws Exception{
		String id = request.getParameter("id");
		
		//TODO: page
		ModelAndView result = new ModelAndView("");
		return result;
	}
	
	
	/**
	* editPage -->save
	* @param response
	* @return
	* @throws Exception
	*/
	@ResponseBody
	@RequestMapping("/save")
	public Object save(HttpServletRequest request,HttpServletResponse response)throws Exception{
		//TODO:
		return null;
	}
	
	/**
	 * export
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/export")
	public ModelAndView export(HttpServletRequest request,HttpServletResponse response){
		//TODO:
		return null;
	}
	
	/**
	 * 
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public Object delete(HttpServletRequest request,HttpServletResponse response){
		//TODO:
		return null;
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/listPage")
	public ModelAndView listPage(HttpServletRequest request,HttpServletResponse response){
		//TODO:
		return null;
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listData")
	public Object listData(HttpServletRequest request,HttpServletResponse response){
		//TODO:
		return null;
	}
}
