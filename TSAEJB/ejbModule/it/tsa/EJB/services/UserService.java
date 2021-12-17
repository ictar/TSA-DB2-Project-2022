package it.tsa.EJB.services;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.tsa.EJB.entities.User;
import it.tsa.EJB.exceptions.LoginErrorException;

@Stateless
public class UserService {

	@PersistenceContext(unitName = "TSAEJB")
	private EntityManager em;

	public User checkCredentials(String usrn, String pwd) throws LoginErrorException {
		List<User> uList = null;
		try {
			uList = em.createNamedQuery("User.checkCredentials", User.class).setParameter(1, usrn).setParameter(2, pwd)
					.getResultList();
		} catch (PersistenceException e) {
			throw new LoginErrorException();
		}
		if (uList.size() == 1)
			return uList.get(0);
		else
			throw new LoginErrorException();
	}

	public boolean createUser(String username, String pwd, String email) {

		List<User> uList;

		uList = em.createNamedQuery("User.checkDuplicateUsername", User.class).setParameter(1, username)
				.getResultList();

		if (uList.isEmpty()) {
			User newUser = new User();
			newUser.setUsername(username);
			newUser.setPassword(pwd);
			newUser.setEmail(email);

			em.persist(newUser);
			em.flush();
			return true;
		} else
			return false;

	}

	public void userInsolvent(User user) {
		User a = em.find(User.class, user.getId());
		a.setInsolvenetFlag(true);
		em.merge(a);
	}
}
