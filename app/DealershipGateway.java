package app;

import java.rmi.RemoteException;
import java.util.List;

import app.Car.Category;
import auth.Admin;
import auth.User;

public class DealershipGateway implements DealershipApi {
  @Override
  public User login(String username, String password) throws RemoteException {
    try {
      NodeApi node = ((NodeApi) Gateway.register.lookup(Gateway.getNextNode().id()));
      return node.login(username, password);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage());
    }
  }

  @Override
  public void addCar(User user, Car car) throws RemoteException {
    if (!(user instanceof Admin)) {
      throw new RemoteException("Apenas administradores podem adicionar carros");
    }

    try {
      NodeApi node = ((NodeApi) Gateway.register.lookup(Gateway.getLeader().id()));
      node.addCar(car);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage());
    }
  }

  @Override
  public void removeCar(User user, String renavam) throws RemoteException {
    if (!(user instanceof Admin)) {
      throw new RemoteException("Apenas administradores podem remover carros");
    }

    try {
      NodeApi node = ((NodeApi) Gateway.register.lookup(Gateway.getLeader().id()));
      node.removeCar(renavam);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage());
    }
  }

  @Override
  public Car buyCar(User user, String renavam) throws RemoteException {
    try {
      NodeApi node = ((NodeApi) Gateway.register.lookup(Gateway.getLeader().id()));
      return node.buyCar(renavam);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage());
    }
  }

  @Override
  public void updateCar(User user, String renavam, Car car) throws RemoteException {
    if (!(user instanceof Admin)) {
      throw new RemoteException("Apenas administradores podem atualizar carros");
    }

    try {
      NodeApi node = ((NodeApi) Gateway.register.lookup(Gateway.getLeader().id()));
      node.updateCar(renavam, car);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage());
    }
  }

  @Override
  public List<Car> searchCars(User user, String search) throws RemoteException {
    try {
      NodeApi node = ((NodeApi) Gateway.register.lookup(Gateway.getNextNode().id()));
      return node.searchCars(search);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage());
    }
  }

  @Override
  public Car getCarByRenavam(User user, String renavam) throws RemoteException {
    try {
      NodeApi node = ((NodeApi) Gateway.register.lookup(Gateway.getNextNode().id()));
      return node.getCarByRenavam(renavam);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage());
    }
  }

  @Override
  public List<Car> listCars(User user) throws RemoteException {
    try {
      NodeApi node = ((NodeApi) Gateway.register.lookup(Gateway.getNextNode().id()));
      return node.listCars();
    } catch (Exception e) {
      throw new RemoteException(e.getMessage());
    }
  }

  @Override
  public List<Car> listCarsByCategory(User user, Category category) throws RemoteException {
    try {
      NodeApi node = ((NodeApi) Gateway.register.lookup(Gateway.getNextNode().id()));
      return node.listCarsByCategory(category);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage());
    }
  }

  @Override
  public int getTotalQuantity(User user) throws RemoteException {
    try {
      NodeApi node = ((NodeApi) Gateway.register.lookup(Gateway.getNextNode().id()));
      return node.getTotalQuantity();
    } catch (Exception e) {
      throw new RemoteException(e.getMessage());
    }
  }
}
