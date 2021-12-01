package entities;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ServiceActivation {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private Date dateOfAct;
	private Date dateOfDeact;
	
}
