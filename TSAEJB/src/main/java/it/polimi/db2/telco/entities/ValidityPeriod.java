package entities;

import javax.persistence.*;

@Entity
public class ValidityPeriod {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int durationMonths;
	private float price;
}
