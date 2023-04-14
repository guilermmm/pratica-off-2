package app;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;

import app.Car.Category;
import auth.*;
import exception.DbException;

public class Dealership implements DealershipApi {
  @Override
  public User login(String username, String password) throws RemoteException {
    Database db = Database.getInstance();

    if (db.getAdminUsername().equals(username)) {
      if (db.checkAdminPassword(password)) {
        return new Admin(username);
      } else {
        throw new RemoteException("Senha incorreta");
      }
    } else {
      return new Costumer(username);
    }
  }

  @Override
  public void addCar(User user, Car car) throws RemoteException {
    if (!(user instanceof Admin)) {
      throw new RemoteException("Apenas administradores podem adicionar carros");
    }

    Database db = Database.getInstance();

    try {
      db.addCar(car);
    } catch (DbException e) {
      throw new RemoteException(e.getMessage());
    }
  }

  @Override
  public void removeCar(User user, String renavam) throws RemoteException {
    if (!(user instanceof Admin)) {
      throw new RemoteException("Apenas administradores podem remover carros");
    }

    Database db = Database.getInstance();

    try {
      db.removeCar(renavam);
    } catch (DbException e) {
      throw new RemoteException(e.getMessage());
    }
  }

  @Override
  public Car buyCar(User user, String renavam) throws RemoteException {
    Database db = Database.getInstance();

    Optional<Car> carByRenavam = db.getCarByRenavam(renavam);
    if (carByRenavam.isEmpty()) {
      throw new RemoteException("Carro não encontrado");
    }

    Car car = carByRenavam.get();

    if (car.getQuantity() == 0) {
      throw new RemoteException("Carro não disponível");
    }

    try {
      car.setQuantity(car.getQuantity() - 1);
      db.updateCar(renavam, car);
    } catch (DbException e) {
      throw new RemoteException(e.getMessage());
    }

    car.setQuantity(1);
    return car;
  }

  @Override
  public void updateCar(User user, String name, Car car) throws RemoteException {
    if (!(user instanceof Admin)) {
      throw new RemoteException("Apenas administradores podem atualizar carros");
    }

    Database db = Database.getInstance();

    try {
      db.updateCar(name, car);
    } catch (DbException e) {
      throw new RemoteException(e.getMessage());
    }
  }

  @Override
  public List<Car> searchCars(User user, String name) throws RemoteException {
    Database db = Database.getInstance();

    return db.searchCars(name);
  }

  @Override
  public Car getCarByRenavam(User user, String renavam) throws RemoteException {
    Database db = Database.getInstance();

    return db.getCarByRenavam(renavam).orElse(null);
  }

  @Override
  public List<Car> listCars(User user) throws RemoteException {
    Database db = Database.getInstance();

    return db.getCars();
  }

  @Override
  public List<Car> listCarsByCategory(User user, Category category) throws RemoteException {
    Database db = Database.getInstance();

    return db.getCarsByCategory(category);
  }

  @Override
  public int getTotalQuantity(User user) throws RemoteException {
    Database db = Database.getInstance();

    return db.getTotalQuantity();
  }

}