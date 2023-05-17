package app;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import dbg.Dbg;
import dbg.Dbg.Color;

public class Server implements Runnable {
  private String nodeId;
  private NodeRole role;
  private Database db;

  public Server(String nodeId, NodeRole role) {
    this.nodeId = nodeId;
    this.role = role;
  }

  @Override
  public void run() {
    try {
      Dbg.log(Color.BLUE, "Iniciando nó " + nodeId + " como " + role);

      Registry register = LocateRegistry.getRegistry("localhost");
      DealershipNode node = new DealershipNode(nodeId, role, db);
      node.getData();
      NodeApi skeleton = (NodeApi) UnicastRemoteObject.exportObject(node, 0);
      register.bind(nodeId, skeleton);

      Dbg.log(Color.BLUE, "Server " + nodeId + " ready");
    } catch (Exception e) {
      Dbg.log(Color.RED, "Server " + nodeId + " exception: " + e.toString());
      e.printStackTrace();
    }
  }

}
