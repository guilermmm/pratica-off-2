package app;

import java.io.Serializable;
import java.util.List;

public interface NodeRole extends Serializable {
}

record LeaderNode(List<String> followers) implements NodeRole {
  private static final long serialVersionUID = 1L;
}

record FollowerNode(String leader) implements NodeRole {
  private static final long serialVersionUID = 1L;
}
