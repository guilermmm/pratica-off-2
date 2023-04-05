package exception;

public class NotAuthorizedException extends Exception {
  public NotAuthorizedException() {
    super("You are not authorized to perform this action.");
  }
}