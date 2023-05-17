package app;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;

import app.Car.Category;
import auth.*;
import dbg.Dbg;
import dbg.Dbg.Color;
import exception.DbException;

public class DealershipNode implements NodeApi {
  private String id;
  private NodeRole role;
  private Database db;

  public DealershipNode(String id, NodeRole role, Database db) {
    this.id = id;
    this.role = role;
    this.db = db;
  }

  @Override
  public User login(String username, String password) throws RemoteException {
    Dbg.log(Color.BLUE, id + " - login");

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
  public void addCar(Car car) throws RemoteException {
    Dbg.log(Color.BLUE, id + " - addCar");
    try {
      db.addCar(car);
      if (role instanceof LeaderNode node) {
        for (String followerId : node.followers()) {
          NodeApi follower = ((NodeApi) Gateway.register.lookup(followerId));
          follower.addCar(car);
        }
      }
    } catch (DbException e) {
      throw new RemoteException(e.getMessage());
    } catch (Exception e) {
      throw new RemoteException(e.getMessage());
    }
  }

  @Override
  public void removeCar(String renavam) throws RemoteException {
    Dbg.log(Color.BLUE, id + " - removeCar");
    try {
      db.removeCar(renavam);
      if (role instanceof LeaderNode node) {
        for (String followerId : node.followers()) {
          NodeApi follower = ((NodeApi) Gateway.register.lookup(followerId));
          follower.removeCar(renavam);
        }
      }
    } catch (DbException e) {
      throw new RemoteException(e.getMessage());
    } catch (Exception e) {
      throw new RemoteException(e.getMessage());
    }
  }

  @Override
  public Car buyCar(String renavam) throws RemoteException {
    Dbg.log(Color.BLUE, id + " - buyCar");

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
      if (role instanceof LeaderNode node) {
        for (String followerId : node.followers()) {
          NodeApi follower = ((NodeApi) Gateway.register.lookup(followerId));
          follower.updateCar(renavam, car);
        }
      }
    } catch (DbException e) {
      throw new RemoteException(e.getMessage());
    } catch (Exception e) {
      throw new RemoteException(e.getMessage());
    }

    car.setQuantity(1);
    return car;
  }

  @Override
  public void updateCar(String renavam, Car car) throws RemoteException {
    Dbg.log(Color.BLUE, id + " - updateCar");
    try {
      db.updateCar(renavam, car);
      if (role instanceof LeaderNode node) {
        for (String followerId : node.followers()) {
          NodeApi follower = ((NodeApi) Gateway.register.lookup(followerId));
          follower.updateCar(renavam, car);
        }
      }
    } catch (DbException e) {
      throw new RemoteException(e.getMessage());
    } catch (Exception e) {
      throw new RemoteException(e.getMessage());
    }
  }

  @Override
  public List<Car> searchCars(String search) throws RemoteException {
    Dbg.log(Color.BLUE, id + " - searchCars");
    return db.searchCars(search);
  }

  @Override
  public Car getCarByRenavam(String renavam) throws RemoteException {
    Dbg.log(Color.BLUE, id + " - getCarByRenavam");
    return db.getCarByRenavam(renavam).orElse(null);
  }

  @Override
  public List<Car> listCars() throws RemoteException {
    Dbg.log(Color.BLUE, id + " - listCars");
    return db.getCars();
  }

  @Override
  public List<Car> listCarsByCategory(Category category) throws RemoteException {
    Dbg.log(Color.BLUE, id + " - listCarsByCategory");
    return db.getCarsByCategory(category);
  }

  @Override
  public int getTotalQuantity() throws RemoteException {
    Dbg.log(Color.BLUE, id + " - getTotalQuantity");
    return db.getTotalQuantity();
  }

  @Override
  public void setRole(NodeRole role) throws RemoteException {
    Dbg.log(Color.BLUE, id + " - setRole " + role);
    this.role = role;
  }
}
