package com.ps.tenbridge.datahub.dto;

import java.io.Serializable;

public class ChangeReasonsDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9096434082835918116L;
	private String change_reason_id;
	private String description;

	public ChangeReasonsDTO(String change_reason_id, String description) {
		super();
		this.change_reason_id = change_reason_id;
		this.description = description;
	}

	public String getChange_reason_id() {
		return change_reason_id;
	}

	public void setChange_reason_id(String change_reason_id) {
		this.change_reason_id = change_reason_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "ChangeReasonsDTO [change_reason_id=" + change_reason_id + ", description=" + description + "]";
	}

}
