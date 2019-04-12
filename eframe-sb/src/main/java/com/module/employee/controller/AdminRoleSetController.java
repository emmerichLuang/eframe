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

@Controller
@RequestMapping(value={"/employee/adminRoleSet/*"})
public class AdminRoleSetController extends BaseController{
	private static Logger logger = LoggerFactory.getLogger(AdminRoleSetController.class);
	
	@Autowired
	private AdminRoleSetService service;
	
	/**
	 * 新增或者修改页面
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/createOrUpdatePage")
	public ModelAndView createOrUpdatePage(HttpServletRequest request, HttpServletResponse response)
			throws Exception{
		String id = request.getParameter("id");
		
		ModelAndView result = new ModelAndView("");
		
		
		return result;
	}
	
	
	/**
	 * createOrUpdatePage 对应的createOrUpdate
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView save(HttpServletResponse response)throws Exception{
		//TODO:
		return null;
	}
	
	/**
	 * 导出
	 * @param requset
	 * @param response
	 * @return
	 */
	public ModelAndView export(HttpServletRequest requset,
			HttpServletResponse response){
				//TODO:
				return null;
	}
	
	/**
	 * 删除
	 * @param response
	 * @return
	 */
	public ModelAndView doDel(HttpServletResponse response){
		//TODO:
		return null;
	}
	
	/**
	 * 列表页面
	 * @param requset
	 * @param response
	 * @return
	 */
	public ModelAndView listPage(HttpServletRequest requset,
			HttpServletResponse response){
				//TODO:
				return null;
			}
	
	/**
	 * 列表数据
	 * @param requset
	 * @param response
	 * @return
	 */
	public ModelAndView listData(HttpServletRequest requset,HttpServletResponse response){
		//TODO:
		return null;
	}
}
