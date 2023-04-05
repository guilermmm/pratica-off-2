package Connection;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import dbg.Dbg;
import dbg.Dbg.Color;

public class Server implements Hello {

  public Server() {
  }

  // implementação do método oi()
  public String oi() {
    return "Oi, RMI!";
  }

  public static void main(String args[]) {

    try {
      Server remoteObject = new Server();

      Hello skeleton = (Hello) UnicastRemoteObject.exportObject(remoteObject, 0);

      LocateRegistry.createRegistry(Registry.REGISTRY_PORT);

      Registry register = LocateRegistry.getRegistry();

      register.bind("Hello", skeleton);

      Dbg.log(Color.BLUE, "Server ready");
    } catch (Exception e) {
      Dbg.log(Color.RED, "Server exception: " + e.toString());
      e.printStackTrace();
    }
  }
}