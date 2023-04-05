package Connection;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Um objeto remoto é uma instância de uma classe que
 * implementa uma interface remota.
 *
 * Uma interface remota estende a interface java.rmi.Remote
 * e declara um conjunto de métodos remotos.
 *
 * Cada método remoto deve declarar java.rmi.RemoteException
 * (ou uma superclasse de RemoteException)
 * em sua cláusula de throws, além de quaisquer
 * exceções específicas da aplicação.
 */
// interface do objeto servidor
public interface Hello extends Remote {
  // método a ser implementado
  String oi() throws RemoteException;

}