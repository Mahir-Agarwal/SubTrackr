    package com.example.subtracker.entites;

    public class Subscription {
        private Long id;
        private String name;
        private String category;
        private Double price;  // Changed from int to Double
        private String nextPaymentDate;  // Keep as String for JSON serialization
        private boolean autoPay;
        private Long userId;

        public Subscription() {}

        public Subscription(String category, String name, Double price, String nextPaymentDate, boolean autoPay) {
            this.category = category;
            this.name = name;
            this.price = price;
            this.nextPaymentDate = nextPaymentDate;
            this.autoPay = autoPay;
        }

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }

        public String getNextPaymentDate() { return nextPaymentDate; }
        public void setNextPaymentDate(String nextPaymentDate) { this.nextPaymentDate = nextPaymentDate; }

        public boolean isAutoPay() { return autoPay; }
        public void setAutoPay(boolean autoPay) { this.autoPay = autoPay; }

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
    }

