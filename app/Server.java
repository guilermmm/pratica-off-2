package app;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import app.Car.Category;
import dbg.Dbg;
import dbg.Dbg.Color;

public class Server {
  public static void main(String args[]) {
    try {
      Database db = Database.getInstance();

      db.addCar(new Car("101", "Fiat Uno", Category.ECONOMIC, 2010, 50, 19999.90));
      db.addCar(new Car("102", "Chevrolet Onix", Category.ECONOMIC, 2015, 40, 21999.90));
      db.addCar(new Car("103", "Ford Ka", Category.ECONOMIC, 2018, 30, 23999.90));
      db.addCar(new Car("104", "Hyundai HB20", Category.ECONOMIC, 2019, 20, 25999.90));
      db.addCar(new Car("105", "Volkswagen Gol", Category.ECONOMIC, 2019, 10, 27999.90));

      db.addCar(new Car("201", "Ford Ka Sedan", Category.INTERMEDIARY, 2010, 50, 39999.90));
      db.addCar(new Car("202", "Chevrolet Prisma", Category.INTERMEDIARY, 2015, 40, 41999.90));
      db.addCar(new Car("203", "Volkswagen Voyage", Category.INTERMEDIARY, 2018, 30, 43999.90));
      db.addCar(new Car("204", "Hyundai HB20S", Category.INTERMEDIARY, 2019, 20, 45999.90));
      db.addCar(new Car("205", "Fiat Argo", Category.INTERMEDIARY, 2019, 10, 47999.90));

      db.addCar(new Car("301", "Ford Fusion", Category.EXECUTIVE, 2010, 50, 79999.90));
      db.addCar(new Car("302", "Chevrolet Cruze", Category.EXECUTIVE, 2015, 40, 81999.90));
      db.addCar(new Car("303", "Volkswagen Jetta", Category.EXECUTIVE, 2018, 30, 83999.90));
      db.addCar(new Car("304", "Hyundai Elantra", Category.EXECUTIVE, 2019, 20, 85999.90));
      db.addCar(new Car("305", "Fiat Linea", Category.EXECUTIVE, 2019, 10, 87999.90));

      DealershipApi skeleton = (DealershipApi) UnicastRemoteObject.exportObject(new Dealership(), 0);

      LocateRegistry.createRegistry(Registry.REGISTRY_PORT);

      Registry register = LocateRegistry.getRegistry();

      register.bind("dealership", skeleton);

      Dbg.log(Color.BLUE, "Server ready");
    } catch (Exception e) {
      Dbg.log(Color.RED, "Server exception: " + e.toString());
      e.printStackTrace();
    }
  }
}