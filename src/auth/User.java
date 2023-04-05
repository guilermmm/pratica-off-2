package auth;

public class User {
  protected static Action[] allowedActions;

  public static boolean can(Action action) {
    for (var a : allowedActions) {
      if (a == action) {
        return true;
      }
    }
    return false;
  }
}
