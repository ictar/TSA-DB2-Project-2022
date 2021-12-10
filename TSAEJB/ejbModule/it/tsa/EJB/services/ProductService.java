package it.tsa.EJB.services;

import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;

import it.tsa.EJB.entities.OptProduct;
import it.tsa.EJB.exceptions.CreationException;

@Stateless
public class ProductService {
	@PersistenceContext(unitName = "TSAEJB")
	protected EntityManager em;
	
	public  Set<OptProduct> findAllProducts() {
		Query qry = em.createQuery("SELECT p FROM OptProduct p");
		return Set.copyOf(qry.getResultList());		
	}
	
	public void createAProduct(String name, float fee) throws CreationException{
		OptProduct product = new OptProduct();
		product.setName(name);
		product.setMonthlyFee(fee);
		
		try {
			em.persist(product);
			em.flush();
		} catch (Exception e) {
			throw new CreationException(e.getMessage());
		}
	}
}
