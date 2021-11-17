package ptithcm.controller;

import java.io.File;
import java.util.List;
import java.util.regex.*;

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

import ptithcm.Hash.*;
import ptithcm.RSA.*;
import ptithcm.entity.NHANVIEN;

@Transactional
@Controller
@RequestMapping("/nhanvien/")
public class NhanVienController {
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
	//ModelAttribute dùng để add thông tin trong suốt đường dẫn nhanvien/
	//SuppressWarnings để nó hết báo lỗi
	@SuppressWarnings("unchecked")
	@ModelAttribute("nhanviens")
	public List<NHANVIEN> getnhanviens(){
		Session session = factory.getCurrentSession();
		return (List<NHANVIEN>) session.createQuery("FROM NHANVIEN").list();
	}
	
	//Gọi trang index
	@RequestMapping(value="index", method = RequestMethod.GET)
	public String index(ModelMap model) {		
		return "NhanVien/index";
	}
	//Gọi trang insert
	@RequestMapping(value="insert", method = RequestMethod.GET)
	public String insertNV_GET(ModelMap model, HttpSession httpsession) {
		model.addAttribute("chedo", "insert");//set chế độ là insert
		return "NhanVien/nhanvien";
	}
	
	@RequestMapping(value="insert", method = RequestMethod.POST)
	public String insertNV_POST(RedirectAttributes re, HttpSession httpSession, @ModelAttribute("nhanviens") List<NHANVIEN> list,
			@RequestParam("manv") String manv, @RequestParam("hoten") String hoten,
			@RequestParam("email") String email, @RequestParam("luong") String luong,
			@RequestParam("tendn") String tendn, @RequestParam("matkhau1") String mk1, @RequestParam("matkhau2") String mk2) {	
		try {//Kiểm tra input			
			long luongl = Long.parseLong(luong);
			if (luongl<1000000) throw new NumberFormatException();
			if (!email.contains("@")) throw new Exception("Nhập đúng định dạng email!");
			if (!mk1.equals(mk2)) throw new Exception("Không trùng mật khẩu!");
			if (!XSScheck(manv)||!XSScheck(hoten)||!XSScheck(email)||!XSScheck(tendn)) 
				throw new Exception("Không sử dụng các kí tự '<,>,/,\'");
			if (manv.length()>20) throw new Exception("Giới hạn độ dài mã nhân viên là 20");
			//Kiểm tra xem mã nhân viên có nằm trong db chưa, 
			for (NHANVIEN nhanvien:list) 
				if (nhanvien.getMANV().equals(manv)||nhanvien.getTENDN().equals(tendn)) 
					throw new Exception("Nhân viên này đã tồn tại trong cơ sở dữ liệu");
			//Đầu tiên là tạo key, ngừng 1s để ct tạo 2 file key
			new GenerateKeys(GenerateKeys.KEY_1024, manv).gernerateKeysToFile();
			Thread.sleep(1000);
			//Tạo nhân viên mới và set các tham số vào
			NHANVIEN nv = new NHANVIEN();
			nv.setMANV(manv);
			nv.setHOTEN(hoten);
			nv.setEMAIL(email);
			nv.setLUONG(RSA.encryptRSA(luong, manv, GenerateKeys.KEY_1024));
			nv.setTENDN(tendn);
			nv.setMATKHAU(Hash.convertSHA1(mk2));
			nv.setLops(null);
			nv.setPUBKEY(manv);
			Session session = factory.openSession();
			Transaction t = session.beginTransaction();
			try {
				session.save(nv);
				t.commit();
				re.addFlashAttribute("thanhcong", "Thêm nhân viên thành công!");
			} catch (Exception ex) {
				t.rollback();
				re.addFlashAttribute("loi", "Không thể thêm nhân viên này!");
				return "redirect:insert.html";
			}//Các Exception có chức năng thông báo lỗi và redirect về trang insert
		} catch (NumberFormatException ex) {
			re.addFlashAttribute("loi", "Nhập đúng định dạng số của lương và lớn hơn 1000000!");
			return "redirect:insert.html";
		} catch (Exception ex) {
			re.addFlashAttribute("loi", ex);
			return "redirect:insert.html";
		}//Nếu thành công thì redirect về trang index
		return "redirect:index.html";
	}
	
