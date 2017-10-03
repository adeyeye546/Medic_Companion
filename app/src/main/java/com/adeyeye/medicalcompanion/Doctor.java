package com.adeyeye.medicalcompanion;

import com.google.firebase.database.Exclude;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Doctor implements Serializable {
	@SerializedName("name")
	private String name;
	@SerializedName("email")
	private String email;
	@SerializedName("information")
	private String information;
	@SerializedName("address")
	private String address;
	@SerializedName("education")
	private String education;
	@SerializedName("gender")
	private String gender;
	@SerializedName("history")
	private String history;
	@SerializedName("phoneNumber")
	private String phoneNumber;

    


	public Doctor(){

	}

	public Doctor(String name, String email, String information, String address, String education, String gender, String history, String phoneNumber) {
		this.name = name;
		this.email = email;
		this.information = information;
		this.address = address;
		this.education = education;
		this.gender = gender;
		this.history = history;
		this.phoneNumber = phoneNumber;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    //	@Exclude
//	public Map<String, Object> toMap() {
//		HashMap<String, Object> result = new HashMap<>();
//		result.put("email", mEmail);
//		result.put("hospitallocation", mHospitallocation);
//		result.put("hospitalname", mHospitalname);
//		result.put("image", mImage);
//		result.put("name", mName);
//		return result;
//	}


}
