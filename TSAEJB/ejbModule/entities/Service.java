package entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Service {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToMany(mappedBy="includedServices")
	private Set<ServicePackage> servicePackageRelated;
	
	private String name;
	private int includedMin;
	private int includedSMS;
	private int includedGB;
	private float extraMinFee;
	private float extraSMSFee;
	private float extraGBFee;
	
}
