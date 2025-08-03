package in.sp.main.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.sp.main.entites.Subscription;

@Repository
public interface SubscriptionRepo extends JpaRepository<Subscription,Long> {
	 List<Subscription> findByUserId(Long userId);
	
}
