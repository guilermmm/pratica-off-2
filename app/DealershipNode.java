package app;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
    try {
      getData();
    } catch (Exception e) {
      throw new RemoteException(e.getMessage());
    }
  }

  public void getData() throws RemoteException, NotBoundException, DbException {
    Registry register = LocateRegistry.getRegistry("localhost");
    db = new Database();
    List<Car> cars;
    if (role instanceof FollowerNode node) {
      NodeApi leaderStub = (NodeApi) register.lookup(node.leader());
      cars = leaderStub.listCars();
    } else {
      cars = readFromDisk();
    }
    for (Car car : cars) {
      db.addCar(car);
    }
  }

  private List<Car> readFromDisk() {
    return List.of(
        new Car("101", "Fiat Uno", Category.ECONOMIC, 2010, 50, 19999.90),
        new Car("102", "Chevrolet Onix", Category.ECONOMIC, 2015, 40, 21999.90),
        new Car("103", "Ford Ka", Category.ECONOMIC, 2018, 30, 23999.90),
        new Car("104", "Hyundai HB20", Category.ECONOMIC, 2019, 20, 25999.90),
        new Car("105", "Volkswagen Gol", Category.ECONOMIC, 2019, 10, 27999.90),
        new Car("201", "Ford Ka Sedan", Category.INTERMEDIARY, 2010, 50, 39999.90),
        new Car("202", "Chevrolet Prisma", Category.INTERMEDIARY, 2015, 40, 41999.90),
        new Car("203", "Volkswagen Voyage", Category.INTERMEDIARY, 2018, 30, 43999.90),
        new Car("204", "Hyundai HB20S", Category.INTERMEDIARY, 2019, 20, 45999.90),
        new Car("205", "Fiat Argo", Category.INTERMEDIARY, 2019, 10, 47999.90),
        new Car("301", "Ford Fusion", Category.EXECUTIVE, 2010, 50, 79999.90),
        new Car("302", "Chevrolet Cruze", Category.EXECUTIVE, 2015, 40, 81999.90),
        new Car("303", "Volkswagen Jetta", Category.EXECUTIVE, 2018, 30, 83999.90),
        new Car("304", "Hyundai Elantra", Category.EXECUTIVE, 2019, 20, 85999.90),
        new Car("305", "Fiat Linea", Category.EXECUTIVE, 2019, 10, 87999.90));
  }
}
