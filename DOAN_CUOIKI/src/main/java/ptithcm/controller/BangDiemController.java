package ptithcm.controller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.hibernate.SQLQuery;
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
import ptithcm.entity.*;

@Transactional
@Controller
@RequestMapping("/bangdiem/")
public class BangDiemController {
	@Autowired
	SessionFactory factory;
	@Autowired
	ServletContext context;
	
	@SuppressWarnings("unchecked")
	@ModelAttribute("hocphans")
	public List<HOCPHAN> gethocphans(){
		Session session = factory.getCurrentSession();
		return (List<HOCPHAN>) session.createQuery("FROM HOCPHAN").list();
	}
	
	@RequestMapping(value="index", method=RequestMethod.GET)
	public String index() {		
		return "BangDiem/index";
	}
	
	@SuppressWarnings({ "unused", "unchecked" })
	@RequestMapping(value="loc", params={"malop", "mahp"}, method=RequestMethod.GET)
	public String loc(ModelMap model,RedirectAttributes re, @RequestParam("malop") String malop, @RequestParam("mahp") String mahp,
			HttpSession httpsession, @ModelAttribute("hocphans") List<HOCPHAN> hocphans) {
		try {
			if(malop.isBlank()||mahp.isBlank()) throw new Exception("Nhập đủ mã lớp và mã môn học");
			
			NHANVIEN account = (NHANVIEN) httpsession.getAttribute("account");
			int i = 0;
			for (i=0; i<account.getLops().size(); i++) 
				if (account.getLops().get(i).getMALOP().equals(malop)) break;
			if (account.getLops().size()==i) throw new Exception("Không tìm thấy lớp cần lọc!");
			LOP lop = account.getLops().get(i);
			
			i = 0;
			for (i=0; i<hocphans.size(); i++) 
				if (hocphans.get(i).getMAHP().equals(mahp)) break;
			if (hocphans.size()==i) throw new Exception("Không tìm thấy học phần!");
			HOCPHAN hp = hocphans.get(i);
			
			Session session = factory.getCurrentSession();
			List<BANGDIEM> bangdiems = (List<BANGDIEM>) session.createQuery("FROM BANGDIEM "
					+ "WHERE sinhvien.lop.MALOP = :malop "
					+ "AND hocphan.MAHP = :mahp ").setString("malop", malop).setString("mahp", mahp).list();
			model.addAttribute("bangdiems", bangdiems);
			model.addAttribute("mahp", mahp);
			model.addAttribute("malop", malop);
		} catch (Exception ex) {
			re.addFlashAttribute("loi", ex);
			return "redirect:index.html";
		}
		return "BangDiem/index";
	}
	
