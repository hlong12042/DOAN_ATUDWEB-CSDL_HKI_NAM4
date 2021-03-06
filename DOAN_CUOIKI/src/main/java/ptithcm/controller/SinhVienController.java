package ptithcm.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ptithcm.entity.LOP;
import ptithcm.entity.NHANVIEN;
import ptithcm.entity.SINHVIEN;
import ptithcm.Hash.Hash;

@Transactional
@Controller
@RequestMapping("/SV/")
public class SinhVienController {
	@Autowired
	SessionFactory factory;
	@Autowired
	ServletContext context;
	
	public static boolean XSScheck(String str) {
		String[] list = {"(?i)<script.*?>.*?</script.*?>", "(?i)<.*?javascript:.*?>.*?</.*?>", "(?i)<.*?\\s+on.*?>.*?</.*?>"};
		for (String s:list) 
			if (Pattern.matches(s, str)) return false;
		if (str.contains("'")) return false;
		return true;
	}	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="index", method = RequestMethod.GET)
	public String index(ModelMap model, HttpSession httpSession) {
		NHANVIEN account = (NHANVIEN) httpSession.getAttribute("account");
		Session session = factory.getCurrentSession();
		List<SINHVIEN> sinhviens = (List<SINHVIEN>) session.createQuery("FROM SINHVIEN "
				+ "WHERE lop.nhanvien.MANV=:manv").setString("manv", account.getMANV()).list();
		model.addAttribute("sinhviens", sinhviens);
		return "SV/index";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="index", params={"malop"}, method=RequestMethod.GET)
	public String loc(ModelMap model, HttpSession httpSession, 
			@RequestParam("malop") String malop) {
		NHANVIEN account = (NHANVIEN) httpSession.getAttribute("account");
		Session session = factory.getCurrentSession();
		List<SINHVIEN> sinhviens = (List<SINHVIEN>) session.createQuery("FROM SINHVIEN "
				+ "WHERE lop.nhanvien.MANV=:manv "
				+ "AND lop.MALOP=:malop").setString("manv", account.getMANV()).setString("malop", malop).list();
		model.addAttribute("sinhviens", sinhviens);
		if (sinhviens.isEmpty()) model.addAttribute("loi", "Kh??ng t??m th???y l???p c???n l???c");
		model.addAttribute("malop", malop);
		return "SV/index";
	}
	
	@RequestMapping(value="insert", method = RequestMethod.GET)
	public String them_get(ModelMap model) {
		model.addAttribute("chedo", "insert");//set ch??? ????? l?? insert
		return "SV/sinhvien";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="insert", method = RequestMethod.POST)
	public String them_post(RedirectAttributes re, HttpSession httpSession,
			@RequestParam("masv") String masv, @RequestParam("hoten") String hoten,
			@RequestParam("ngaysinh") String ns, @RequestParam("diachi") String diachi,
			@RequestParam("malop") String malop, @RequestParam("tendn") String tendn,
			@RequestParam("matkhau1") String matkhau1, @RequestParam("matkhau2") String matkhau2) {
		try {//Ki???m tra input
			if(masv.isBlank()||hoten.isBlank()||ns.isBlank()||diachi.isBlank()||
					malop.isBlank()||tendn.isBlank()||matkhau1.isBlank()||matkhau2.isBlank())
				throw new Exception("Kh??ng ???????c ????? tr???ng!");
			if (!XSScheck(hoten)||!XSScheck(diachi)||!XSScheck(tendn))
				throw new Exception("Kh??ng s??? d???ng c??c k?? t??? '<,>,/,\\'");
			if (!matkhau1.equals(matkhau2))
				throw new Exception("Kh??ng tr??ng m???t kh???u!");
			if (masv.length()>20) throw new Exception("Gi???i h???n ????? d??i m?? sinh vi??n l?? 20");
			Date ngaysinh = new SimpleDateFormat("yyyy-MM-dd").parse(ns);
			//Ki???m tra t???n t???i m?? l???p v?? m?? sinh vi??n
			Session session = factory.getCurrentSession();
			NHANVIEN account = (NHANVIEN) httpSession.getAttribute("account");
			List<LOP> list = (List<LOP>) session.createQuery("FROM LOP "
					+ "WHERE MALOP=:malop "
					+ "AND nhanvien.MANV=:manv").setString("malop", malop).setString("manv", account.getMANV()).list();
			if (list.isEmpty()) throw new Exception(String.format("Kh??ng t??m th???y l???p [%s]!",malop));
			LOP lop = list.get(0);
			int i;
			for (i=0; i<lop.getSinhviens().size(); i++) 
				if (lop.getSinhviens().get(i).getMASV().equals(masv)||lop.getSinhviens().get(i).getTENDN().equals(tendn)) break;
			if (i!=lop.getSinhviens().size()) 
				throw new Exception("Sinh vi??n n??y ???? t???n t???i ho???c tr??ng t??n ????ng nh???p!");
			//T???o sinh vi??n v?? th??m
			SINHVIEN sv = new SINHVIEN();
			sv.setMASV(masv);
			sv.setHOTEN(hoten);
			sv.setNGAYSINH(ngaysinh);
			sv.setDIACHI(diachi);
			sv.setTENDN(tendn);
			sv.setMATKHAU(Hash.convertSHA1(matkhau1));
			sv.setLop(lop);
			session.clear();
			session = factory.openSession();
			Transaction t = session.beginTransaction();
			try {
				session.save(sv);
				t.commit();
				session.clear();//Update httpsession
				for (LOP l:account.getLops()) 
					if (l.getMALOP().equals(lop.getMALOP())){
						l.getSinhviens().add(sv); break;
					}
				re.addFlashAttribute("thanhcong", "Th??m sinh vi??n th??nh c??ng!");
			} catch (Exception ex) {
				t.rollback();
				re.addFlashAttribute("loi", "Kh??ng th??? th??m sinh vi??n n??y!");
				return "redirect:insert.html";
			}			
		} catch (IllegalArgumentException ex) {
			re.addFlashAttribute("loi", "Nh???p ????ng ?????nh d???ng ng??y th??ng!");
			return "redirect:insert.html";
		} catch (Exception ex){
			re.addFlashAttribute("loi", ex);
			return "redirect:insert.html";
		}
		return "redirect:index.html";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="delete", method = RequestMethod.POST)
	public String delete(RedirectAttributes re, HttpSession httpSession,
			@RequestParam("masv") String masv) {
		try {
			NHANVIEN account = (NHANVIEN) httpSession.getAttribute("account");
			Session session = factory.getCurrentSession();
			List<SINHVIEN> list = (List<SINHVIEN>) session.createQuery("FROM SINHVIEN "
					+ "WHERE MASV=:masv "
					+ "AND lop.nhanvien.MANV=:manv").setString("masv", masv).setString("manv", account.getMANV()).list();
			if (list.isEmpty()) throw new Exception("Kh??ng t??m th???y sinh vi??n n??y!");
			SINHVIEN sv = list.get(0);
			if (!sv.getBangdiems().isEmpty()) throw new Exception("Sinh vi??n n??y ???? theo h???c, kh??ng th??? x??a!");
			LOP lop = sv.getLop();
			session.clear();
			session = factory.openSession();
			Transaction t = session.beginTransaction();
			try {
				session.delete(sv);
				t.commit();
				session.clear();
				for (LOP l:account.getLops()) 
					if (l.getMALOP().equals(lop.getMALOP())) {
						for (SINHVIEN s:l.getSinhviens()) {
							if (s.getMASV().equals(masv)) {
								l.getSinhviens().remove(s);
								break;
							}
						}
					break;
				}				
				re.addFlashAttribute("thanhcong", "X??a sinh vi??n th??nh c??ng!");
			} catch (Exception ex) {
				t.rollback();
				re.addFlashAttribute("loi", "Kh??ng th??? x??a sinh vi??n n??y!");
			}
		} catch (Exception ex) {
			re.addFlashAttribute("loi", ex);
		}
		return "redirect:index.html";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="update", params="masv", method=RequestMethod.GET)
	public String update_get(ModelMap model, RedirectAttributes re, 
			HttpSession httpsession, @RequestParam("masv") String masv) {
		try {
			Session session = factory.getCurrentSession();
			NHANVIEN account = (NHANVIEN) httpsession.getAttribute("account");
			List<SINHVIEN> list = (List<SINHVIEN>) session.createQuery("FROM SINHVIEN "
					+ "WHERE MASV=:masv "
					+ "AND lop.nhanvien.MANV=:manv").setString("masv", masv).setString("manv", account.getMANV()).list();
			if (list.isEmpty()) throw new Exception(String.format("Kh??ng t??m th???y sinh vi??n [%s]!",masv));
			model.addAttribute("sv", list.get(0));
			model.addAttribute("chedo", "update");
		} catch (Exception ex) {
			re.addFlashAttribute("loi", ex);
			return "redirect:index.html";
		}
		return "SV/sinhvien";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="update", method=RequestMethod.POST)
	public String update_post(RedirectAttributes re, HttpSession httpSession,
			@RequestParam("masv") String masv, @RequestParam("hoten") String hoten,
			@RequestParam("ngaysinh") String ns, @RequestParam("diachi") String diachi,
			@RequestParam("malop") String malop, @RequestParam("tendn") String tendn,
			@RequestParam("matkhau1") String matkhau1, @RequestParam("matkhau2") String matkhau2) {
		try {
			//Ki???m tra input
			if(masv.isBlank()||hoten.isBlank()||ns.isBlank()||diachi.isBlank()||
					malop.isBlank()||tendn.isBlank()||matkhau1.isBlank()||matkhau2.isBlank())
				throw new Exception("Kh??ng ???????c ????? tr???ng!");
			if (!XSScheck(hoten)||!XSScheck(diachi)||!XSScheck(tendn))
				throw new Exception("Kh??ng s??? d???ng c??c k?? t??? '<,>,/,\\'");
			if (!matkhau1.equals(matkhau2))
				throw new Exception("Kh??ng tr??ng m???t kh???u!");
			Date ngaysinh = new SimpleDateFormat("yyyy-MM-dd").parse(ns);
			//Ki???m tra t???n t???i m?? l???p v?? m?? sinh vi??n
			Session session = factory.getCurrentSession();
			NHANVIEN account = (NHANVIEN) httpSession.getAttribute("account");
			List<LOP> list = (List<LOP>) session.createQuery("FROM LOP "
					+ "WHERE MALOP=:malop "
					+ "AND nhanvien.MANV=:manv").setString("malop", malop).setString("manv", account.getMANV()).list();
			if (list.isEmpty()) throw new Exception(String.format("Kh??ng t??m th???y l???p [%s]!",malop));
			LOP lop = list.get(0);
			int i;
			for (i=0; i<lop.getSinhviens().size(); i++) 
				if (lop.getSinhviens().get(i).getMASV().equals(masv)||lop.getSinhviens().get(i).getTENDN().equals(tendn)) break;
			if (i==lop.getSinhviens().size()) 
				throw new Exception("Kh??ng t??m th???y sinh vi??n n??y!");
			SINHVIEN sv = lop.getSinhviens().get(i);
			sv.setHOTEN(hoten);
			sv.setNGAYSINH(ngaysinh);
			sv.setDIACHI(diachi);
			sv.setTENDN(tendn);
			sv.setMATKHAU(Hash.convertSHA1(matkhau1));
			session.clear();
			session = factory.openSession();
			Transaction t = session.beginTransaction();
			try {
				session.update(sv);
				t.commit();
				for (LOP l:account.getLops()) {
					if (l.getMALOP().equals(lop.getMALOP())) {
						for (i=0; i<l.getSinhviens().size(); i++) {
							if (l.getSinhviens().get(i).getMASV().equals(masv)) {
								l.getSinhviens().set(i, sv);
								break;
							}
						}
						break;
					}
				}
				re.addFlashAttribute("thanhcong", "Ch???nh s???a sinh vi??n th??nh c??ng");
			} catch (Exception ex) {
				t.rollback();
				re.addFlashAttribute("loi", "Kh??ng th??? th??m sinh vi??n n??y!");
			}
		} catch (Exception ex) {
			re.addFlashAttribute("loi", ex);
		}
		return "redirect:index.html";
	}
}
