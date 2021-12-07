package it.tsa.EJB.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.*;


/**
 * Entity implementation class for Entity: OptProd
 *
 */
@Entity

public class OptProduct implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;
	
	private String name;
	private int monthlyFee;
	
	// relationships

	// optProd -> servicePackage
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
			name="optprodinpkg",
			joinColumns={@JoinColumn(name="optProdId")},
			inverseJoinColumns={@JoinColumn(name="servicePkgId")}
			)
	private List<ServicePackage> servicePkgs;
	
	// optProduct -> Order
	@ManyToMany(mappedBy="chosenOptProds", fetch=FetchType.LAZY)
	private Set<Order> orders;
	
	public OptProduct() {
		super();
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the monthlyFee
	 */
	public int getMonthlyFee() {
		return monthlyFee;
	}

	/**
	 * @param monthlyFee the monthlyFee to set
	 */
	public void setMonthlyFee(int monthlyFee) {
		this.monthlyFee = monthlyFee;
	}

	/**
	 * @return the servicePkgs
	 */
	public List<ServicePackage> getServicePkgs() {
		return servicePkgs;
	}

	/**
	 * @param servicePkgs the servicePkgs to set
	 */
/*	public void setServicePkgs(List<ServicePackage> servicePkg) {
		this.getServicePkgs().add(servicePkg);
		servicePkg.getOptProd().add(this);
	}
*/
	/**
	 * @return the orders
	 */
	public Set<Order> getOrders() {
		return orders;
	}

	/**
	 * @param orders the orders to set
	 */
/*	public void setOrders(List<Order> order) {
		this.getOrders().add(order);
		order.getOptProd().add(this);
	}
*/
	
}
