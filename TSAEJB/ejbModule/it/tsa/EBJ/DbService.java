package it.tsa.EBJ;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.polimi.db2.album.entities.Album;
import it.tsa.EJB.entities.ServicePackage;
import it.tsa.EJB.entities.dummyEntities.*;
import it.tsa.EJB.exceptions.LoginErrorException;

@Stateless
public class DbService {

	@PersistenceContext(unitName = "TSAEJB")
	private EntityManager em;

	public DbService() {
	}

	public User checkCredentials(String usrn, String pwd) throws LoginErrorException {
		List<User> uList = null;
		try {
			uList = em.createNamedQuery("User.checkCredentials", User.class).setParameter(1, usrn).setParameter(2, pwd)
					.getResultList();
		} catch (PersistenceException e) {
			throw new LoginErrorException();
		}
		if (uList.isEmpty())
			throw new LoginErrorException();
		else if (uList.size() == 1)
			return uList.get(0);
		else
			throw new LoginErrorException();
	}

	public boolean createUser(String username, String pwd, String email) {

		List<User> uList = null;
		try {
			//TODO define query in User class
			uList = em.createNamedQuery("User.checkDuplicateUsername", User.class).setParameter(1, username)
					.getResultList();

		} catch (PersistenceException e) {
			return false;
		}
		
		if (uList.isEmpty()) {
			User newUser = new User();
			newUser.setUsername(username);
			newUser.setPassword(pwd);
			newUser.setEmail(email);

			em.persist(newUser);
			em.flush();
			return true;
		} else
			return false;

	}
	
	public List<ServicePackage> getAllServicePackages() {

		return em.createNamedQuery("ServicePackage.findAll", ServicePackage.class).getResultList();
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
