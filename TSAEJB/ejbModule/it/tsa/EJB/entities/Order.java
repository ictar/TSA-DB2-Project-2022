package it.tsa.EJB.entities;


import java.time.LocalTime;
import java.util.Date;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "orders")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@OneToOne
	@JoinColumn(name="activationScheduleId")
	private ActivationSchedule activationSchedule;
	
	@ManyToOne
	@JoinColumn(name = "validityPeriodId")
	private ValidityPeriod validityPeriod;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;
	
	@ManyToOne
	@JoinColumn(name="servicePkgId")
	private ServicePackage servicePackage;
	
	@ManyToMany
	@JoinTable(
			name = "chosenOptProd",
			joinColumns= @JoinColumn(name ="orderId"),
			inverseJoinColumns = @JoinColumn(name ="optProdId")
	)
	private Set<OptProduct> chosenOptProds;
		
	private Date dateOfCreation;
	public int getId() {
		return id;
	}

	public ActivationSchedule getActivationSchedule() {
		return activationSchedule;
	}

	public void setActivationSchedule(ActivationSchedule activationSchedule) {
		this.activationSchedule = activationSchedule;
	}

	public ValidityPeriod getValidityPeriod() {
		return validityPeriod;
	}

	public void setValidityPeriod(ValidityPeriod validityPeriod) {
		this.validityPeriod = validityPeriod;
	}

	public ServicePackage getServicePackage() {
		return servicePackage;
	}

	public void setServicePackage(ServicePackage servicePackage) {
		this.servicePackage = servicePackage;
	}

	public Set<OptProduct> getChosenOptProds() {
		return chosenOptProds;
	}

	public void setChosenOptProds(Set<OptProduct> chosenOptProds) {
		this.chosenOptProds = chosenOptProds;
	}

	public Date getDateOfCreation() {
		return dateOfCreation;
	}

	public void setDateOfCreation(Date dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}

	public int getHourOfCreation() {
		return hourOfCreation;
	}

	public void setHourOfCreation(int hourOfCreation) {
		this.hourOfCreation = hourOfCreation;
	}

	public float getTotalvalue() {
		return totalvalue;
	}

	public void setTotalvalue(float totalvalue) {
		this.totalvalue = totalvalue;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public boolean isValidityFlag() {
		return validityFlag;
	}

	public void setValidityFlag(boolean validityFlag) {
		this.validityFlag = validityFlag;
	}

	public boolean isRejectedFlag() {
		return rejectedFlag;
	}

	public void setRejectedFlag(boolean rejectedFlag) {
		this.rejectedFlag = rejectedFlag;
	}

	public User getUser() {
		return user;
	}

	private int hourOfCreation;
	private float totalvalue;
	private Date startDate;
	private boolean validityFlag;
	private boolean rejectedFlag;
	
	public void setUser(User user) {
		this.user = user;
		}
	
}