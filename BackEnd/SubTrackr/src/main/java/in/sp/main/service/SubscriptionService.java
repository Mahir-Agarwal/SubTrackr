package in.sp.main.service;

import java.util.List;
import in.sp.main.entites.Subscription;

public interface SubscriptionService {

    Subscription saveSubscription(Subscription subscription);

    List<Subscription> getAllSubscriptions();

    Subscription getSubscriptionById(Long id);

    void deleteSubscription(Long id);
    
    List<Subscription> getSubscriptionsByUserId(Long userId); // optional user-based fetch
}
