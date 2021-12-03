package entities;


import javax.persistence.*;
import java.util.Set;

@Entity
public class OrderProduct {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name="userID")
	private User user;
	
	@ManyToMany
	@JoinTable(
			name = "ChosenOptProds",
			joinColumns = @JoinColumn(name = "orderProductID"),
			inverseJoinColumns = @JoinColumn(name ="optProdID")
			)
	private Set<OptionalProduct> chosenOptionalProducts;
	
}
