package ptithcm.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "LOP")
public class LOP {
	@Id
	private String MALOP;
	private String TENLOP;
	
	@ManyToOne
	@JoinColumn(name = "MANV")
	private NHANVIEN nhanvien;
	
	@OneToMany(mappedBy = "lop", fetch = FetchType.EAGER)
	private List<SINHVIEN> sinhviens;
	
	public String getMALOP() {
		return MALOP;
	}
	public void setMALOP(String mALOP) {
		MALOP = mALOP;
	}
	public String getTENLOP() {
		return TENLOP;
	}
	public void setTENLOP(String tENLOP) {
		TENLOP = tENLOP;
	}
	public NHANVIEN getNhanvien() {
		return nhanvien;
	}
	public void setNhanvien(NHANVIEN nhanvien) {
		this.nhanvien = nhanvien;
	}
	public List<SINHVIEN> getSinhviens() {
		return sinhviens;
	}
	public void setSinhvien(List<SINHVIEN> sinhviens) {
		this.sinhviens = sinhviens;
	}
	public int getSyso() {
		return sinhviens.isEmpty()?0:sinhviens.size();
	}
}
