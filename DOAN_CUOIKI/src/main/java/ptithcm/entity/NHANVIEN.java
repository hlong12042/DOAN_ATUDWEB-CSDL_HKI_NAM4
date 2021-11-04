package ptithcm.entity;

import java.io.File;
import java.security.interfaces.RSAKey;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import ptithcm.RSA.*;

@Entity
@Table(name = "NHANVIEN")
public class NHANVIEN {
	@Id
	private String MANV;
	private String HOTEN;
	private String EMAIL;
	private byte[] LUONG;
	private String TENDN;
	private byte[] MATKHAU;
	private String PUBKEY;
	
	@OneToMany(mappedBy = "nhanvien", fetch = FetchType.EAGER)
	private List<LOP> lops;
	
	public String getMANV() {
		return MANV;
	}
	public void setMANV(String mANV) {
		MANV = mANV;
	}
	public String getHOTEN() {
		return HOTEN;
	}
	public void setHOTEN(String hOTEN) {
		HOTEN = hOTEN;
	}
	public String getEMAIL() {
		return EMAIL;
	}
	public void setEMAIL(String eMAIL) {
		EMAIL = eMAIL;
	}
	public byte[] getLUONG() {
		return LUONG;
	}
	public void setLUONG(byte[] lUONG) {
		LUONG = lUONG;
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
	public List<LOP> getLops() {
		return lops;
	}
	public void setLops(List<LOP> lops) {
		this.lops = lops;
	}
	public String getPUBKEY() {
		return PUBKEY;
	}
	public void setPUBKEY(String pUBKEY) {
		PUBKEY = pUBKEY;
	}
	public long getLUONGlong() {
		return Long.valueOf(RSA.decryptRSA(LUONG, MANV, GenerateKeys.KEY_1024));
	}
	public boolean isHadKey() {
		return MANV!=null;
	}
//	public String getLUONGlong() {
//		File file = new File(GenerateKeys.PRIVATE_KEY_FOLDER);
//		return file.getAbsolutePath();
//	}
}
