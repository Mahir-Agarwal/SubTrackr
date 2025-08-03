package in.sp.main.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.sp.main.entites.Subscription;
import in.sp.main.service.SubscriptionService;

@RestController
@RequestMapping("/api/subscriptions")
@CrossOrigin(origins = "*") // Allow Android app to access
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    // ➕ Add new subscription
    @PostMapping
    public ResponseEntity<Subscription> createSubscription(@RequestBody Subscription subscription) {
        Subscription savedSub = subscriptionService.saveSubscription(subscription);
        return ResponseEntity.ok(savedSub);
    }

    // 📃 Get all subscriptions
    @GetMapping
    public ResponseEntity<List<Subscription>> getAllSubscriptions() {
        return ResponseEntity.ok(subscriptionService.getAllSubscriptions());
    }

    // 🔍 Get subscription by ID
    @GetMapping("/{id}")
    public ResponseEntity<Subscription> getSubscriptionById(@PathVariable Long id) {
        Subscription sub = subscriptionService.getSubscriptionById(id);
        if (sub == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(sub);
    }

    // 🧾 Get subscriptions by User ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Subscription>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionsByUserId(userId));
    }

    // ❌ Delete subscription
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long id) {
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }
}
