package it.tsa.EJB.entities;

import javax.persistence.*;

import java.util.Set;

@Entity(name="servicePkg")
@Table(name="servicePkg")
@NamedQuery(name = "ServicePackage.findAll", query = "SELECT sp FROM servicePkg sp")
public class ServicePackage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;

	@OneToMany(mappedBy = "servicePackage")
	private Set<ValidityPeriod> validityPeriods;

	@ManyToMany
	@JoinTable(name = "servicesInPkg", joinColumns = @JoinColumn(name = "servicePkgId"), inverseJoinColumns = @JoinColumn(name = "serviceId"))
	/*
	 * would place this but generates error in Service
	 * 
	 * @Basic(fetch = FetchType.LAZY)
	 */
	private Set<Service> availableServices;

	@ManyToMany(mappedBy="servicePkgs", fetch=FetchType.EAGER)
	private Set<OptProduct> availableOptProds;

	@OneToMany(mappedBy = "servicePackage")
	private Set<Order> orders;
	
	public void setName(String name) {
		this.name = name;
	}

	public Set<ValidityPeriod> getValidityPeriods() {
		return validityPeriods;
	}

	public void setValidityPeriods(Set<ValidityPeriod> validityPeriods) {
		this.validityPeriods = validityPeriods;
	}

	public Set<Service> getAvailableServices() {
		return availableServices;
	}

	public void setAvailableServices(Set<Service> availableServices) {
		this.availableServices = availableServices;
	}

	public Set<OptProduct> getAvailableOptProds() {
		return availableOptProds;
	}

	public void setAvailableOptProds(Set<OptProduct> availableOptProds) {
		this.availableOptProds = availableOptProds;
	}

	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	public String getName() {
		return name;
	}
}	
