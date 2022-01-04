package it.tsa.EJB.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: User
 *
 */
@Entity
@NamedQuery(name = "User.checkCredentials", query = "SELECT u From User u WHERE u.username=?1 and u.password=?2")
@NamedQuery(name = "User.getInsolvents", query = "SELECT u From User u WHERE u.insolventFlag = 1")
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
	@OneToMany(fetch=FetchType.EAGER, mappedBy="user",
			cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
	private List<Order> orders;
	
	
	// user -> auditing
	@OneToMany(fetch=FetchType.LAZY, mappedBy = "user")
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
	 * @param  insolventFlag the insolventFlag to set
	 */
	public void setInsolvenetFlag(boolean flag) {
		this.insolventFlag = flag;
	}
	/**
	 * @return the numFailedPayments
	 */
	public int getNumFailedPayments() {
		return numFailedPayments;
	}
	/**
	 * @param numberOfFailedPayments the numberOfFailedPayments to set
	 */
	public void setNumFailedPayments(int numFailedPayments) {
		this.numFailedPayments = numFailedPayments;
	}
	public int getId() {
		return this.id;
	}
	
	public String toString() {
		return "User id: " + getId() + " name: " + getUsername();
	}
	
	public List<Order> getOrders(){
		return this.orders;
	}
   
	public void addOrder(Order order) {
		getOrders().add(order);
		order.setUser(this);
	}
	
	public List<Auditing> getAudits(){
		return this.audits;
	}
	
	public void addAudit(Auditing audit) {
		getAudits().add(audit);
		audit.setUser(this);
	}
}
