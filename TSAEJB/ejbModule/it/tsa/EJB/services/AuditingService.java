package it.tsa.EJB.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.tsa.EJB.entities.Auditing;

@Stateless
public class AuditingService {
	@PersistenceContext(unitName = "TSAEJB")
	protected EntityManager em;

	public List<Auditing> getAllAuditings() {
		List<Auditing> aList;
		
		try {
			aList = em.createNamedQuery("Auditing.findAll", Auditing.class)
					.getResultList();
		} catch (PersistenceException e) {
			return null;
		}
		
		return aList;
	}
}