	//Gọi trang update, sử dụng phương thức GET
	//Vì cần load dữ liệu từ nhân viên có sẵn nên cần manv để truy vấn
	//href có dạng "nhanvien/update.html?manv=${manv}"
	@RequestMapping(value="update", params = {"manv"}, method = RequestMethod.GET)
	public String updateNV(ModelMap model, HttpSession httpsession, 
			@ModelAttribute("nhanviens") List<NHANVIEN> list, @RequestParam("manv") String manv) {				
		NHANVIEN nhanvien = new NHANVIEN();
		for (NHANVIEN nv:list) 
			if (nv.getMANV().equals(manv)) {
				nhanvien = nv;
				break;
			}		
		model.addAttribute("nv", nhanvien);
		model.addAttribute("chedo", "update");
		return "NhanVien/nhanvien";
	}
	
	@RequestMapping(value="update", method = RequestMethod.POST)
	public String updateNV_POST(RedirectAttributes re, @ModelAttribute("nhanviens") List<NHANVIEN> list,
			@RequestParam("manv") String manv, @RequestParam("hoten") String hoten,
			@RequestParam("email") String email, @RequestParam("luong") String luong,
			@RequestParam("tendn") String tendn, @RequestParam("matkhau1") String mk1, @RequestParam("matkhau2") String mk2) {
		try {//Kiểm tra input
			long luongl = Long.parseLong(luong);
			if (luongl<1000000) throw new NumberFormatException();
			if (!email.contains("@")) throw new Exception("Nhập đúng định dạng email!");
			if (!mk1.equals(mk2)) throw new Exception("Không trùng mật khẩu!");
			if (!XSScheck(hoten)||!XSScheck(email)||!XSScheck(tendn)) 
				throw new Exception("Không sử dụng các kí tự '<,>,/,\'");
			//Kiểm tra xem manv đã có trong db chưa
			int i=0;
			for (i=0; i<list.size();i++)
				if (list.get(i).getMANV().equals(manv)) break;
			if (i==list.size()) throw new Exception("Không tìm thấy nhân viên này!");
			NHANVIEN nv = list.get(i);
			nv.setHOTEN(hoten);
			nv.setEMAIL(email);
			nv.setLUONG(RSA.encryptRSA(luong, manv, GenerateKeys.KEY_1024));
			nv.setTENDN(tendn);
			nv.setMATKHAU(Hash.convertSHA1(mk2));
			nv.setLops(null);
			nv.setPUBKEY(manv);
			Session session = factory.openSession();
			Transaction t = session.beginTransaction();
			try {
				session.update(nv);
				t.commit();
				re.addFlashAttribute("thanhcong", "Chỉnh sửa thành công!");
			} catch (Exception ex) {
				t.rollback();
				re.addFlashAttribute("loi", "Không thể chỉnh sửa nhân viên này!");
				return "redirect:update.html?manv="+manv;
			}
		} catch (Exception ex) {
			re.addFlashAttribute("loi", ex);
			return "redirect:update.html?manv="+manv;
		}
		return "redirect:index.html";
	}
	
	@RequestMapping(value="delete", method = RequestMethod.POST)
	public String delete(RedirectAttributes re, HttpSession httpsession,@RequestParam("manv") String manv) {
		try {
			Session session = factory.getCurrentSession();
			NHANVIEN nv = (NHANVIEN) session.get(NHANVIEN.class, manv);
			if (nv==null) throw new Exception("Không tìm thấy nhân viên này!");
			if (!nv.getLops().isEmpty()) throw new Exception("Nhân viên này đã quản lí lớp, không thể xóa!");
			if (nv == (NHANVIEN) httpsession.getAttribute("account")) 
				throw new Exception("Tài khoản đang sử dụng nhân viên này, không thể xóa!");
			session.clear();
			session = factory.openSession();
			Transaction t = session.beginTransaction();
			try {
				session.delete(nv);
				t.commit();//Cập nhật lại httpsession
				//xóa 2 file key cũ đi tránh chiếm bộ nhớ
				File prikey = new File(GenerateKeys.PRIVATE_KEY_FOLDER+manv);
				File pubkey = new File(GenerateKeys.PUBLIC_KEY_FOLDER+manv+".pub");
				prikey.delete(); pubkey.delete();
				re.addFlashAttribute("thanhcong", "Nhân viên đã được xóa!");
			} catch(Exception ex) {
				t.rollback();
				re.addFlashAttribute("loi", "Không thể xóa nhân viên này!");
			}
		} catch (Exception ex) {
			re.addFlashAttribute("loi", ex);
		}		
		return "redirect:index.html";
	}
	
}
