package it.tsa.EJB.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Auditing
 *
 */
@Entity
@NamedQueries ({
	@NamedQuery(name="Auditing.findAll", query="SELECT a from Auditing a")
})
public class Auditing implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String username;
	private String email;
	private float amount;
	private Timestamp lastRejectionTime;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="userID")
	private User user;
	
	public Auditing() {
		super();
	}
	
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
		this.username = user.getUsername();
		this.email = user.getEmail();
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}


	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the amount
	 */
	public float getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(float amount) {
		this.amount = amount;
	}

	/**
	 * @return the lastRejectionTIme
	 */
	public Timestamp getLastRejectionTime() {
		return lastRejectionTime;
	}

	/**
	 * @param lastRejectionTIme the lastRejectionTIme to set
	 */
	public void setLastRejectionTime(Timestamp lastRejectionTIme) {
		this.lastRejectionTime = lastRejectionTIme;
	}
	
	
   
}
