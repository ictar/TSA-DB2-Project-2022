package it.tsa.EJB.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.tsa.EJB.entities.User;

@Stateless
public class UserService {
	@PersistenceContext(unitName = "TSAEJB")
	protected EntityManager em;
	
	public List<User> getInsolventUsers() {
		List<User> uList;
		
		try {
			uList = em.createNamedQuery("User.getInsolvents", User.class)
					.getResultList();
		} catch (PersistenceException e) {
			return null;
		}
		
		return uList;
	}

}
