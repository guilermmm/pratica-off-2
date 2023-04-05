package app;

import java.util.List;

import app.Car.Category;

public class Dealership implements DealershipApi {

  private Set<Car> cars;

  @Override
  public boolean addCar(Car car) throws NotAuthorizedException, RemoteException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'addCar'");
  }

  @Override
  public boolean removeCar(String name) throws NotAuthorizedException, RemoteException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'removeCar'");
  }

  @Override
  public boolean buyCar(String name) throws NotAuthorizedException, RemoteException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'buyCar'");
  }

  @Override
  public boolean updateCar(String name, Car car) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateCar'");
  }

  @Override
  public List<Car> searchCarsByName(String name) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'searchCarsByName'");
  }

  @Override
  public List<Car> searchCarsByRenavam(String renavam) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'searchCarsByRenavam'");
  }

  @Override
  public List<Car> listCars() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'listCars'");
  }

  @Override
  public List<Car> listCarsByCategory(Category category) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'listCarsByCategory'");
  }

}