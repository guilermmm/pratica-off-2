package auth;

public class Admin extends User {
  static {
    allowedActions = new Action[] {
        Action.CREATE,
        Action.READ,
        Action.LIST,
        Action.SEARCH,
        Action.UPDATE,
        Action.BUY,
    };
  }
}