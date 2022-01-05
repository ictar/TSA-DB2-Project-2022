package it.tsa.EJB.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ActivationSchedule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private Date dateOfAct;
	private Date dateOfDeact;
	
	@OneToOne
	@JoinColumn(name="orderId")
	private Order order;

	public Date getDateOfAct() {
		return dateOfAct;
	}

	public void setDateOfAct(Date dateOfAct) {
		this.dateOfAct = dateOfAct;
	}

	public Date getDateOfDeact() {
		return dateOfDeact;
	}

	public void setDateOfDeact(Date dateOfDeact) {
		this.dateOfDeact = dateOfDeact;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}
