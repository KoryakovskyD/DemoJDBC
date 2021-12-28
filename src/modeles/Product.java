package modeles;

public class Product {
    private final String article;
    private String name;
    private String color;
    private int price;
    private int balance;

    public Product(String article, String name, String color, int price, int balance) {
        this.article = article;
        this.name = name;
        this.color = color;
        this.price = price;
        this.balance = balance;
    }

    public String getArticle() {
        return article;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getPrice() {
        return price;
    }

    public int getBalance() {
        return balance;
    }
}
