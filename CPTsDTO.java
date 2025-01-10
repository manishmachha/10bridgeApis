package com.ps.tenbridge.datahub.dto;

import java.io.Serializable;

public class CPTsDTO implements Serializable {	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String description;


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "CptsDTO [code=" + code + ", description=" + description + "]";
	}

	public CPTsDTO(String code, String description) {
		super();
		this.code = code;
		this.description = description;
	}

}
