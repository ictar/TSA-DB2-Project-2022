package it.tsa.EJB.services;


import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.tsa.EJB.entities.Employee;
import it.tsa.EJB.exceptions.CredentialsException;

@Stateless
public class EmployeeService {
	
	@PersistenceContext(unitName = "TSAEJB")
	protected EntityManager em;
	
	public EmployeeService() {

	}
	
	
	public Employee checkCredentials(String username, String password) throws   CredentialsException, NonUniqueResultException {
		List<Employee> eList;
		
		try {
			eList = em.createNamedQuery("Employee.checkCredenetials", Employee.class)
					.setParameter(1, username)
					.setParameter(2, password)
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