	@RequestMapping(value="delete", method=RequestMethod.POST)
	public String delete(RedirectAttributes re, @RequestParam("id") int id, HttpSession httpsession,
			@RequestParam("malop") String malop, @RequestParam("mahp") String mahp) {
		try {
			if (id<=0||malop.isBlank()||mahp.isBlank()) throw new Exception("Gói tin của bạn có vấn đề!");
			
			Session session = factory.getCurrentSession();
			BANGDIEM bd = (BANGDIEM) session.get(BANGDIEM.class, id);
			if (bd==null) throw new Exception("Không tìm thấy bảng ghi này!");
			if (!bd.getSinhvien().getLop().getMALOP().equals(malop)||!bd.getHocphan().getMAHP().equals(mahp)) 
				throw new Exception("Không tìm thấy bảng ghi này");
			session.clear();
			session = factory.openSession();
			Transaction t = session.beginTransaction();
			try {
				session.delete(bd);
				t.commit();
				re.addFlashAttribute("thanhcong", "Bảng ghi đã được xóa!");
			} catch (Exception ex) {
				t.rollback();
				re.addFlashAttribute("loi", "Không thể xóa bảng ghi này!");
				return "redirect:index.html";
			}
		} catch (Exception ex) {
			re.addFlashAttribute("loi", ex);
			return "redirect:index.html";
		}
		return "redirect:loc.html?malop="+malop+"&mahp="+mahp;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="insert_SV", method=RequestMethod.POST)
	public String insert_SV(RedirectAttributes re, HttpSession httpsession,
			@RequestParam("malop") String malop, @RequestParam("masv") String masv, @RequestParam("mahp") String mahp) {
		try {
			if (malop.isBlank()||masv.isBlank()||mahp.isBlank()) throw new Exception("Gói tin của bạn có vấn đề!");
			
			NHANVIEN account = (NHANVIEN) httpsession.getAttribute("account");
			int i=0;
			for (i=0; i<account.getLops().size(); i++)
				if (account.getLops().get(i).getMALOP().equals(malop)) break;
			if (i==account.getLops().size()) throw new Exception("Không tìm thấy lớp này trong số các lớp bạn quản lí");
			LOP lop = account.getLops().get(i);
			
			i=0;
			for (i=0; i<lop.getSinhviens().size(); i++) 
				if (lop.getSinhviens().get(i).getMASV().equals(masv)) break;
			if (i==lop.getSinhviens().size()) 
				throw new Exception(String.format("Không tìm thấy sinh viên [%s] trong lớp [%s]", masv, malop));
			SINHVIEN sv = lop.getSinhviens().get(i);
			
			Session session = factory.getCurrentSession();
			HOCPHAN hp = (HOCPHAN) session.get(HOCPHAN.class, mahp);
			if (hp==null) throw new Exception("Không tìm thấy học phần này!");
			
			List<Object> list = session.createQuery("FROM BANGDIEM "
					+ "WHERE sinhvien.MASV = :masv AND hocphan.MAHP = :mahp").setString("masv", masv).setString("mahp", mahp).list();
			if (!list.isEmpty()) throw new Exception("Sinh viên này đã tồn tại trong học phần!");
			BANGDIEM bd = new BANGDIEM();
			bd.setSinhvien(sv);
			bd.setHocphan(hp);
			bd.setDIEMTHI(RSA.encryptRSA("0", account.getMANV(), GenerateKeys.KEY_1024));
			session.clear();
			session = factory.openSession();
			Transaction t = session.beginTransaction();
			try {
				session.save(bd);
				t.commit();
				re.addFlashAttribute("thanhcong", "Thêm sinh viên thành công!");
			} catch (Exception ex) {
				t.rollback();
				re.addFlashAttribute("loi", "Không thể thêm sinh viên này!");
				return "redirect:index.html";
			}
		} catch (Exception ex) {
			re.addFlashAttribute("loi", ex);
			return "redirect:index.html";
		}
		return "redirect:loc.html?malop="+malop+"&mahp="+mahp;
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value="update", method=RequestMethod.POST)
	public String update(RedirectAttributes re, @RequestParam("malop") String malop, @RequestParam("mahp") String mahp,
			HttpSession httpsession, @RequestParam("id") int[] ids, @RequestParam("diem") float[] diems) {
		try {
			if (mahp.isBlank()||malop.isBlank()||ids.length==0||diems.length==0||ids.length!=diems.length)
				throw new Exception("Gói tin của bạn có vấn đề!");
			
			NHANVIEN account = (NHANVIEN) httpsession.getAttribute("account");
			int i=0;
			for (i=0; i<account.getLops().size(); i++)
				if (account.getLops().get(i).getMALOP().equals(malop)) break;
			if (i==account.getLops().size()) throw new Exception("Không tìm thấy lớp này trong số các lớp bạn quản lí");
			LOP lop = account.getLops().get(i);
			
			Session session = factory.getCurrentSession();
			HOCPHAN hp = (HOCPHAN) session.get(HOCPHAN.class, mahp);
			if (hp==null) throw new Exception("Không tìm thấy học phần này!");
			
			session.clear();
			session = factory.openSession();
			Transaction t = session.beginTransaction();
			try {
				SQLQuery q = session.createSQLQuery("UPDATE BANGDIEM SET DIEMTHI = :diem WHERE ID = :id");
				for (i=0; i<ids.length; i++) 
					if(q.setInteger("id", ids[i]).setParameter("diem", 
							RSA.encryptRSA(String.valueOf(diems[i]), account.getMANV(), GenerateKeys.KEY_1024)).executeUpdate()==2) 
						throw new Exception(String.format("Không thể ghi làm điểm, lỗi tại bảng ghi thứ [%s]",ids[i]));
				t.commit();
				re.addFlashAttribute("thanhcong", "Đã lưu lại kết quả!");
			} catch (Exception ex) {
				t.rollback();
				re.addFlashAttribute("loi", ex);
			}
		} catch (Exception ex) {
			re.addFlashAttribute("loi", ex);
			return "redirect:index.html";
		}
		return "redirect:loc.html?malop="+malop+"&mahp="+mahp;
	}
}
