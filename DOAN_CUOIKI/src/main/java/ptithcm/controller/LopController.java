package ptithcm.controller;

import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ptithcm.entity.NHANVIEN;
import ptithcm.entity.LOP;

@Transactional
@Controller
@RequestMapping("/lop/")
public class LopController {
	@Autowired
	SessionFactory factory;
	@Autowired
	ServletContext context;
	
	public static boolean XSScheck(String str) {
		String[] list = {"(?i)<script.*?>.*?</script.*?>", "(?i)<.*?javascript:.*?>.*?</.*?>", "(?i)<.*?\\s+on.*?>.*?</.*?>"};
		for (String s:list) 
			if (Pattern.matches(s, str)) return false;
		return true;
	}
	
	@RequestMapping("index")
	public String index() {
		return "Lop/index";
	}
	
	@RequestMapping(value="insert", method=RequestMethod.POST)
	public String insert(RedirectAttributes re, HttpSession httpSession,
			@RequestParam("malop") String malop, @RequestParam("tenlop") String tenlop) {
		try {//Kiểm tra input
			if (!XSScheck(tenlop)||!XSScheck(malop)) throw new Exception("Không sử dụng các kí tự '<,>,/,\\\\'");
			if (malop.length()>20) throw new Exception("Giới hạn độ dài mã lớp là 20!");
			NHANVIEN account = (NHANVIEN) httpSession.getAttribute("account");
			for (LOP lop:account.getLops())
				if (lop.getMALOP().equals(malop)) throw new Exception("Lớp đã tồn tại!");
			//Thêm vào db
			LOP lop = new LOP();
			lop.setMALOP(malop);
			lop.setTENLOP(tenlop);
			lop.setNhanvien(account);
			Session session = factory.openSession();
			Transaction t = session.beginTransaction();
			try {
				session.save(lop);
				t.commit();
				account.getLops().add(lop);
				re.addFlashAttribute("thanhcong", "Thêm lớp thành công!");
			} catch (Exception ex) {
				t.rollback();
				re.addFlashAttribute("loi", "Không thể thêm lớp này!");
			}
		} catch (Exception ex){
			re.addFlashAttribute("loi", ex);
		}
		return "redirect:index.html";
	}
	
	@RequestMapping(value="update", method=RequestMethod.POST)
	public String update(RedirectAttributes re, HttpSession httpSession,
			@RequestParam("malop") String malop, @RequestParam("tenlop") String tenlop) {
		try {//Kiểm tra input
			if (!XSScheck(tenlop)) throw new Exception("Không sử dụng các kí tự '<,>,/,\\\\'");
			NHANVIEN account = (NHANVIEN) httpSession.getAttribute("account");
			int i;
			for (i=0; i<account.getLops().size(); i++)
				if (account.getLops().get(i).getMALOP().equals(malop)) break;
			if (i==account.getLops().size()) 
				throw new Exception(String.format("Không tìm thấy lớp [%s]!", malop));
			//Thêm vào db
			LOP lop = account.getLops().get(i);
			lop.setTENLOP(tenlop);
			Session session = factory.openSession();
			Transaction t = session.beginTransaction();
			try {
				session.update(lop);
				t.commit();
				account.getLops().set(i, lop);
				re.addFlashAttribute("thanhcong", "Tên lớp đã được chỉnh sửa!");
			} catch (Exception ex) {
				t.rollback();
				re.addFlashAttribute("loi", "Không thể chỉnh sửa lớp này!");
			}
		} catch (Exception ex){
			re.addFlashAttribute("loi", ex);
		}
		return "redirect:index.html";
	}
	
	@RequestMapping(value="delete", method=RequestMethod.POST)
	public String delete(RedirectAttributes re, HttpSession httpSession,
			@RequestParam("malop") String malop) {
		try {//Kiểm tra input
			NHANVIEN account = (NHANVIEN) httpSession.getAttribute("account");
			int i;
			for (i=0; i<account.getLops().size(); i++)
				if (account.getLops().get(i).getMALOP().equals(malop)) break;
			if (i==account.getLops().size()) 
				throw new Exception(String.format("Không tìm thấy lớp [%s]!", malop));			
			LOP lop = account.getLops().get(i);
			if (!lop.getSinhviens().isEmpty())
				throw new Exception("Lớp này đã có sinh viên, không thể xóa!");
			
			Session session = factory.openSession();
			Transaction t = session.beginTransaction();
			try {
				session.delete(lop);
				t.commit();
				account.getLops().remove(i);
				re.addFlashAttribute("thanhcong", "Lớp đã được xóa!");
			} catch (Exception ex) {
				t.rollback();
				re.addFlashAttribute("loi", "Không thể xóa lớp này!");
			}
		} catch (Exception ex) {
			re.addFlashAttribute("loi", ex);
		}
		return "redirect:index.html";
	}
}
