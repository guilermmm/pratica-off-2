package app;

import java.rmi.RemoteException;
import java.util.List;

import exception.NotAuthorizedException;

public interface DealershipApi {
  public boolean addCar(Car car) throws NotAuthorizedException, RemoteException;

  public boolean removeCar(String name) throws NotAuthorizedException, RemoteException;

  public boolean buyCar(String name) throws NotAuthorizedException, RemoteException;

  public boolean updateCar(String name, Car car) throws NotAuthorizedException, RemoteException;

  public List<Car> searchCarsByName(String name) throws RemoteException;

  public List<Car> searchCarsByRenavam(String renavam) throws RemoteException;

  public List<Car> listCars() throws RemoteException;

  public List<Car> listCarsByCategory(Car.Category category) throws RemoteException;
}