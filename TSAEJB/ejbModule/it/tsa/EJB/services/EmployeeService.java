package it.tsa.EJB.services;

import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import it.tsa.EJB.entities.Employee;
import it.tsa.EJB.exceptions.CredentialsException;

@Stateless
public class EmployeeService {
	protected EntityManager em;
	
	public EmployeeService(EntityManager em) {
		this.em = em;
	}
	
	
	
	public Employee checkCredentials(String username, String password) throws   CredentialsException, NonUniqueResultException {
		List<Employee> eList;
		
		try {
			eList = em.createNamedQuery("Employee.checkCredenetials", Employee.class)
					.setParameter("username", username)
					.setParameter("password", password)
					.getResultList();
		}catch(PersistenceException e) {
			throw new  CredentialsException("Could not verify credentials of employee " + username);
		}
		
		if (eList.isEmpty()) {
			return null;
		} else if (eList.size() == 1) {
			return eList.get(0);
		}
		
		throw new NonUniqueResultException("More than one employee registered with same credentials.");
	}
}
