package it.tsa.EJB.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.tsa.EJB.entities.OptProduct;

@Stateless
public class SalesReportService {
	@PersistenceContext(unitName = "TSAEJB")
	protected EntityManager em;

	// return Number of total purchases per package and validity period.
	public List getTotalPurchasePerSPandVP() {
		List rList;
		
		try {
			rList = em.createNativeQuery("select e.spid, e.vpid, e.ordercnt from ServicePkgPurchase e")
					.getResultList();
		}catch (PersistenceException e) {
			return null;
		}
		return rList;
	}
	
	// return Number of total purchases per package
	public List getTotalPurchasePerSP() {
		return em.createNativeQuery("SELECT e.spid, sum(e.ordercnt) FROM ServicePkgPurchase e GROUP BY e.spid")
						.getResultList();
	}
	
	// return Average number of optional products sold together with each service package.
	public List getAvgProdPerSP() {
		return em.createNativeQuery("SELECT e.spid, sum(e.prodcnt)/sum(e.ordercnt) FROM ServicePkgPurchase e GROUP BY e.spid")
						.getResultList();
	}
	
	// Total value of sales per package with the optional products.
	public List getTotalSalesPerSPWithProd() {
		return em.createNativeQuery("SELECT * FROM ServicePkgSaleWithProd e")
						.getResultList();
	}
	
	// Total value of sales per package without the optional products.
	public List getTotalSalesPerSPWithoutProd() {
		return em.createNativeQuery("SELECT * FROM ServicePkgSaleNoProd e")
						.getResultList();
	}
	// Best seller optional product, i.e. the optional product with the greatest value of sales across all
	// the sold service packages.
	public Object getBestSellerProduct() {
		List rList;
		try {
			rList = em.createNativeQuery("SELECT pid, saleCnt From prodSale p ORDER BY p.saleCnt DESC LIMIT 1")
					.getResultList();
			
			if (rList.size() < 1) {
				// no
				return null;
			}
		} catch (PersistenceException e) {
			return null;
		}
		
		return rList.get(0);
	}
}
