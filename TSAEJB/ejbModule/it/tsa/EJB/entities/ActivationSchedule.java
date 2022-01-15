package it.tsa.EJB.entities;

import javax.persistence.*;

import java.time.LocalDate;

@Entity
public class ActivationSchedule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private LocalDate dateOfActivation;
	private LocalDate dateOfDeactivation;
	
	/*
	 * don't cascade because activationSchedule can be deleted
	 * without having to delete the order, other operations must be performed
	 * directly on Order.
	 * Eager because it is useful to have immediately, it is not very heavy (?)
	 */
	@OneToOne(fetch= FetchType.EAGER)
	@JoinColumn(name="orderId")
	private Order order;

	public LocalDate getDateOfAct() {
		return dateOfActivation;
	}

	public void setDateOfAct(LocalDate dateOfAct) {
		this.dateOfActivation = dateOfAct;
	}

	public LocalDate getDateOfDeact() {
		return dateOfDeactivation;
	}

	public void setDateOfDeact(LocalDate dateOfDeact) {
		this.dateOfDeactivation = dateOfDeact;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}
