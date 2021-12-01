package entities;


import java.time.LocalTime;
import java.util.Date;

import javax.persistence.*;

@Entity
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@OneToOne
	@JoinColumn(name="orderProductID")
	private OrderProduct orderproduct;
	
	@OneToOne
	@JoinColumn(name="serviceActivationID")
	private ServiceActivation serviceActivation;
	
	@ManyToOne
	@JoinColumn(name = "userID")
	private User user;
		
	private Date dateOfCreation;
	private LocalTime hourOfCreation;
	private float totalvalue;
	private Date startDate;
	private boolean validityFlag;
	private boolean rejectedFlag;
	
}