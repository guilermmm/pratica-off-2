package app;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import app.Car.Category;
import auth.User;

public interface DealershipApi extends Remote {
  User login(String username, String password) throws RemoteException;

  void addCar(User user, Car car) throws RemoteException;

  void removeCar(User user, String renavam) throws RemoteException;

  Car buyCar(User user, String renavam) throws RemoteException;

  void updateCar(User user, String renavam, Car car) throws RemoteException;

  List<Car> searchCars(User user, String search) throws RemoteException;

  Car getCarByRenavam(User user, String renavam) throws RemoteException;

  List<Car> listCars(User user) throws RemoteException;

  List<Car> listCarsByCategory(User user, Category category) throws RemoteException;

  int getTotalQuantity(User user) throws RemoteException;
}
