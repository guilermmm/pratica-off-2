package app;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import app.Car.Category;
import dbg.Dbg;
import dbg.Dbg.Color;

public class Server implements Runnable {
  private String nodeId;
  private NodeRole role;

  public Server(String nodeId, NodeRole role) {
    this.nodeId = nodeId;
    this.role = role;
  }

  @Override
  public void run() {
    try {
      Dbg.log(Color.BLUE, "Iniciando nó " + nodeId + " como " + role);

      Registry register = LocateRegistry.getRegistry("localhost");
      Database db = new Database();

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

      DealershipNode node = new DealershipNode(nodeId, role, db);
      NodeApi skeleton = (NodeApi) UnicastRemoteObject.exportObject(node, 0);
      register.bind(nodeId, skeleton);

      Dbg.log(Color.BLUE, "Server " + nodeId + " ready");
    } catch (Exception e) {
      Dbg.log(Color.RED, "Server " + nodeId + " exception: " + e.toString());
      e.printStackTrace();
    }
  }

  static List<Car> readFromDisk() {
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
