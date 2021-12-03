package entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class ServicePackage {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	
	@OneToOne
	@JoinColumn(name ="validityPeriodID")
	private ValidityPeriod validityPeriod;
	
	@ManyToMany
	@JoinTable(
			name = "AvailableServices",
			joinColumns= @JoinColumn(name ="servicePackageID"),
			inverseJoinColumns = @JoinColumn(name ="serviceID")
	)	
	private Set<Service> includedServices;
}
