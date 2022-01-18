package it.tsa.EJB.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.tsa.EJB.entities.User;
import it.tsa.EJB.exceptions.CredentialsException;

@Stateless
public class UserService {

	@PersistenceContext(unitName = "TSAEJB")
	private EntityManager em;

	public User checkCredentials(String usrn, String pwd) throws CredentialsException {
		List<User> eList;

		try {
			eList = em.createNamedQuery("Employee.checkCredenetials", User.class).setParameter(1, usrn)
					.setParameter(2, pwd).getResultList();
		} catch (PersistenceException e) {
			throw new CredentialsException("Could not verify credentials of employee " + usrn);
		}

		if (eList.isEmpty()) {
			throw new CredentialsException("Credentials are wrong");
		} else if (eList.size() == 1) {
			return eList.get(0);
		}

		throw new CredentialsException("More than one employee registered with same credentials.");

	}

	public boolean createUser(String username, String pwd, String email) {
		User newUser;
		List<User> uList;

		uList = em.createNamedQuery("User.checkDuplicateUsername", User.class).setParameter(1, username)
				.getResultList();

		if (!uList.isEmpty())
			return false;

		newUser = new User();
		newUser.setUsername(username);
		newUser.setPassword(pwd);
		newUser.setEmail(email);

		try {
			em.merge(newUser);
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public void userInsolvent(User user) throws Exception {
		user.failedPayment();

	}

	public void fixUser(User user) throws Exception {
		user.decreaseFailedPayments();
	}

	public List<User> getInsolventUsers() {
		List<User> uList;

		try {
			uList = em.createNamedQuery("User.getInsolvents", User.class).getResultList();
		} catch (PersistenceException e) {
			return null;
		}

		return uList;
	}
}
