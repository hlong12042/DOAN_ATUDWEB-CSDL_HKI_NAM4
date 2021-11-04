package ptithcm.controller;

import javax.servlet.http.HttpSession;

import java.util.List;

import javax.servlet.ServletContext;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ptithcm.entity.*;
import ptithcm.Hash.*;

@Transactional
@Controller
public class SignController {
	@Autowired
	SessionFactory factory;
	@Autowired
	ServletContext context;
	
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String login_get(ModelMap model, HttpSession httpsession) {		
		return "account/login";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String login_post(HttpSession httpsession, RedirectAttributes re,
			@RequestParam("tendn") String tendn, @RequestParam("matkhau") String matkhau) {		
		String hql = "FROM NHANVIEN WHERE TENDN = :tendn AND MATKHAU = :matkhau";
		Session session = factory.getCurrentSession();
		List<Object> list = session.createQuery(hql).setString("tendn", tendn).setParameter("matkhau", 
				Hash.convertSHA1(matkhau)).list();
		if (list.isEmpty()) {
			re.addFlashAttribute("tb", "Tên đăng nhập hoặc mật khẩu không đúng!");
			return "redirect:login.html";
		}
		httpsession.setAttribute("account", (NHANVIEN) list.get(0));
		httpsession.setMaxInactiveInterval(5*60*60);
		return "redirect:index.html";
	}
	
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout(HttpSession httpsession) {
		httpsession.setAttribute("account",null);
		return "redirect:login.html";
	}
	
	@RequestMapping(value = "forgot-password", method = RequestMethod.GET)
	public String forgot(RedirectAttributes re) {
		re.addFlashAttribute("tb", "Liên hệ với NV khác để nhận hỗ trợ");
		return "redirect:login.html";
	}
	
	@RequestMapping(value = "register", method = RequestMethod.GET)
	public String register(RedirectAttributes re) {
		re.addFlashAttribute("tb", "Liên hệ với NV khác để nhận hỗ trợ");
		return "redirect:login.html";
	}
}
