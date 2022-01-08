package it.tsa.EJB.entities;


import java.util.Date;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "orders")
@NamedQuery(name = "Order.getSuspended", query = "SELECT o From Order o WHERE o.rejectedFlag = true")
@NamedQuery(name = "Order.getUserOrders", query = "SELECT o FROM Order o WHERE o.user=?1")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private int hourOfCreation;
	private float totalvalue;
	private Date startDate;
	private boolean validityFlag;
	private boolean rejectedFlag;
	
	/*
	 * fetch Eager because it is a simple data and can be easily retrieved.
	 * Don't cascade because we don't want that modifications made on Order to affect 
	 * ValidityPeriod
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "validityPeriodId")
	private ValidityPeriod validityPeriod;
	
	/*
	 * Fetch Eager because the application focuses on User's orders,
	 * so it can be useful to have them both retrieved together
	 * Don't cascade because User and Order modifications must be separate
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userId")
	private User user;
	
	/*
	 * Fetch Lazy because we don't always need to know what ServicePackage is
	 * in Order
	 * Don't cascade because we don't want to allow ServicePackage modification
	 * from Order
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="servicePkgId")
	private ServicePackage servicePackage;
	
	/*
	 * Fetch Lazy because we don't always need to know what OptProduct is
	 * in Order
	 * Don't cascade because we don't want to allow OptProduct5 modification
	 * from Order
	 */
	@ManyToMany
	@JoinTable(
			name = "chosenOptProd",
			joinColumns= @JoinColumn(name ="orderId"),
			inverseJoinColumns = @JoinColumn(name ="optProdId")
	)
	private Set<OptProduct> chosenOptProds;
		
	private Date dateOfCreation;
	public int getId() {
		return id;
	}
	
	public ValidityPeriod getValidityPeriod() {
		return validityPeriod;
	}

	public void setValidityPeriod(ValidityPeriod validityPeriod) {
		this.validityPeriod = validityPeriod;
	}

	public ServicePackage getServicePackage() {
		return servicePackage;
	}

	public void setServicePackage(ServicePackage servicePackage) {
		this.servicePackage = servicePackage;
	}

	public Set<OptProduct> getChosenOptProds() {
		return chosenOptProds;
	}

	public void setChosenOptProds(Set<OptProduct> chosenOptProds) {
		this.chosenOptProds = chosenOptProds;
	}

	public Date getDateOfCreation() {
		return dateOfCreation;
	}

	public void setDateOfCreation(Date dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}

	public int getHourOfCreation() {
		return hourOfCreation;
	}

	public void setHourOfCreation(int hourOfCreation) {
		this.hourOfCreation = hourOfCreation;
	}

	public float getTotalvalue() {
		return totalvalue;
	}

	public void setTotalvalue(float totalvalue) {
		this.totalvalue = totalvalue;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public boolean isValidityFlag() {
		return validityFlag;
	}

	public void setValidityFlag(boolean validityFlag) {
		this.validityFlag = validityFlag;
	}

	public boolean isRejectedFlag() {
		return rejectedFlag;
	}

	public void setRejectedFlag(boolean rejectedFlag) {
		this.rejectedFlag = rejectedFlag;
	}

	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
		}
	
	public float computeTotalCost() {

		float totalCost = validityPeriod.getPrice();
//		System.out.println("PRICE: ValPer " + totalCost);
		/*chosenOptProds.stream().forEach(op -> {
			System.out.println("PRICE: Single optProd" + op.getMonthlyFee());
		});
	*/	totalCost = chosenOptProds.stream().map(product -> product.getMonthlyFee()).reduce(totalCost,
				(a, b) -> a + b);
//		System.out.println("PRICE: singleMonth " + totalCost);
		totalCost *= validityPeriod.getMonthDuration();
//		System.out.println("PRICE: total " + totalCost);
		return totalCost;
	}
}