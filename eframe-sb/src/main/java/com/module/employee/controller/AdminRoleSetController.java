package com.module.employee.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.base.controller.BaseController;
import com.module.employee.service.AdminRoleSetService;

/**
 * by E.E's code Generator
 * @Date 2019-04-15
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
	@RequestMapping("/createOrUpdatePage")
	public ModelAndView createOrUpdatePage(HttpServletRequest request, HttpServletResponse response)
			throws Exception{
		String id = request.getParameter("id");
		
		//TODO: page
		ModelAndView result = new ModelAndView("");
		return result;
	}
	
	
	/**
	 * createOrUpdatePage -->createOrUpdate
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView save(HttpServletRequest requset,HttpServletResponse response)throws Exception{
		//TODO:
		return null;
	}
	
	/**
	 * export
	 * @param requset
	 * @param response
	 * @return
	 */
	public ModelAndView export(HttpServletRequest requset,HttpServletResponse response){
		//TODO:
		return null;
	}
	
	/**
	 * 
	 * @param response
	 * @return
	 */
	public ModelAndView doDel(HttpServletRequest requset,HttpServletResponse response){
		//TODO:
		return null;
	}
	
	/**
	 * 
	 * @param requset
	 * @param response
	 * @return
	 */
	public ModelAndView listPage(HttpServletRequest requset,HttpServletResponse response){
		//TODO:
		return null;
	}
	
	/**
	 * 
	 * @param requset
	 * @param response
	 * @return
	 */
	public ModelAndView listData(HttpServletRequest requset,HttpServletResponse response){
		//TODO:
		return null;
	}
}