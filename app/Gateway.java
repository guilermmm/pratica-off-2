package app;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import app.Car.Category;
import auth.User;
import dbg.Dbg;
import dbg.Dbg.Color;

public class Gateway {
  static Thread leader = null;
  static List<Thread> followers = new ArrayList<>();

  public static void main(String args[]) {
    try {

      leader = new Thread(() -> Server.main(new String[] { "leader" }));
      leader.start();

      for (int i = 0; i < 2; i++) {
        Thread thread = new Thread(() -> Server.main(new String[] { "follower" }));
        thread.start();
        followers.add(thread);
      }

      DealershipGateway gateway = new DealershipGateway();

      DealershipApi skeleton = (DealershipApi) UnicastRemoteObject.exportObject(gateway, 0);
    } catch (Exception e) {
      Dbg.log(Color.RED, "Server exception: " + e.toString());
      e.printStackTrace();
    }

  }

}
