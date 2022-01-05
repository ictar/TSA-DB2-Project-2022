package it.tsa.EJB.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.tsa.EJB.entities.ActivationSchedule;
import it.tsa.EJB.entities.Auditing;
import it.tsa.EJB.entities.OptProduct;
import it.tsa.EJB.entities.Order;
import it.tsa.EJB.entities.ServicePackage;
import it.tsa.EJB.entities.User;
import it.tsa.EJB.entities.ValidityPeriod;
import it.tsa.EJB.exceptions.LoginErrorException;

@Stateless
public class DbService {

	@PersistenceContext(unitName = "TSAEJB")
	private EntityManager em;

	public ServicePackage retrieveServicePackage(int servicePackageId) {
		return em.createNamedQuery("ServicePackage.findOne", ServicePackage.class).setParameter(1, servicePackageId)
				.getResultList().get(0);
	}

	public List<ServicePackage> findAllServicePackages() {
		return em.createNamedQuery("ServicePackage.findAll", ServicePackage.class).getResultList();
	}

	public void createActivationSchedule(Order order) {
		ActivationSchedule as = new ActivationSchedule();
		Date endDate = order.getStartDate();
		endDate.setMonth(order.getStartDate().getMonth() + order.getValidityPeriod().getMonthDuration());
		as.setDateOfAct(order.getStartDate());
		as.setDateOfDeact(endDate);
		as.setOrder(order);
	}


	public void createAuditing(Order order, User user) {
		Auditing a = new Auditing();
		a.setUser(user);
		a.setAmount(order.getTotalvalue());
		a.setLastRejectionTime(new Timestamp(System.currentTimeMillis()));
		em.persist(a);
		em.flush();
	}
	
	// examples
	/*
	 * public int insertArtist(String name) { Artist newArtist = new Artist();
	 * newArtist.setName(name); em.persist(newArtist); em.flush(); return
	 * newArtist.getID(); }
	 * 
	 * public boolean insertSong(String name, String artistName) { Song newSong =
	 * new Song(); newSong.setName(name);
	 * 
	 * List<Artist> tags = em.createNamedQuery("Artist.getIDFromName",
	 * Artist.class).setParameter("name", artistName) .getResultList();
	 * System.out.println("Result " +tags.get(0)); if (tags.size() > 0) {
	 * newSong.setArtist(tags.get(0)); em.persist(newSong); em.flush(); return true;
	 * } else return false; }
	 * 
	 * public boolean updateSong(int id) { //instead of persist call merge (?) Song
	 * a = em.find(Song.class, id); System.out.println("Song: " + a); if (a == null)
	 * return false; a.setName("CAO"); em.merge(a); return true; }
	 * 
	 * public boolean deleteArtist(int id) { Artist a = em.find(Artist.class, id);
	 * if (a == null) return false; em.remove(a); return true; }
	 */
}
