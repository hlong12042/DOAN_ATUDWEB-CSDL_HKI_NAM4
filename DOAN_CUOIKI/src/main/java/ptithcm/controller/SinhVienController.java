package ptithcm.controller;

import javax.servlet.ServletContext;

import javax.transaction.Transactional;


import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Transactional
@Controller
@RequestMapping("/SV/")
public class SinhVienController {
	@Autowired
	SessionFactory factory;
	@Autowired
	ServletContext context;
	
	@RequestMapping(value="index", method = RequestMethod.GET)
	public String index() {
		return "SV/index";
	}
}
