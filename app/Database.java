package app;

import java.util.*;
import java.util.stream.Collectors;

import app.Car.Category;
import exception.DbException;

public class Database {
  private static Database instance = null;

  private List<Car> cars = new ArrayList<>();
  private String adminUsername = "admin";
  private String adminPassword = "admin";

  private Database() {
  }

  public static Database getInstance() {
    if (instance == null) {
      instance = new Database();
    }
    return instance;
  }

  public List<Car> getCars() {
    return cars.stream()
        .map(Car::clone)
        .sorted(Comparator.comparing(Car::getName))
        .collect(Collectors.toList());
  }

  public List<Car> searchCars(String search) {
    return cars.stream()
        .filter(car -> car.getName().toLowerCase().contains(search.toLowerCase()) || car.getRenavam().equals(search))
        .map(Car::clone)
        .sorted(Comparator.comparing(Car::getName))
        .collect(Collectors.toList());
  }

  public List<Car> getCarsByCategory(Category category) {
    return cars.stream()
        .filter(car -> car.getCategory().equals(category))
        .map(Car::clone)
        .sorted(Comparator.comparing(Car::getName))
        .collect(Collectors.toList());
  }

  public Optional<Car> getCarByRenavam(String name) {
    return cars.stream()
        .filter(car -> car.getRenavam().equals(name))
        .map(Car::clone)
        .findFirst();
  }

  public void addCar(Car car) throws DbException {
    Optional<Car> carByRenavam = getCarByRenavam(car.getRenavam());
    if (carByRenavam.isPresent()) {
      throw new DbException("Car already exists");
    }

    cars.add(car.clone());
  }

  public void removeCar(String renavam) throws DbException {
    Optional<Car> carByRenavam = getCarByRenavam(renavam);
    if (carByRenavam.isEmpty()) {
      throw new DbException("Car not found");
    }

    cars.removeIf(c -> c.getRenavam().equals(renavam));
  }

  public void updateCar(String renavam, Car car) throws DbException {
    Optional<Car> carByRenavam = getCarByRenavam(renavam);
    if (carByRenavam.isEmpty()) {
      throw new DbException("Car not found");
    }

    cars.removeIf(c -> c.getRenavam().equals(renavam));
    cars.add(car.clone());
  }

  public int getTotalQuantity() {
    return cars.stream()
        .mapToInt(Car::getQuantity)
        .sum();
  }

  public String getAdminUsername() {
    return adminUsername;
  }

  public boolean checkAdminPassword(String password) {
    return adminPassword.equals(password);
  }
}
