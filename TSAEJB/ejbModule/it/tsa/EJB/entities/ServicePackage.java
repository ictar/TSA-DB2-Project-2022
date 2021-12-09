package it.tsa.EJB.entities;

import javax.persistence.*;

import java.util.Set;

@Entity
@Table(name="ServicePkg")
@NamedQuery(name = "ServicePackage.findAll", query = "SELECT sp FROM ServicePackage sp")
@NamedQuery(name = "ServicePackage.findOne", query = "SELECT sp FROM ServicePackage sp WHERE sp.id=?1")
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
	
	public int getId() {
		return id;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public Set<Service> getavailableServices() {
		return availableServices;
	}
	
	public Set<OptProduct> getAvailableOptProds(){
		return availableOptProds;
	}
	public Set<ValidityPeriod> getValidityPeriods(){
		return validityPeriods;
	}
}	