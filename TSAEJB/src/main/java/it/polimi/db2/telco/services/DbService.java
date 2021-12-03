package services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entities.Artist;
import entities.Song;

@Stateless
public class DbService {

	@PersistenceContext(unitName = "TelephoneServicesEJB")
	private EntityManager em;

	public DbService() {
	}

	public int insertArtist(String name) {
		Artist newArtist = new Artist();
		newArtist.setName(name);
		em.persist(newArtist);
		em.flush();
		return newArtist.getID();
	}
	
	public boolean matchArtistAndSong(int songId, int artistId) {
		return false;
	}

	public boolean insertSong(String name, String artistName) {
		Song newSong = new Song();
		newSong.setName(name);

		List<Artist> tags = em.createNamedQuery("Artist.getIDFromName", Artist.class).setParameter("name", artistName)
				.getResultList();
		System.out.println("Result " +tags.get(0));
		if (tags.size() > 0) {
			newSong.setArtist(tags.get(0));
			em.persist(newSong);
			em.flush();
			return true;
		} else
			return false;
	}

	public boolean updateSong(int id) {
		//instead of persist call merge (?)
		Song a = em.find(Song.class, id);
		System.out.println("Song: " + a);
		if (a == null)
			return false;
		a.setName("CAO");
		em.merge(a);
		return true;
	}
	
	public boolean deleteArtist(int id) {
		Artist a = em.find(Artist.class, id);
		if (a == null)
			return false;
		em.remove(a);
		return true;
	}

}
