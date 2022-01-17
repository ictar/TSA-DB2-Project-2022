package it.tsa.EJB.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

/**
 * Entity implementation class for Entity: User
 *
 */
@Entity
@NamedQueries({
		@NamedQuery(name = "User.checkCredentials", query = "SELECT u From User u WHERE u.username=?1 and u.password=?2", hints = @QueryHint(name = QueryHints.REFRESH, value = HintValues.TRUE)),
		@NamedQuery(name = "User.checkDuplicateUsername", query = "SELECT u From User u WHERE u.username=?1"),
		@NamedQuery(name = "User.getInsolvents", query = "SELECT u From User u WHERE u.insolventFlag = true") })
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String username;
	private String email;
	private String password;
	private boolean insolventFlag;
	private int numFailedPayments;

	// relationships
	// user -> order
	/*
	 * Eager because Order is what the app cares about. Cascade merge because from
	 * User we want to be able to modify orders, delete because deleting user must
	 * delete also related orders
	 */
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
	private List<Order> orders;

	// user -> auditing
	/*
	 * Lazy because not always required. When we delete User we want to delete also
	 * related info in Auditing table
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<Auditing> audits;

	public User() {
		super();
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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the insolventFlag
	 */
	public boolean isInsolventFlag() {
		return insolventFlag;
	}

	/**
	 * @return the numFailedPayments
	 */
	public int getNumFailedPayments() {
		return numFailedPayments;
	}

	public int getId() {
		return this.id;
	}

	public int failedPayment() {
		insolventFlag = true;
		numFailedPayments++;
		return numFailedPayments;
	}

	public List<Order> getOrders() {
		return this.orders;
	}

	public void addOrder(Order order) {
		orders.add(order);
	}

	public List<Auditing> getAudits() {
		return this.audits;
	}

	public void addAudit(Auditing audit) {
		getAudits().add(audit);
		audit.setUser(this);
	}

	public void decreaseFailedPayments() {
		boolean hasRejectedOrder = false;

		numFailedPayments = 0;
		for (int i = 0; i < orders.size(); i++) {
			if (orders.get(i).isRejectedFlag())
				hasRejectedOrder = true;
		}

		insolventFlag = hasRejectedOrder;
	}
}
