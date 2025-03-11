package project;
import java.util.*;



class Node {
    int key;
    int height;
    Node left, right;

    public Node(int key) {
        this.key = key;
        this.height = 1;
        this.left = null;
        this.right = null;
    }
}

class AVLTree {
    private Node root;

    public void insert(int key) {
        root = insertRec(root, key);
    }

    private Node insertRec(Node node, int key) {
        if (node == null) return new Node(key);
        
        if (key < node.key)
            node.left = insertRec(node.left, key);
        else if (key > node.key)
            node.right = insertRec(node.right, key);
        else 
            return node; // Avoid duplicate keys
        
        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }
    
    private Node balance(Node node) {
        int balanceFactor = getBalance(node);
        
        if (balanceFactor > 1) {
            if (getBalance(node.left) < 0)
                node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        
        if (balanceFactor < -1) {
            if (getBalance(node.right) > 0)
                node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        
        return node;
    }
    
    private Node rotateLeft(Node z) {
        Node y = z.right;
        z.right = y.left;
        y.left = z;
        
        z.height = 1 + Math.max(height(z.left), height(z.right));
        y.height = 1 + Math.max(height(y.left), height(y.right));
        
        return y;
    }
    
    private Node rotateRight(Node z) {
        Node y = z.left;
        z.left = y.right;
        y.right = z;
        
        z.height = 1 + Math.max(height(z.left), height(z.right));
        y.height = 1 + Math.max(height(y.left), height(y.right));
        
        return y;
    }
    
    private int height(Node node) {
        return (node == null) ? 0 : node.height;
    }
    
    private int getBalance(Node node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }
}

class Product {
    int productId, quantity;
    String name, category, supplier;
    double price;

    public Product(int productId, String name, String category, double price, int quantity, String supplier) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.supplier = supplier;
    }

    public void updateQuantity(int quantityChange) {
        this.quantity += quantityChange;
    }

    public void updatePrice(double newPrice) {
        this.price = newPrice;
    }

    @Override
    public String toString() {
        return "ID: " + productId + ", Name: " + name + ", Category: " + category + ", Price: " + price + ", Quantity: " + quantity + ", Supplier: " + supplier;
    }
}

class Inventory {
    private List<Product> products;
    private AVLTree avlTree;

    public Inventory() {
        this.products = new ArrayList<>();
        this.avlTree = new AVLTree();
    }

    public void addProduct(Product product) {
        products.add(product);
        avlTree.insert(product.productId);
    }

    public List<Product> getProducts() {  // FIX: Add getter to avoid direct access
        return products;
    }

    public void displayInventory() {
        for (Product product : products) {
            System.out.println(product);
        }
    }
}

class InventoryManager {
    private Inventory inventory;
    private List<Product> lowStockAlerts;

    public InventoryManager() {
        this.inventory = new Inventory();
        this.lowStockAlerts = new ArrayList<>();
    }

    public void addProduct(int productId, String name, String category, int stock, double price, String supplier) {
        Product product = new Product(productId, name, category, price, stock, supplier);
        inventory.addProduct(product);
    }

    public void generateLowStockAlerts(int threshold) {
        lowStockAlerts.clear();
        for (Product product : inventory.getProducts()) {  // FIX: Use getter
            if (product.quantity < threshold) {
                lowStockAlerts.add(product);
            }
        }
    }

    public void displayAlerts() {
        if (lowStockAlerts.isEmpty()) {
            System.out.println("No low stock alerts.");
        } else {
            for (Product alert : lowStockAlerts) {
                System.out.println("Low stock alert for Product ID: " + alert.productId + " - Stock: " + alert.quantity);
            }
        }
    }

    public void displayInventory() {
        inventory.displayInventory();
    }

    public List<Product> getInventory() {
        return inventory.getProducts();
    }
}  


public class Inventory_system {
	public static void main(String[] args) {
        InventoryManager manager = new InventoryManager();

        // Manually adding products
        manager.addProduct(101, " Wireless Mouse", "Electronics", 150, 25.99, "LogiTech Ltd");
        manager.addProduct(102, " Laptop Charger", "Electronics",80, 45.5, "PowerTech Inc");
        manager.addProduct(103, "Smartphone", "Electronics", 50, 699, " MobileHub");
        manager.addProduct(104, "USB-C Cable", "Electronics", 250, 34.99, "ConnectX");
        manager.addProduct(105, "Wireless Keyboard", "Electronics", 10, 199.99, "KeyTech");
        manager.addProduct(106, "Headphones", "Electronics", 10, 199.99, "SoundWave");
        manager.addProduct(107, "Printer Ink", "Electronics", 10, 199.99, "PrintTech");
        
        // Step 1: Display Initial Inventory
        System.out.println("\n=== INITIAL INVENTORY ===");
        manager.displayInventory();

        // Step 2: Generate Low Stock Alerts
        manager.generateLowStockAlerts(20);
        System.out.println("\n=== LOW STOCK ALERTS ===");
        manager.displayAlerts();

        // Step 3: Test Stock & Price Updates
        System.out.println("\n=== UPDATING STOCK AND PRICE ===");
        Product testProduct = manager.getInventory().get(0);
        System.out.println("Updating Product ID: " + testProduct.productId);

        testProduct.updateQuantity(30);
        System.out.println("New Stock for " + testProduct.productId + ": " + testProduct.quantity);

        testProduct.updateQuantity(-10);
        System.out.println("Updated Stock for " + testProduct.productId + ": " + testProduct.quantity);

        testProduct.updatePrice(testProduct.price + 10);
        System.out.println("Updated Price for " + testProduct.productId + ": " + testProduct.price);

        // Step 4: Display Updated Inventory
        System.out.println("\n=== UPDATED INVENTORY ===");
        manager.displayInventory();

        // Step 5: Regenerate Alerts
        manager.generateLowStockAlerts(20);
        System.out.println("\n=== UPDATED LOW STOCK ALERTS ===");
        manager.displayAlerts();
    }

}
