package in.sp.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.sp.main.entites.Subscription;
import in.sp.main.repo.SubscriptionRepo;

@Service
public class SubscriptionServiceImp implements SubscriptionService{
	
	
	@Autowired
	SubscriptionRepo repo;

	@Override
	public Subscription saveSubscription(Subscription subscription) {
		
		return repo.save(subscription);
	}

	@Override
	public List<Subscription> getAllSubscriptions() {
		return repo.findAll();
	}

	@Override
	public Subscription getSubscriptionById(Long id) {
		Optional<Subscription> s = repo.findById(id);
		
		return s.orElse(null);
	}

	@Override
	public void deleteSubscription(Long id) {
		
		repo.deleteById(id);
		
	}

	@Override
	public List<Subscription> getSubscriptionsByUserId(Long userId) {
		return repo.findByUserId(userId);
	}
	
	
}
