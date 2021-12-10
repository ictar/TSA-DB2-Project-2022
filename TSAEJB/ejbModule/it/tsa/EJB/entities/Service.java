package it.tsa.EJB.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Service {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToMany(mappedBy="availableServices", fetch = FetchType.EAGER)
	private Set<ServicePackage> servicePackageRelated;
	
	private String name;
	private int includedMin;
	private int includedSMS;
	private int includedGB;
	private float extraMinFee;
	private float extraSMSFee;
	private float extraGBFee;

	public Set<ServicePackage> getServicePackageRelated() {
		return servicePackageRelated;
	}
	public void setServicePackageRelated(Set<ServicePackage> servicePackageRelated) {
		this.servicePackageRelated = servicePackageRelated;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIncludedMin() {
		return includedMin;
	}
	public void setIncludedMin(int includedMin) {
		this.includedMin = includedMin;
	}
	public int getIncludedSMS() {
		return includedSMS;
	}
	public void setIncludedSMS(int includedSMS) {
		this.includedSMS = includedSMS;
	}
	public int getIncludedGB() {
		return includedGB;
	}
	public void setIncludedGB(int includedGB) {
		this.includedGB = includedGB;
	}
	public float getExtraMinFee() {
		return extraMinFee;
	}
	public void setExtraMinFee(float extraMinFee) {
		this.extraMinFee = extraMinFee;
	}
	public float getExtraSMSFee() {
		return extraSMSFee;
	}
	public void setExtraSMSFee(float extraSMSFee) {
		this.extraSMSFee = extraSMSFee;
	}
	public float getExtraGBFee() {
		return extraGBFee;
	}
	public void setExtraGBFee(float extraGBFee) {
		this.extraGBFee = extraGBFee;
	}

}
