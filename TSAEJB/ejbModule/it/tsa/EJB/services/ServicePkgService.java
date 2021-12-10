package it.tsa.EJB.services;

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
	
	
	public void createAServicePkg(String name, Set<ValidityPeriod> vps, Set<Service> services, Set<OptProduct> prods) throws CreationException{
		ServicePackage svcPkg = new ServicePackage();
		svcPkg.setName(name);
		svcPkg.setValidityPeriods(vps);
		svcPkg.setAvailableServices(services);
		svcPkg.setAvailableOptProds(prods);
		
		try {
			em.persist(svcPkg);
			em.flush();
		} catch (Exception e) {
			throw new CreationException(e.getMessage());
		}
	}
}
