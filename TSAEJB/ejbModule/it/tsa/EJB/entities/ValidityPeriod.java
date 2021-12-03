package it.tsa.EJB.entities;

import java.util.Set;

import javax.persistence.*;

@Entity
public class ValidityPeriod {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int durationMonths;
	private float price;
	
	@ManyToOne
	@JoinColumn(name ="validityPeriodId")
	private ServicePackage servicePackage;
	
	@OneToMany(mappedBy="validityPeriod")
	private Set<Order> actualOrders;
}
