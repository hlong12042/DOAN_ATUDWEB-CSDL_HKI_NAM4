package ptithcm.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "HOCPHAN")
public class HOCPHAN {
	@Id
	private String MAHP;
	private String TENHP;
	private int SOTC;
	
	@OneToMany(mappedBy = "hocphan", fetch = FetchType.EAGER)
	private List<BANGDIEM> bangdiems;

	public String getMAHP() {
		return MAHP;
	}

	public void setMAHP(String mAHP) {
		MAHP = mAHP;
	}

	public String getTENHP() {
		return TENHP;
	}

	public void setTENHP(String tENHP) {
		TENHP = tENHP;
	}

	public List<BANGDIEM> getBangdiems() {
		return bangdiems;
	}

	public void setBangdiems(List<BANGDIEM> bangdiems) {
		this.bangdiems = bangdiems;
	}

	public int getSOTC() {
		return SOTC;
	}

	public void setSOTC(int sOTC) {
		SOTC = sOTC;
	}
	
	
}
