package it.tsa.EJB.services;

import java.util.HashSet;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.tsa.EJB.entities.OptProduct;
import it.tsa.EJB.entities.Service;
import it.tsa.EJB.entities.ServicePackage;
import it.tsa.EJB.entities.ValidityPeriod;
import it.tsa.EJB.exceptions.CreationException;

@Stateless
public class ServicePkgService {
	@PersistenceContext(unitName = "TSAEJB")
	protected EntityManager em;
	
	
	public void createAServicePkg(String name, int[] vpids, int[] sids, int[] pids) throws CreationException{
		ServicePackage svcPkg = new ServicePackage();
		svcPkg.setName(name);
		
		// retrieve validity periods
		for (int id: vpids) {
			ValidityPeriod vp = em.find(ValidityPeriod.class, id);
			svcPkg.addValidityPeriod(vp);
		}
		if(svcPkg.getValidityPeriods() == null || svcPkg.getValidityPeriods().isEmpty()) {
			throw new CreationException("A service package should comprise at least one validity period.");
		}
		
		// retrieve services
		Set<Service> services = new HashSet<Service> ();
		for (int id: sids) {
			Service srv = em.find(Service.class, id);
			services.add(srv);
		}
		if(services.isEmpty()) {
			throw new CreationException("A service package should comprise at least one service.");
		}
		svcPkg.setAvailableServices(services);
		
		// retrieve prods
		if(pids != null && pids.length > 0) {
			Set<OptProduct> prods = new HashSet<OptProduct> ();
			for(int id: pids) {
				OptProduct prod = em.find(OptProduct.class, id);
				prods.add(prod);
			}
			svcPkg.setAvailableOptProds(prods);
		}
		
		
		try {
			em.persist(svcPkg);
			em.flush();
		} catch (Exception e) {
			throw new CreationException(e.getMessage());
		}
	}
}
