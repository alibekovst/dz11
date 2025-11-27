package shop;

import java.util.*;

// ===============================
// ABSTRACT USER
// ===============================
abstract class User {
    protected int id;
    protected String name;
    protected String email;
    protected String address;
    protected String phone;
    protected String role;

    public User(int id, String name, String email, String address, String phone, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.role = role;
    }

    public void register() {
        System.out.println(name + " registered.");
    }

    public void login() {
        System.out.println(name + " logged in.");
    }

    public void updateData() {
        System.out.println("User data updated.");
    }
}

// ===============================
// CLIENT
// ===============================
class Client extends User {
    private int loyaltyPoints = 0;

    public Client(int id, String name, String email, String address, String phone) {
        super(id, name, email, address, phone, "CLIENT");
    }

    public void addLoyaltyPoints(int points) { loyaltyPoints += points; }

    public int getLoyaltyPoints() { return loyaltyPoints; }
}

// ===============================
// ADMIN
// ===============================
class Admin extends User {
    public Admin(int id, String name, String email, String address, String phone) {
        super(id, name, email, address, phone, "ADMIN");
    }

    public void logAction(String action) {
        System.out.println("Admin log: " + action);
    }
}

// ===============================
// CATEGORY
// ===============================
class Category {
    private int id;
    private String name;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

// ===============================
// PRODUCT
// ===============================
class Product {
    private int id;
    private String title;
    private String description;
    private double price;
    private int stock;
    private Category category;

    public Product(int id, String title, String description,
                   double price, int stock, Category category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    public void create() { System.out.println("Product created: " + title); }
    public void update() { System.out.println("Product updated."); }
    public void delete() { System.out.println("Product deleted."); }

    public double getPrice() { return price; }
    public String getTitle() { return title; }
}

// ===============================
// PROMO CODE
// ===============================
class PromoCode {
    private String code;
    private double discountPercent;

    public PromoCode(String code, double discountPercent) {
        this.code = code;
        this.discountPercent = discountPercent;
    }

    public boolean isValid() { return true; }

    public double applyDiscount(double amount) {
        return amount - (amount * discountPercent / 100);
    }
}

// ===============================
// PAYMENT
// ===============================
class Payment {
    private int id;
    private String type;
    private double amount;
    private String status;
    private Date date;

    public Payment(int id, String type, double amount) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.status = "Pending";
        this.date = new Date();
    }

    public void process() {
        status = "Paid";
        System.out.println("Payment processed: " + amount);
    }

    public void refund() {
        status = "Refunded";
        System.out.println("Payment refunded.");
    }
}

// ===============================
// COURIER
// ===============================
class Courier {
    private int id;
    private String name;
    private String phone;

    public Courier(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }
}

// ===============================
// DELIVERY
// ===============================
class Delivery {
    private int id;
    private String address;
    private String status;
    private Courier courier;

    public Delivery(int id, String address, Courier courier) {
        this.id = id;
        this.address = address;
        this.courier = courier;
        this.status = "Processing";
    }

    public void send() {
        status = "Sent";
        System.out.println("Delivery sent.");
    }

    public void track() {
        System.out.println("Delivery status: " + status);
    }

    public void complete() {
        status = "Completed";
        System.out.println("Delivery completed.");
    }
}

// ===============================
// REVIEW
// ===============================
class Review {
    private int id;
    private int rating;
    private String comment;
    private Client client;
    private Product product;

    public Review(int id, int rating, String comment, Client client, Product product) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.client = client;
        this.product = product;
    }

    public void leaveReview() {
        System.out.println("Review left for product: " + product.getTitle());
    }
}

// ===============================
// ORDER
// ===============================
class Order {
    private int id;
    private Date createdAt;
    private String status;
    private Client client;
    private List<Product> products = new ArrayList<>();
    private double totalSum;
    private Payment payment;
    private Delivery delivery;

    public Order(int id, Client client) {
        this.id = id;
        this.client = client;
        this.status = "Created";
        this.createdAt = new Date();
    }

    public void addProduct(Product p) {
        products.add(p);
        totalSum += p.getPrice();
    }

    public void applyPromoCode(PromoCode promo) {
        if (promo.isValid()) {
            totalSum = promo.applyDiscount(totalSum);
        }
    }

    public void place() {
        status = "Placed";
        System.out.println("Order placed. Total: " + totalSum);
    }

    public void cancel() {
        status = "Cancelled";
        System.out.println("Order cancelled.");
    }

    public void pay(Payment payment) {
        this.payment = payment;
        payment.process();
        status = "Paid";
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }
}

// ===============================
// FACTORY PATTERN
// ===============================
interface ProductFactory {
    Product createProduct(int id, String title, double price);
}

class ElectronicProductFactory implements ProductFactory {
    public Product createProduct(int id, String title, double price) {
        return new Product(id, title, "Electronic item", price, 10, new Category(1, "Electronics"));
    }
}

class ClothingProductFactory implements ProductFactory {
    public Product createProduct(int id, String title, double price) {
        return new Product(id, title, "Clothing item", price, 20, new Category(2, "Clothing"));
    }
}

// ===============================
// PUBLIC MAIN CLASS
// ===============================
public class Main {
    public static void main(String[] args) {

        Client client = new Client(1, "Alibek", "ali@mail.com", "Almaty", "7777");
        client.register();

        ProductFactory factory = new ElectronicProductFactory();
        Product laptop = factory.createProduct(101, "MacBook", 500000);

        Order order = new Order(1, client);
        order.addProduct(laptop);

        PromoCode promo = new PromoCode("SALE10", 10);
        order.applyPromoCode(promo);

        order.place();

        Payment payment = new Payment(1, "CARD", 450000);
        order.pay(payment);

        Courier courier = new Courier(11, "Daniyar", "8777");
        Delivery delivery = new Delivery(11, "Almaty, Dostyk 10", courier);
        order.setDelivery(delivery);

        delivery.send();
        delivery.track();
        delivery.complete();
    }
}
