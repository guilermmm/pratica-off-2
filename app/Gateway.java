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

      DealershipGateway dealership = new DealershipGateway();
      DealershipApi skeleton = (DealershipApi) UnicastRemoteObject.exportObject(dealership, 0);
      register.bind("dealership", skeleton);

      Thread leaderThread = new Thread(new Server(leaderId, new LeaderNode(followersIds)));
      leader = new Node(leaderId);
      leaderThread.start();
      nodes.add(leader);

      for (String id : followersIds) {
        Thread thread = new Thread(new Server(id, new FollowerNode(leaderId)));
        thread.start();
        nodes.add(new Node(id));
      }

      Thread.sleep(1000);

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
            setNodeAlive(id, false);
            Dbg.log(Color.BLUE, "Node " + id + " killed");
          } else {
            setNodeAlive(id, true);
            Dbg.log(Color.BLUE, "Node " + id + " revived");
          }
        }
      }
    } catch (Exception e) {
      Dbg.log(Color.RED, "Server exception: " + e.toString());
      e.printStackTrace();
    }
  }

  public static Node findNode(String id) {
    return nodes.stream().filter(n -> n.id().equals(id)).findFirst().orElse(null);
  }

  public static void setNodeAlive(String nodeId, boolean isAlive) {
    Node node = findNode(nodeId);
    if (node == null) {
      throw new RuntimeException("Nó " + nodeId + " não encontrado");
    }
    node.setAlive(isAlive);
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

  public static NodeRole getNodeRole(String nodeId) {
    var followersIds = nodes.stream().filter(n -> n.isAlive() && !n.id().equals(leader.id())).map(Node::id).toList();
    if (findNode(nodeId).isAlive()) {
      if (nodeId.equals(leader.id())) {
        return new LeaderNode(followersIds);
      } else {
        return new FollowerNode(leader.id());
      }
    } else {
      return new DeadNode();
    }
  }

  public static void updateAllRoles() {
    for (Node node : nodes) {
      try {
        NodeApi nodeApi = (NodeApi) register.lookup(node.id());
        nodeApi.setRole(getNodeRole(node.id()));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }
}
