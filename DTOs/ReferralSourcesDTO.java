package com.ps.tenbridge.datahub.dto;

import java.io.Serializable;

public class ReferralSourcesDTO implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6897055177191634659L;
	private int referral_source_id;
	private String description;

	public int getReferral_source_id() {
		return referral_source_id;
	}

	public void setReferral_source_id(int referral_source_id) {
		this.referral_source_id = referral_source_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "CptsDTO [referral_source_id=" + referral_source_id + ", description=" + description + "]";
	}

	public ReferralSourcesDTO(int referral_source_id, String description) {
		super();
		this.referral_source_id = referral_source_id;
		this.description = description;
	}

}
