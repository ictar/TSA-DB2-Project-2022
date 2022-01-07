package it.tsa.EJB.entities;

import javax.persistence.*;

import java.util.HashSet;
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

	
	//@ManyToMany(mappedBy="servicePkgs", fetch=FetchType.EAGER)
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
			name="optprodinpkg",
			joinColumns={@JoinColumn(name="servicePkgId")},
			inverseJoinColumns={@JoinColumn(name="optProdId")}
			)
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


	public Set<Service> getAvailableServices() {
		return availableServices;
	}

	public void setAvailableServices(Set<Service> availableServices) {
		this.availableServices = availableServices;
	}

	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	public void setValidityPeriods(Set<ValidityPeriod> validityPeriods) {
		this.validityPeriods = validityPeriods;
	}

	public void setAvailableOptProds(Set<OptProduct> availableOptProds) {
		this.availableOptProds = availableOptProds;
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
	
	public void addValidityPeriod(ValidityPeriod vp) {
		if(validityPeriods == null) {
			validityPeriods = new HashSet<ValidityPeriod> ();
		}
		validityPeriods.add(vp);
		vp.setServicePackage(this);
	}
	
	public String toString() {
		return "ServicePackage id: " + getId() + " name: " + getName();
	}
}	
