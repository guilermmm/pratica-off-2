package app;

import java.rmi.RemoteException;
import java.util.List;

import app.Car.Category;
import auth.User;

public class DealershipGateway implements DealershipApi {

  @Override
  public User login(String username, String password) throws RemoteException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'login'");
  }

  @Override
  public void addCar(User user, Car car) throws RemoteException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'addCar'");
  }

  @Override
  public void removeCar(User user, String renavam) throws RemoteException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'removeCar'");
  }

  @Override
  public Car buyCar(User user, String renavam) throws RemoteException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'buyCar'");
  }

  @Override
  public void updateCar(User user, String renavam, Car car) throws RemoteException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateCar'");
  }

  @Override
  public List<Car> searchCars(User user, String search) throws RemoteException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'searchCars'");
  }

  @Override
  public Car getCarByRenavam(User user, String renavam) throws RemoteException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getCarByRenavam'");
  }

  @Override
  public List<Car> listCars(User user) throws RemoteException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'listCars'");
  }

  @Override
  public List<Car> listCarsByCategory(User user, Category category) throws RemoteException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'listCarsByCategory'");
  }

  @Override
  public int getTotalQuantity(User user) throws RemoteException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getTotalQuantity'");
  }

}
