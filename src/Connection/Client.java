package Connection;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import dbg.Dbg;
import dbg.Dbg.Color;

public class Client {
  private Client() {
  }

  public static void main(String[] args) {

    try {

      Registry register = LocateRegistry.getRegistry("localhost");

      Hello stubObjRemotoCliente = (Hello) register.lookup("Hello");

      String response = stubObjRemotoCliente.oi();

      Dbg.log("response: " + response);

      Dbg.log(Color.BLUE, "Bem vindo ao sistema de gerenciamento de carros!");

      while (true) {
        Dbg.log(Color.CYAN, "1 - Adicionar carro");
        Dbg.log(Color.CYAN, "2 - Apagar carro");
        Dbg.log(Color.CYAN, "3 - Listar carros");
        Dbg.log(Color.CYAN, "4 - Listar carros por categoria");
        Dbg.log(Color.CYAN, "5 - Pesquisar carro");
        Dbg.log(Color.CYAN, "6 - Atualizar carro");
        Dbg.log(Color.CYAN, "7 - Exibir quantidade de carros");
        Dbg.log(Color.CYAN, "8 - Comprar carro");
        Dbg.log(Color.CYAN, "9 - Sair");
      }

    } catch (Exception e) {
      System.err.println("Cliente: " + e.toString());
      e.printStackTrace();
    }
  }
}
