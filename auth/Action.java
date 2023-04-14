package auth;

public enum Action {
  CREATE("Adicionar carro"),
  DELETE("Remover carro"),
  LIST("Listar carros"),
  SEARCH("Pesquisar carros"),
  UPDATE("Alterar carro"),
  QUANTITY("Exibir quantidade de carros"),
  BUY("Comprar carro");

  private String title;

  private Action(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }
}