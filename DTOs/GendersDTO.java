package com.ps.tenbridge.datahub.dto;

import java.io.Serializable;

public class GendersDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6499493542019739934L;
	private String GenderId;
	private String GenderName;

	public String getGenderId() {
		return GenderId;
	}

	public void setGenderId(String GenderId) {
		this.GenderId = GenderId;
	}

	public String getGenderName() {
		return GenderName;
	}

	public void setGenderName(String GenderName) {
		this.GenderName = GenderName;
	}

	@Override
	public String toString() {
		return "GendersDTO [GenderId=" + GenderId + ", GenderName=" + GenderName + "]";
	}

	public GendersDTO(String GenderId, String GenderName) {
		super();
		this.GenderId = GenderId;
		this.GenderName = GenderName;
	}

}
