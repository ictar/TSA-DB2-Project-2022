package it.polimi.db2.telco.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Auditing
 *
 */
@Entity

public class Auditing implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String username;
	private String email;
	private int amount;
	private Timestamp lastRejectionTIme;
	
	@ManyToOne
	@JoinColumn(name="userId")
	private int userId;
	
	public Auditing() {
		super();
	}
	
	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userid) {
		this.userId = userid;
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
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * @return the lastRejectionTIme
	 */
	public Timestamp getLastRejectionTIme() {
		return lastRejectionTIme;
	}

	/**
	 * @param lastRejectionTIme the lastRejectionTIme to set
	 */
	public void setLastRejectionTIme(Timestamp lastRejectionTIme) {
		this.lastRejectionTIme = lastRejectionTIme;
	}
	
	
   
}
