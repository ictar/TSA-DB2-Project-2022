package it.tsa.EJB.services;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.tsa.EJB.entities.ActivationSchedule;
import it.tsa.EJB.entities.Auditing;
import it.tsa.EJB.entities.Order;
import it.tsa.EJB.entities.ServicePackage;
import it.tsa.EJB.entities.User;

@Stateless
public class MiscService {

	@PersistenceContext(unitName = "TSAEJB")
	private EntityManager em;

	public ServicePackage retrieveServicePackage(int servicePackageId) {

		ServicePackage result;

		try {
			result= em.createNamedQuery("ServicePackage.findOne", ServicePackage.class).setParameter(1, servicePackageId)
					.getResultList().get(0);
		} catch (PersistenceException e) {
			return null;
		}
		return result;
	}

	public List<ServicePackage> findAllServicePackages() {
		List<ServicePackage> result;

		try {
			result= em.createNamedQuery("ServicePackage.findAll", ServicePackage.class).getResultList();
		} catch (PersistenceException e) {
			return null;
		}
		return result;
	}

	public ActivationSchedule createActivationSchedule(Order order) throws Exception {
		ActivationSchedule as = new ActivationSchedule();
		LocalDate endDate = order.getStartDate().plusMonths(order.getValidityPeriod().getMonthDuration());
		as.setDateOfAct(order.getStartDate());
		as.setDateOfDeact(endDate);
		as.setOrder(order); //for completeness when adding to db
		return as;
	}

	public void createAuditing(Order order, User user) throws Exception{
		Auditing a = new Auditing();
		a.setUser(user);
		a.setAmount(order.getTotalvalue());
		a.setLastRejectionTime(new Timestamp(System.currentTimeMillis()));
		em.merge(a);
	}
}
