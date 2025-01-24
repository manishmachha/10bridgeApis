package com.ps.tenbridge.datahub.dto;

import java.io.Serializable;

public class CancelReasonsDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2788011168503493615L;
	private String cancellation_reason_id;
	private String description;

	public String getCancellation_reason_id() {
		return this.cancellation_reason_id;
	}

	public void setCancellation_reason_id(String cancellation_reason_id) {
		this.cancellation_reason_id = cancellation_reason_id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "CancelReasonsDTO [ cancellation_reason_id=" + getCancellation_reason_id() + ", description="
				+ getDescription() + "]";
	}

	public CancelReasonsDTO(String cancellation_reason_id, String description) {
		super();
		this.cancellation_reason_id = cancellation_reason_id;
		this.description = description;
	}

}
