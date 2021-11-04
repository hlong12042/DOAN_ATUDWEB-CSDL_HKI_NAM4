package ptithcm.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ptithcm.RSA.GenerateKeys;
import ptithcm.RSA.RSA;

@Entity
@Table(name = "BANGDIEM")
public class BANGDIEM {
	@Id
	@GeneratedValue
	private int ID;
	@ManyToOne
	@JoinColumn(name = "MASV")
	private SINHVIEN sinhvien;
	@ManyToOne
	@JoinColumn(name = "MAHP")
	private HOCPHAN hocphan;
	
	private byte[] DIEMTHI;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public SINHVIEN getSinhvien() {
		return sinhvien;
	}

	public void setSinhvien(SINHVIEN sinhvien) {
		this.sinhvien = sinhvien;
	}

	public HOCPHAN getHocphan() {
		return hocphan;
	}

	public void setHocphan(HOCPHAN hocphan) {
		this.hocphan = hocphan;
	}

	public byte[] getDIEMTHI() {
		return DIEMTHI;
	}

	public void setDIEMTHI(byte[] dIEMTHI) {
		DIEMTHI = dIEMTHI;
	}
	public float getDIEMTHIfloat() {
		return Float.parseFloat(RSA.decryptRSA(DIEMTHI, sinhvien.getLop().getNhanvien().getMANV(), GenerateKeys.KEY_1024));
	}
}
