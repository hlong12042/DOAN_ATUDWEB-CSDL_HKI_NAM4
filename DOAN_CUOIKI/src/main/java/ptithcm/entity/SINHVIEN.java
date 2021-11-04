package ptithcm.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "SINHVIEN")
public class SINHVIEN {
	@Id
	private String MASV;
	private String HOTEN;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private Date NGAYSINH;
	private String DIACHI;
	private String TENDN;
	private byte[] MATKHAU;
	
	@ManyToOne
	@JoinColumn(name = "MALOP")
	private LOP lop;
	
	@OneToMany(mappedBy = "sinhvien", fetch = FetchType.EAGER)
	private List<BANGDIEM> bangdiems;

	public String getMASV() {
		return MASV;
	}

	public void setMASV(String mASV) {
		MASV = mASV;
	}

	public String getHOTEN() {
		return HOTEN;
	}

	public void setHOTEN(String hOTEN) {
		HOTEN = hOTEN;
	}

	public Date getNGAYSINH() {
		return NGAYSINH;
	}

	public void setNGAYSINH(Date nGAYSINH) {
		NGAYSINH = nGAYSINH;
	}

	public String getDIACHI() {
		return DIACHI;
	}

	public void setDIACHI(String dIACHI) {
		DIACHI = dIACHI;
	}

	public String getTENDN() {
		return TENDN;
	}

	public void setTENDN(String tENDN) {
		TENDN = tENDN;
	}

	public byte[] getMATKHAU() {
		return MATKHAU;
	}

	public void setMATKHAU(byte[] mATKHAU) {
		MATKHAU = mATKHAU;
	}

	public LOP getLop() {
		return lop;
	}

	public void setLop(LOP lop) {
		this.lop = lop;
	}

	public List<BANGDIEM> getBangdiems() {
		return bangdiems;
	}

	public void setBangdiems(List<BANGDIEM> bangdiems) {
		this.bangdiems = bangdiems;
	}
	
}
