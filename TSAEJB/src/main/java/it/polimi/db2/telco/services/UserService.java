package it.polimi.db2.telco.model;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class UserService {
	protected EntityManager em;
	
	public UserService(EntityManager em) {
		this.em = em;
	}
	
	public User createUser(String name, String email, String password) {
		User u = new User();
		u.setUsername(name);
		u.setEmail(email);
		u.setPassword(password);
		em.persist(u);
		return u;
	}
	
	public User findUser(int id) {
		return em.find(User.class, id);
	}
	public Collection<User> findAllUsers() {
		TypedQuery query = em.createQuery("SELECT u FROM User u", User.class);
		return query.getResultList();
	}
}
