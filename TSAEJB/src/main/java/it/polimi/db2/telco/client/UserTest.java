package it.polimi.db2.telco.client;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import it.polimi.db2.telco.model.User;
import it.polimi.db2.telco.model.UserService;

public class UserTest {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("TelcoService");
		EntityManager em = emf.createEntityManager();
		UserService service = new UserService(em);
		
		// create and persist an user
		em.getTransaction().begin();
		User ur = service.createUser("ele1", "123@456.mail.com", "7890123");
		em.getTransaction().commit();
		System.out.println("Persisted" + ur);
		
		// find a specific user
		ur = service.findUser(ur.getId());
		System.out.println("Found " + ur);
		
		// find all users
		Collection<User> urs = service.findAllUsers();
		for (User u : urs)
			System.out.println("Found User: " + u);
		
		// close the EM and EMF when done
		em.close();
		emf.close();

	}

}
