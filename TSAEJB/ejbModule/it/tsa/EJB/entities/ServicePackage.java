package it.tsa.EJB.entities;

import javax.persistence.*;

import it.tsa.EJB.entities.OptionalProduct;

import java.util.Set;

@Entity
@NamedQuery(name = "ServicePackage.findAll", query = "SELECT sp FROM ServicePackage sp")
public class ServicePackage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;

	@OneToMany(mappedBy = "servicePackage")
	private Set<ValidityPeriod> validityPeriods;

	@ManyToMany
	@JoinTable(name = "servicesInPkg", joinColumns = @JoinColumn(name = "servicePackageId"), inverseJoinColumns = @JoinColumn(name = "serviceId"))
	/*
	 * would place this but generates error in Service
	 * 
	 * @Basic(fetch = FetchType.LAZY)
	 */
	private Set<Service> availableServices;

	@ManyToMany(mappedBy="servicePkgs", fetch=FetchType.EAGER)
	private Set<OptionalProduct> availableOptProds;

	@OneToMany(mappedBy = "servicePackage")
	private Set<Order> orders;
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}	