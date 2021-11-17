package ptithcm.controller;

import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ptithcm.RSA.GenerateKeys;
import ptithcm.RSA.RSA;
import ptithcm.entity.BANGDIEM;
import ptithcm.entity.HOCPHAN;
import ptithcm.entity.LOP;
import ptithcm.entity.NHANVIEN;
import ptithcm.entity.SINHVIEN;

@Transactional
@Controller
@RequestMapping("/hocphan/")
public class HocPhanController {
	@Autowired
	SessionFactory factory;
	@Autowired
	ServletContext context;	
	//Kiểm tra XSS, SQLi không bị vì sử dụng object để thực hiện các chức năng (tương đương PreparedStatement)
	public static boolean XSScheck(String str) {
		String[] list = {"(?i)<script.*?>.*?</script.*?>", "(?i)<.*?javascript:.*?>.*?</.*?>", "(?i)<.*?\\s+on.*?>.*?</.*?>"};
		for (String s:list) 
			if (Pattern.matches(s, str)) return false;
		if (str.contains("'")) return false;
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@ModelAttribute("hocphans")
	public List<HOCPHAN> getHocphans(){
		Session session = factory.getCurrentSession();
		return (List<HOCPHAN>) session.createQuery("FROM HOCPHAN").list();
	}
	
	@RequestMapping(value="index", method = RequestMethod.GET)
	public String index(ModelMap model) {		
		return "HocPhan/index";
	}
	
	@RequestMapping(value="insert", method = RequestMethod.POST)
	public String insert(RedirectAttributes re, @ModelAttribute("hocphans") List<HOCPHAN> hocphans, HttpSession httpsession,
			@RequestParam("mahp") String mahp, @RequestParam("tenhp") String tenhp, @RequestParam("sotc") int sotc) {
		try{//Kiểm tra input
			if(mahp.isBlank()||tenhp.isBlank()) throw new Exception("Gói tin của bạn có vấn đề!");
			if(!XSScheck(mahp)||!XSScheck(tenhp)) throw new Exception("Không sử dụng các kí tự '<,>,/,\'");
			if (sotc<=0||12<sotc) throw new Exception("Chỉ nhập số tín chỉ từ 1 đến 12 theo qui định!");
			if (mahp.length()>20) throw new Exception("Giới hạn độ dài mã học phần là 20");
			for (HOCPHAN hp:hocphans)
				if (hp.getMAHP().equals(mahp)) throw new Exception("Học phần này đã tồn tại trong db (hoặc trùng mã học phần)!");
			HOCPHAN hp = new HOCPHAN();
			hp.setMAHP(mahp);
			hp.setTENHP(tenhp);
			hp.setSOTC(sotc);
			Session session = factory.openSession();
			Transaction t = session.beginTransaction();
			try {
				session.save(hp);
				t.commit();
				re.addFlashAttribute("thanhcong", "Thêm học phần thành công");
			} catch(Exception ex) {
				t.rollback();
				re.addFlashAttribute("loi", "Không thể thêm học phần này!");
			}
		} catch(Exception ex) {
			re.addFlashAttribute("loi", ex);
		}
		return "redirect:index.html";
	}
	
	@RequestMapping(value="update", method = RequestMethod.POST)
	public String update(RedirectAttributes re, @ModelAttribute("hocphans") List<HOCPHAN> hocphans, HttpSession httpsession,
			@RequestParam("mahp") String mahp, @RequestParam("tenhp") String tenhp, @RequestParam("sotc") int sotc) {
		try {
			if(mahp.isBlank()||tenhp.isBlank()) throw new Exception("Gói tin của bạn có vấn đề!");
			if(!XSScheck(tenhp)) throw new Exception("Không sử dụng các kí tự '<,>,/,\'");
			if (sotc<=0||12<sotc) throw new Exception("Chỉ nhập số tín chỉ từ 1 đến 12 theo qui định!");
			HOCPHAN hp;
			int i=0;
			for (i=0; i<hocphans.size(); i++) 
				if (hocphans.get(i).getMAHP().equals(mahp)) break;			
			if (i==hocphans.size()) throw new Exception("Không tìm thấy học phần này!");
			hp = hocphans.get(i);
			hp.setTENHP(tenhp);
			hp.setSOTC(sotc);
			Session session = factory.openSession();
			Transaction t = session.beginTransaction();
			try {
				session.update(hp);
				t.commit();
				re.addFlashAttribute("thanhcong", "Học phần đã được chỉnh sửa");
			} catch(Exception ex) {
				t.rollback();
				re.addFlashAttribute("loi", "Không thể chỉnh sửa học phần này!");
			}
		} catch(Exception ex) {
			re.addFlashAttribute("loi", ex);
		}
		return "redirect:index.html";
	}
	
	@RequestMapping(value="delete", method = RequestMethod.POST)
	public String delete(RedirectAttributes re, @ModelAttribute("hocphans") List<HOCPHAN> hocphans,
			@RequestParam("mahp") String mahp) {
		try {
			if (mahp.isBlank()) throw new Exception("Không tìm thấy học phần này!");			
			int i=0;
			for (i=0; i<hocphans.size(); i++) 
				if (hocphans.get(i).getMAHP().equals(mahp)) break;			
			if (i==hocphans.size()) throw new Exception("Không tìm thấy học phần này!");
			HOCPHAN hp = hocphans.get(i);
			if (!hp.getBangdiems().isEmpty())
				throw new Exception("Học phần này đã được giảng dạy không thể xóa!");
			
			Session session = factory.openSession();
			Transaction t = session.beginTransaction();
			try {
				session.delete(hp);
				t.commit();
				re.addFlashAttribute("thanhcong", "Học phần đã được xóa");
			} catch(Exception ex) {
				t.rollback();
				re.addFlashAttribute("loi", "Không thể chỉnh sửa học phần này!");
			}
		} catch(Exception ex) {
			re.addFlashAttribute("loi", ex);
		}
		return "redirect:index.html";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="register", method=RequestMethod.POST)
	public String register(RedirectAttributes re, 
			@RequestParam("mahp") String mahp, @RequestParam("malop") String malop,
			@ModelAttribute("hocphans") List<HOCPHAN> hocphans, HttpSession httpsession) {
		try{
			
			if(mahp.isBlank()||malop.isBlank()) throw new Exception("Gói tin của bạn có vấn đề!");
			int i=0;	
			for (i=0;i<hocphans.size();i++) 
				if (hocphans.get(i).getMAHP().equals(mahp)) break;
			if (i==hocphans.size()) throw new Exception("Không tìm thấy học phần bạn chọn!");
			HOCPHAN hp = hocphans.get(i);
			
			NHANVIEN account = (NHANVIEN) httpsession.getAttribute("account");
			for (i=0;i<account.getLops().size();i++) 
				if (account.getLops().get(i).getMALOP().equals(malop)) break;
			if (i==account.getLops().size()) throw new Exception("Không tìm thấy lớp bạn chọn!");
			LOP lop = account.getLops().get(i);
			
			Session session = factory.getCurrentSession();
			List<Object> list = session.createQuery("FROM BANGDIEM WHERE hocphan.MAHP = :mahp "
					+ "AND sinhvien.lop.MALOP = :malop ").setString("mahp", mahp).setString("malop", malop).list();
			if (list.size()>0) throw new Exception("Lớp đã được đăng kí trước!");
			
			session.clear();
			session = factory.openSession();
			Transaction t = session.beginTransaction();
			try {
				for (SINHVIEN sv:lop.getSinhviens()) {
					BANGDIEM bd = new BANGDIEM();
					bd.setSinhvien(sv);
					bd.setHocphan(hp);
					bd.setDIEMTHI(RSA.encryptRSA("0", account.getMANV(), GenerateKeys.KEY_1024));
					session.save(bd);
				}
				t.commit();
				re.addFlashAttribute("thanhcong", "Đăng kí học phần thành công!");
			} catch (Exception ex) {
				t.rollback();
				re.addFlashAttribute("loi", "Không thể đăng kí!");
			}
		} catch(Exception ex) {
			re.addFlashAttribute("loi", ex);
		}
		return "redirect:index.html";
	}
}
