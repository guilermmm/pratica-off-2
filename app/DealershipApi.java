package app;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import app.Car.Category;
import auth.User;

public interface DealershipApi extends Remote {
  public User login(String username, String password) throws RemoteException;

  public void addCar(User user, Car car) throws RemoteException;

  public void removeCar(User user, String renavam) throws RemoteException;

  public Car buyCar(User user, String renavam) throws RemoteException;

  public void updateCar(User user, String renavam, Car car) throws RemoteException;

  public List<Car> searchCars(User user, String search) throws RemoteException;

  public Car getCarByRenavam(User user, String renavam) throws RemoteException;

  public List<Car> listCars(User user) throws RemoteException;

  public List<Car> listCarsByCategory(User user, Category category) throws RemoteException;

  public int getTotalQuantity(User user) throws RemoteException;
}
