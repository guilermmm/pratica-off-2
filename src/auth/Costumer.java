package auth;

public class Costumer extends User {
  static {
    allowedActions = new Action[] {
        Action.LIST,
        Action.SEARCH,
        Action.READ,
        Action.BUY,
    };
  }
}