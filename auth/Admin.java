package auth;

public class Admin extends User {
  static {
    allowedActions = new Action[] {
        Action.CREATE,
        Action.DELETE,
        Action.LIST,
        Action.SEARCH,
        Action.UPDATE,
        Action.QUANTITY,
        Action.BUY,
    };
  }

  public Admin(String username) {
    super(username);
  }
}
