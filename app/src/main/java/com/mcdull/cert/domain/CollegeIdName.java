package com.mcdull.cert.domain;

public class CollegeIdName {
	String college_id;
	String college_name;

	public String getCollege_id() {
		return college_id;
	}

	public void setCollege_id(String college_id) {
		this.college_id = college_id;
	}

	public String getCollege_name() {
		return college_name;
	}

	public void setCollege_name(String college_name) {
		this.college_name = college_name;
	}

	public CollegeIdName(String college_id, String college_name) {
		super();
		this.college_id = college_id;
		this.college_name = college_name;
	}

	public CollegeIdName() {
		super();
		// TODO Auto-generated constructor stub
	}
}
