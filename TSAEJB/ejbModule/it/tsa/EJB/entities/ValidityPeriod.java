package it.tsa.EJB.entities;

import java.util.Set;

import javax.persistence.*;

@Entity
@NamedQuery(name = "ValidityPeriod.getOne", query = "SELECT vp FROM ValidityPeriod vp WHERE vp.id=?1")
public class ValidityPeriod {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int monthDuration;
	private float price;
	
	/*
	 * don't cascade because ValidityPeriod can be deleted while
	 * it is useful to keep the ServicePackage stored.
	 * Lazy because one ValidityPeriod can have many ServicePackage associated
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="servicePkgId")
	private ServicePackage servicePackage;
	
	/*
	 * don't cascade because ValidityPeriod can be deleted while
	 * it is useful to keep the Order stored.
	 * Lazy because one ValidityPeriod can have many Order associated
	 */
	@OneToMany(mappedBy="validityPeriod", fetch = FetchType.LAZY)
	private Set<Order> actualOrders;
	
	public int getId() {
		return id;
	}

	public int getMonthDuration() {
		return monthDuration;
	}

	public void setMonthDuration(int monthDuration) {
		this.monthDuration = monthDuration;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
	

	public ServicePackage getServicePackage() {
		return servicePackage;
	}

	public void setServicePackage(ServicePackage servicePackage) {
		this.servicePackage = servicePackage;
	}

	@Override
	public String toString() {
		return "Month Duration = " + monthDuration + ", Price = " + price;
	}
		
}
