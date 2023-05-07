package app;

import java.io.Serializable;

public class Car implements Serializable {
  private static final long serialVersionUID = 1L;

  public static enum Category {
    ECONOMIC, INTERMEDIARY, EXECUTIVE;

    public static Category fromString(String category) {
      switch (category) {
        case "ECONOMIC":
          return ECONOMIC;
        case "INTERMEDIARY":
          return INTERMEDIARY;
        case "EXECUTIVE":
          return EXECUTIVE;
        default:
          return null;
      }
    }
  }

  private String renavam;
  private String name;
  private Category category;
  private int year;
  private int quantity;
  private double price;

  public Car(String renavam, String name, Category category, int year, int quantity, double price) {
    this.renavam = renavam;
    this.name = name;
    this.category = category;
    this.year = year;
    this.quantity = quantity;
    this.price = price;
  }

  public String getRenavam() {
    return renavam;
  }

  public void setRenavam(String renavam) {
    this.renavam = renavam;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public Car clone() {
    return new Car(renavam, name, category, year, quantity, price);
  }
}
