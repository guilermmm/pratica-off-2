package auth;

import java.io.Serializable;

public abstract class User implements Serializable {
  private static final long serialVersionUID = 1L;

  protected static Action[] allowedActions;

  private String username;

  public User(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  public Action[] getAllowedActions() {
    return allowedActions;
  }

  public static boolean can(Action action) {
    for (var a : allowedActions) {
      if (a == action) {
        return true;
      }
    }
    return false;
  }
}
