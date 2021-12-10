package it.tsa.EJB.entities;


import java.time.LocalTime;
import java.util.Date;
import java.util.Set;

import javax.persistence.*;


@Entity
@Table(name="orders")
public class Order {
	//last class to check
	
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
	private LocalTime hourOfCreation;
	private float totalvalue;
	private Date startDate;
	private boolean validityFlag;
	private boolean rejectedFlag;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
	
}