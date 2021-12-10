package it.tsa.EJB.services;

import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import it.tsa.EJB.entities.OptProduct;
import it.tsa.EJB.entities.ValidityPeriod;
import it.tsa.EJB.exceptions.CreationException;

@Stateless
public class ValidityPeriodService {
	@PersistenceContext(unitName = "TSAEJB")
	protected EntityManager em;
	
	
	public void createAValidityPeriod(int duration, float price) throws CreationException{
		ValidityPeriod vp = new ValidityPeriod();
		vp.setMonthDuration(duration);
		vp.setPrice(price);
		
		try {
			em.persist(vp);
			em.flush();
		} catch (Exception e) {
			throw new CreationException(e.getMessage());
		}
	}
	
	public  Set<ValidityPeriod> findAllValidityPeriods() {
		Query qry = em.createQuery("SELECT vp FROM ValidityPeriod vp");
		return Set.copyOf(qry.getResultList());		
	}
}
