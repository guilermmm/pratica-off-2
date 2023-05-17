package app;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import dbg.Dbg;
import dbg.Dbg.Color;

class Node {
  private String id;
  private boolean alive;

  public Node(String id) {
    this.id = id;
    this.alive = true;
  }

  public String id() {
    return id;
  }

  public boolean isAlive() {
    return alive;
  }

  public void setAlive(boolean alive) {
    this.alive = alive;
  }

}

public class Gateway {
  static Node leader = null;
  static Queue<Node> nodes = new ArrayDeque<>();
  static Registry register = null;

  public static void main(String args[]) {
    try {
      LocateRegistry.createRegistry(Registry.REGISTRY_PORT);

      register = LocateRegistry.getRegistry("localhost");

      String leaderId = "n1";
      List<String> followersIds = List.of("n2", "n3");

      Thread leaderThread = new Thread(new Server(leaderId, new LeaderNode(followersIds)));
      leader = new Node(leaderId);
      leaderThread.start();
      nodes.add(leader);

      boolean done = false;
      while (!done) {
        try {
          register.lookup(leader.id());
          done = true;
        } catch (Exception e) {
          Thread.sleep(0);
        }
      }

      for (String id : followersIds) {
        Thread thread = new Thread(new Server(id, new FollowerNode(leaderId)));
        thread.start();
        nodes.add(new Node(id));
      }

      DealershipGateway dealership = new DealershipGateway();
      DealershipApi skeleton = (DealershipApi) UnicastRemoteObject.exportObject(dealership, 0);
      register.bind("dealership", skeleton);

      try (Dbg dbg = new Dbg()) {
        while (true) {
          for (Node node : nodes) {
            if (node.isAlive()) {
              Dbg.log(Color.GREEN, "Node " + node.id() + " is ALIVE");
            } else {
              Dbg.log(Color.RED, "Node " + node.id() + " is DEAD");
            }
          }

          String id = dbg.input(Color.YELLOW);

          Node node = findNode(id);
          if (node == null) {
            Dbg.log(Color.RED, "Node " + id + " not found");
            continue;
          }

          if (node.isAlive()) {
            killNode(id);
            Dbg.log(Color.BLUE, "Node " + id + " killed");
          } else {
            reviveNode(id);
            Dbg.log(Color.BLUE, "Node " + id + " revived");
          }
        }
      }
    } catch (Exception e) {
      Dbg.log(Color.RED, "Server exception: " + e.toString());
      e.printStackTrace();
    }
  }

  public static Node getNextNode() {
    Node node = nodes.poll();
    nodes.add(node);
    if (!node.isAlive()) {
      updateAllRoles();
      return getNextNode();
    }
    return node;
  }

  public static Node getLeader() {
    Node leader = Gateway.leader;
    if (!leader.isAlive()) {
      Node nextLeader = getNextNode();
      Gateway.leader = nextLeader;
      updateAllRoles();
      return nextLeader;
    }
    return leader;
  }

  private static Node findNode(String id) {
    return nodes.stream().filter(n -> n.id().equals(id)).findFirst().orElse(null);
  }

  private static void killNode(String nodeId) {
    Node node = findNode(nodeId);
    if (node == null) {
      throw new RuntimeException("Nó " + nodeId + " não encontrado");
    }
    node.setAlive(false);
  }

  private static void reviveNode(String nodeId) {
    Node node = findNode(nodeId);
    if (node == null) {
      throw new RuntimeException("Nó " + nodeId + " não encontrado");
    }
    node.setAlive(true);
    updateAllRoles();
  }

  private static void updateAllRoles() {
    List<String> followersIds = nodes.stream()
        .filter(n -> n.isAlive() && !n.id().equals(leader.id()))
        .map(Node::id)
        .toList();

    for (Node node : nodes) {
      if (!node.isAlive()) {
        continue;
      }

      try {
        NodeRole role = null;
        if (findNode(node.id()).isAlive()) {
          if (node.id().equals(leader.id())) {
            role = new LeaderNode(followersIds);
          } else {
            role = new FollowerNode(leader.id());
          }
        }

        NodeApi nodeApi = (NodeApi) register.lookup(node.id());
        nodeApi.setRole(role);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }
}
