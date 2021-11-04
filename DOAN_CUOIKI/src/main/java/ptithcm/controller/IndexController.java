package ptithcm.controller;

import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Transactional
@Controller
public class IndexController {
	@Autowired
	SessionFactory factory;
	@Autowired
	ServletContext context;
	
	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index(ModelMap model, HttpSession session) {		
		if (session.getAttribute("account")==null)
			return "redirect:/login.html";
		return "index";
	}
	
}
