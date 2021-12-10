package it.tsa.EJB.entities;

import java.util.Set;

import javax.persistence.*;

@Entity
public class ValidityPeriod {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int monthDuration;
	private float price;
	
	@ManyToOne
	@JoinColumn(name ="servicePkgId")
	private ServicePackage servicePackage;
	
	@OneToMany(mappedBy="validityPeriod")
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

	@Override
	public String toString() {
		return "Month Duration = " + monthDuration + ", Price = " + price;
	}
		
}
