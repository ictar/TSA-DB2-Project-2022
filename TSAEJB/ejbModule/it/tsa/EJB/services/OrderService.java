package it.tsa.EJB.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.tsa.EJB.entities.OptProduct;
import it.tsa.EJB.entities.Order;
import it.tsa.EJB.entities.ServicePackage;
import it.tsa.EJB.entities.User;
import it.tsa.EJB.entities.ValidityPeriod;
import it.tsa.EJB.exceptions.CreationException;

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

	public Order createOrder(User user, int chosenSP, int chosenVP, List<Integer> chosenOP, LocalDate startDate)
			throws CreationException {

		try {
			HashSet<OptProduct> chosenOptProds = new HashSet<OptProduct>();
			Order newOrder = new Order();
			LocalDateTime rightNow = LocalDateTime.now(ZoneId.of("GMT+1"));
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
			newOrder.setDateOfCreation(rightNow.toLocalDate());
			newOrder.setHourOfCreation(rightNow.get(ChronoField.HOUR_OF_DAY));
			newOrder.setRejectedFlag(false);
			newOrder.computeTotalValue();
			newOrder.setValidityFlag(false);
			newOrder.setStartDate(startDate);

			// dont add order to user because this creates only Order instance
			return newOrder;
		} catch (Exception e) {
			throw new CreationException("Order");
		}
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
		} else
			userService.userInsolvent(user);

		em.merge(order);
		em.flush();
	}

	private void setOrderValidity(Order order, boolean valid) {
		order.setValidityFlag(valid);
		order.setRejectedFlag(!valid);
	}

	public List<Order> getAllSuspendedOrders() {
		List<Order> oList;

		try {
			oList = em.createNamedQuery("Order.getSuspended", Order.class).getResultList();
		} catch (PersistenceException e) {
			return null;
		}

		return oList;
	}
}
