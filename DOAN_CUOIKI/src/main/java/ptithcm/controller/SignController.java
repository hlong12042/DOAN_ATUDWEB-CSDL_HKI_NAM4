package ptithcm.controller;

import javax.servlet.http.HttpSession;

import java.util.List;

import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ptithcm.entity.*;
import ptithcm.Hash.*;
import ptithcm.Random.*;

@Transactional
@Controller
public class SignController {
	@Autowired
	SessionFactory factory;
	@Autowired
	ServletContext context;
	@Autowired
	JavaMailSender mailer;
	
	public static String[] captcha_list = Random.RandomStringArray(99, 7, Random.alphabet);
	
	@ModelAttribute("captcha")
	public String catcha_text() {
		return captcha_list[Random.randomInt(0, 61)]+" "+captcha_list[Random.randomInt(0, 61)]+" "+captcha_list[Random.randomInt(0, 61)];
	}
	
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String login_get() {		
		return "account/login";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String login_post(HttpSession httpsession, RedirectAttributes re, @RequestParam("captcha") String captcha,
			@RequestParam("tendn") String tendn, @RequestParam("matkhau") String matkhau) {
		int i=0;
		for (i=0; i<captcha_list.length;i++) 
			if (captcha_list[i].equals(captcha)) break;
		if (i==captcha_list.length) {
			re.addFlashAttribute("tb", "Sai captcha!");
			return "redirect:login.html";
		}
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
	
	@RequestMapping(value = "reset_password", method = RequestMethod.GET)
	public String reset_get() {
		return "account/forgot_password";
	}
	
	@SuppressWarnings("unchecked" )
	@RequestMapping(value = "reset_password", method = RequestMethod.POST)
	public String reset_post(HttpSession httpsession, RedirectAttributes re,
			@RequestParam("tendn") String tendn, @RequestParam("email") String email) {
		try {
			if (tendn.isBlank()||email.isBlank()) throw new Exception("Không được để trống!");
			if (!email.contains("@")) throw new Exception("Nhập đúng định dạng email!");
			Session session = factory.getCurrentSession();
			List<NHANVIEN> list = (List<NHANVIEN>) session.createQuery("FROM NHANVIEN WHERE TENDN=:tendn").setString("tendn", tendn).list();
			if (list.isEmpty()) throw new Exception("Không tìm thấy tài khoản này!");
			String mk = Random.RandomString(40, Random.character);
			String body = String.format("<h1>[QLSV]</h1> "
					+ "<p>Xin chào [%s],</p> "
					+ "<p>Mật khẩu của bạn đã được reset</p>"
					+ "<p>Mật khẩu mới của bạn là:</p>"
					+ "<div style=\"padding: 2px 16px; background-color: yellow; box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2);\">\r\n"
					+ "	<div class=\"container\">\r\n"
					+ "		<h2>%s</h2>\r\n"
					+ "	</div>\r\n"
					+ "</div>"
					+ "<p>Hãy đăng nhập lại và đổi lại mật khẩu. Sau khi đăng nhập hãy xóa mail này để đảm bảo an toàn</p>", tendn, mk);
			MimeMessage mail = mailer.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mail);
			helper.setFrom("hoanglongmap2468@gmail.com");
			helper.setTo(email);
			helper.setSubject("QLSV thông báo reset password");
			helper.setText(body, true);
			mailer.send(mail);
			NHANVIEN account = list.get(0);
			account.setMATKHAU(Hash.convertSHA1(mk));
			session.clear();
			session = factory.openSession();
			Transaction t = session.beginTransaction();
			try {
				session.update(account);
				t.commit();
			} catch (Exception ex) {
				t.rollback();
				re.addFlashAttribute("thongbao", "Không thể reset password cho tài khoản này!");
			}
		} catch (Exception ex){
			re.addFlashAttribute("thongbao", ex);
			return "redirect:reset_password";
		}
		return "redirect:index.html";
	}
	
	@RequestMapping(value = "register", method = RequestMethod.GET)
	public String register(RedirectAttributes re) {
		re.addFlashAttribute("tb", "Liên hệ với NV khác để nhận hỗ trợ");
		return "redirect:login.html";
	}
}
