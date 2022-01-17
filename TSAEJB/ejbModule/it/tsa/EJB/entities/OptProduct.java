package it.tsa.EJB.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;


/**
 * Entity implementation class for Entity: OptProd
 *
 */
@Entity
@Table(name="optproduct")
@NamedQuery(name = "OptProduct.findOne", query = "SELECT op FROM OptProduct op WHERE op.id=?1")
public class OptProduct implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;
	private float monthlyFee;
	
	// relationships

	// optProd -> servicePackage
	@ManyToMany(mappedBy="availableOptProds", fetch=FetchType.LAZY)
	private Set<ServicePackage> servicePkgs;
	
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
	public float getMonthlyFee() {
		return monthlyFee;
	}

	/**
	 * @param monthlyFee the monthlyFee to set
	 */
	public void setMonthlyFee(float monthlyFee) {
		this.monthlyFee = monthlyFee;
	}

	/**
	 * @return the servicePkgs
	 */
	public Set<ServicePackage> getServicePkgs() {
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
