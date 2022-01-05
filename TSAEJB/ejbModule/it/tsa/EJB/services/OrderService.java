package it.tsa.EJB.services;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.tsa.EJB.entities.OptProduct;
import it.tsa.EJB.entities.Order;
import it.tsa.EJB.entities.ServicePackage;
import it.tsa.EJB.entities.User;
import it.tsa.EJB.entities.ValidityPeriod;

@Stateless
public class OrderService {

	@PersistenceContext(unitName = "TSAEJB")
	private EntityManager em;

	@EJB
	private DbService dbService;

	@EJB
	private UserService userService;

	public Order getOrder(User user) {
		return em.createNamedQuery("Order.getUserOrders", Order.class).setParameter(1, user).getResultList().get(0);
	}

	public Order createOrder(User user, int chosenSP, int chosenVP, List<Integer> chosenOP, Date startDate) {
		HashSet<OptProduct> chosenOptProds = new HashSet<OptProduct>();
		Order newOrder = new Order();
		Date date = new Date();
		newOrder.setUser(user);

		// TODO is findOne/getOne the right implementation?
		newOrder.setValidityPeriod(em.createNamedQuery("ValidityPeriod.getOne", ValidityPeriod.class)
				.setParameter(1, chosenVP).getResultList().get(0));
		newOrder.setServicePackage(em.createNamedQuery("ServicePackage.findOne", ServicePackage.class)
				.setParameter(1, chosenSP).getResultList().get(0));

		for (Integer optProd : chosenOP) {
			chosenOptProds.add(em.createNamedQuery("OptProduct.findOne", OptProduct.class).setParameter(1, optProd)
					.getResultList().get(0));
		}

		newOrder.setChosenOptProds(chosenOptProds);
		newOrder.setDateOfCreation(date);
		newOrder.setHourOfCreation(date.getHours());
		newOrder.setRejectedFlag(false);
		newOrder.setTotalvalue(newOrder.computeTotalCost());
		newOrder.setValidityFlag(true);
		newOrder.setStartDate(startDate);

		//dont add order to user because this creates only Order instance
		System.out.println("Start date in orderservice createorder" + newOrder.getStartDate());
		return newOrder;
	}

	public void confirmOrder(Order order) {
		em.persist(order);
		em.flush();
	}

	public void addOrder(Order order, User user, boolean activated) {
		setOrderValidity(order, activated);
		em.merge(order);
		if (activated)
			dbService.createActivationSchedule(order);
		else
			userService.userInsolvent(user);

		user.addOrder(order);

		em.flush();
	}

	public void fixOrder(Order order, User user, boolean activated) {
		if (activated) {
			setOrderValidity(order, activated);
			em.merge(order);
			userService.fixUser(user);
		}
		else
			userService.userInsolvent(user);

		em.merge(order);
		em.flush();
	}
	
	private void setOrderValidity(Order order, boolean valid) {
		order.setValidityFlag(valid);
		order.setRejectedFlag(!valid);
	}
}
