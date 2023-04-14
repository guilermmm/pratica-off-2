package auth;

public class Costumer extends User {
  static {
    allowedActions = new Action[] {
        Action.LIST,
        Action.SEARCH,
        Action.QUANTITY,
        Action.BUY,
    };
  }

  public Costumer(String username) {
    super(username);
  }
}