package ptithcm.interceptor;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import ptithcm.entity.NHANVIEN;

@Transactional
public class AccountInterceptor extends HandlerInterceptorAdapter{
	@Autowired
	SessionFactory factory;
	
	@Autowired
	ServletContext context;
	
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception{
		HttpSession httpsession = req.getSession();
		if (httpsession.getAttribute("account")==null) {
			res.sendRedirect(req.getContextPath()+"/login.html");
			return false;
		}
		NHANVIEN account = (NHANVIEN) httpsession.getAttribute("account");
		Session session = factory.getCurrentSession();
		NHANVIEN nv = (NHANVIEN) session.get(NHANVIEN.class, account.getMANV());
		if (nv.getMATKHAU()!=account.getMATKHAU()) 
			return true;
		res.sendRedirect(req.getContextPath()+"/login.html");
		return false;		
	}
}
