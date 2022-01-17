package it.tsa.EJB.entities;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "orders")
@NamedQueries({ @NamedQuery(name = "Order.getSuspended", query = "SELECT o From Order o WHERE o.rejectedFlag = true"),
		@NamedQuery(name = "Order.getUserOrders", query = "SELECT o FROM Order o WHERE o.user=?1") })
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private float totalValue;
	private LocalDate startDate;
	private LocalDate dateOfCreation;
	private int hourOfCreation;
	private boolean validityFlag;
	private boolean rejectedFlag;

	/*
	 * fetch Eager because it is a simple data and can be easily retrieved. Don't
	 * cascade because we don't want that modifications made on Order to affect
	 * ValidityPeriod
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "validityPeriodId")
	private ValidityPeriod validityPeriod;

	/*
	 * Fetch Eager because the application focuses on User's orders, so it can be
	 * useful to have them both retrieved together Don't cascade because User and
	 * Order modifications must be separate
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userId")
	private User user;

	/*
	 * Fetch Lazy because we don't always need to know what ServicePackage is in
	 * Order Don't cascade because we don't want to allow ServicePackage
	 * modification from Order
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "servicePkgId")
	private ServicePackage servicePackage;

	/*
	 * Fetch Lazy because we don't always need to know what OptProduct is in Order
	 * Don't cascade because we don't want to allow OptProduct5 modification from
	 * Order
	 */
	@ManyToMany(cascade = CascadeType.REMOVE)
	@JoinTable(name = "chosenOptProd", joinColumns = @JoinColumn(name = "orderId"), inverseJoinColumns = @JoinColumn(name = "optProdId"))
	private Set<OptProduct> chosenOptProds;

	@OneToOne(cascade = CascadeType.PERSIST, mappedBy = "order")
	private ActivationSchedule activationSchedule;

	public ActivationSchedule getActivationSchedule() {
		return activationSchedule;
	}

	public void setActivationSchedule(ActivationSchedule actSched) {
		activationSchedule = actSched;
	}

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

	public LocalDate getDateOfCreation() {
		return dateOfCreation;
	}

	public void setDateOfCreation(LocalDate dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}

	public int getHourOfCreation() {
		return hourOfCreation;
	}

	public void setHourOfCreation(int hourOfCreation) {
		this.hourOfCreation = hourOfCreation;
	}

	public float getTotalvalue() {
		return totalValue;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
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

	public float computeTotalValue() {

		totalValue = validityPeriod.getPrice();
		totalValue = chosenOptProds.stream().map(product -> product.getMonthlyFee()).reduce(totalValue,
				(a, b) -> a + b);
		totalValue *= validityPeriod.getMonthDuration();
		return totalValue;
	}
}