package it.tsa.EJB.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.tsa.EJB.entities.Order;

@Stateless
public class OrderService {
	@PersistenceContext(unitName = "TSAEJB")
	protected EntityManager em;

	public List<Order> getAllSuspendedOrders() {
		List<Order> oList;
		
		try {
			oList = em.createNamedQuery("Order.getSuspended", Order.class)
					.getResultList();
		} catch (PersistenceException e) {
			return null;
		}
		
		return oList;
	}
}
