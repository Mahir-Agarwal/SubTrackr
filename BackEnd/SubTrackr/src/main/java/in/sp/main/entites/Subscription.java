package in.sp.main.entites;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "subscription")
public class Subscription {
		
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;               // Service Name
    private String category;           // Category
    private Double price;              // Amount
    private LocalDate nextPaymentDate; // Next Due Date
    private boolean autoPay;           // Enable Timely Reminders
    private Long userId;               // Optional - current user

    // Constructors, getters, setters
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public LocalDate getNextPaymentDate() {
		return nextPaymentDate;
	}
	public void setNextPaymentDate(LocalDate nextPaymentDate) {
		this.nextPaymentDate = nextPaymentDate;
	}
	
	public boolean isAutoPay() {
		return autoPay;
	}
	
	public void setAutoPay(boolean autoPay) {
		this.autoPay = autoPay;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getId() {
	    return id;
	}

	public void setId(Long id) {
	    this.id = id;
	}
}
