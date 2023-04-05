public class Car {
  public static enum Category {
    ECONOMIC, INTERMEDIARY, EXECUTIVE
  }

  private String name;
  private String renavam;
  private Category category;
  private int year;
  private int quantity;
  private double price;

  public Car(String name, String renavam, Category category, int year, int quantity, double price) {
    this.name = name;
    this.renavam = renavam;
    this.category = category;
    this.year = year;
    this.quantity = quantity;
    this.price = price;
  }

  @Override
  public String toString() {
    return "Car [name=" + name + ", renavam=" + renavam + ", category=" + category + ", year=" + year + ", quantity="
        + quantity + ", price=" + price + "]";
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRenavam() {
    return renavam;
  }

  public void setRenavam(String renavam) {
    this.renavam = renavam;
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
}
