package app;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import app.Car.Category;
import auth.User;

public interface NodeApi extends Remote {
  User login(String username, String password) throws RemoteException;

  void addCar(Car car) throws RemoteException;

  void removeCar(String renavam) throws RemoteException;

  Car buyCar(String renavam) throws RemoteException;

  void updateCar(String renavam, Car car) throws RemoteException;

  List<Car> searchCars(String search) throws RemoteException;

  Car getCarByRenavam(String renavam) throws RemoteException;

  List<Car> listCars() throws RemoteException;

  List<Car> listCarsByCategory(Category category) throws RemoteException;

  int getTotalQuantity() throws RemoteException;

  void setRole(NodeRole role) throws RemoteException;
}
