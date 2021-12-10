package it.tsa.EJB.services;

import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import it.tsa.EJB.entities.Service;
import it.tsa.EJB.exceptions.CreationException;

@Stateless
public class ServiceService {
	
	@PersistenceContext(unitName = "TSAEJB")
	protected EntityManager em;

	public  Set<Service> findAllService() {
		Query qry = em.createQuery("SELECT s FROM Service s");
		return Set.copyOf(qry.getResultList());
	}
	
	public void createAService(String name, int includedMin, int includedSMS, int includedGB, float extraMinFee, float extraSMSFee, float extraGBFee) throws CreationException{
		Service srv = new Service();
		srv.setName(name);
		srv.setIncludedMin(includedMin);
		srv.setIncludedGB(includedGB);
		srv.setIncludedSMS(includedSMS);
		srv.setExtraGBFee(extraGBFee);
		srv.setExtraMinFee(extraMinFee);
		srv.setExtraSMSFee(extraSMSFee);
		
		try {
			em.persist(srv);
			em.flush();
		} catch (Exception e) {
			throw new CreationException(e.getMessage());
		}
	}
}
