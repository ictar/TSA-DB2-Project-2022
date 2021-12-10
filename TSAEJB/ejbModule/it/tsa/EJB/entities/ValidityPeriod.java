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
	
	@ManyToOne
	@JoinColumn(name ="servicePackageId")
	private ServicePackage servicePackage;
	
	@OneToMany(mappedBy="validityPeriod")
	private Set<Order> actualOrders;
	
	public int getId() {
		return id;
	}
	
	public int getMonthDuration() {
		return monthDuration;
	}
	
	public float getPrice() {
		return price;
	}
}
