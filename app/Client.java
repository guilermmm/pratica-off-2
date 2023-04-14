package app;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import app.Car.Category;
import auth.*;
import dbg.Dbg;
import dbg.Dbg.Color;

public class Client {
  private static String categories = List.of(
      coloredCategory(Category.ECONOMIC),
      coloredCategory(Category.INTERMEDIARY),
      coloredCategory(Category.EXECUTIVE))
      .toString();

  private static String coloredCategory(Category category) {
    switch (category) {
      case ECONOMIC:
        return Color.colored(Color.GREEN_BRIGHT, "ECONOMIC");
      case INTERMEDIARY:
        return Color.colored(Color.YELLOW_BRIGHT, "INTERMEDIARY");
      case EXECUTIVE:
        return Color.colored(Color.RED_BRIGHT, "EXECUTIVE");
      default:
        return null;
    }
  }

  private static void showCars(List<Car> cars) {
    if (cars.isEmpty()) {
      Dbg.log(Color.RED, "Nenhum carro encontrado");
      return;
    }

    String renavamTitle = "Renavam";
    String nameTitle = "Nome";
    String categoryTitle = "Categoria";
    String yearTitle = "Ano";
    String quantityTitle = "Quantidade";
    String priceTitle = "Preço";

    int maxRenavamLength = Integer.max(
        cars.stream().mapToInt(car -> car.getRenavam().length()).max().orElse(0),
        renavamTitle.length());
    int maxNameLength = Integer.max(
        cars.stream().mapToInt(car -> car.getName().length()).max().orElse(0),
        nameTitle.length());
    int maxCategoryLength = Integer.max(
        cars.stream().mapToInt(car -> car.getCategory().toString().length()).max().orElse(0),
        categoryTitle.length());
    int maxYearLength = Integer.max(
        cars.stream().mapToInt(car -> String.valueOf(car.getYear()).length()).max().orElse(0),
        yearTitle.length());
    int maxQuantityLength = Integer.max(
        cars.stream().mapToInt(car -> String.valueOf(car.getQuantity()).length()).max().orElse(0),
        quantityTitle.length());
    int maxPriceLength = Integer.max(
        cars.stream().mapToInt(car -> String.format("$%.2f", car.getPrice()).length()).max().orElse(0),
        priceTitle.length());

    String renavamHeader = renavamTitle + " ".repeat(maxRenavamLength - 7);
    String nameHeader = nameTitle + " ".repeat(maxNameLength - 4);
    String categoryHeader = categoryTitle + " ".repeat(maxCategoryLength - 9);
    String yearHeader = yearTitle + " ".repeat(maxYearLength - 3);
    String quantityHeader = quantityTitle + " ".repeat(maxQuantityLength - 10);
    String priceHeader = priceTitle + " ".repeat(maxPriceLength - 5);

    String header = String.format(
        "| %s | %s | %s | %s | %s | %s |",
        Color.colored(Color.BLUE, renavamHeader),
        Color.colored(Color.BLUE, nameHeader),
        Color.colored(Color.BLUE, categoryHeader),
        Color.colored(Color.BLUE, yearHeader),
        Color.colored(Color.BLUE, quantityHeader),
        Color.colored(Color.BLUE, priceHeader));

    String middleLine = String.format(
        "| %s | %s | %s | %s | %s | %s |",
        "-".repeat(maxRenavamLength),
        "-".repeat(maxNameLength),
        "-".repeat(maxCategoryLength),
        "-".repeat(maxYearLength),
        "-".repeat(maxQuantityLength),
        "-".repeat(maxPriceLength));

    String borderLine = "=".repeat(middleLine.length());

    Dbg.log(borderLine);
    Dbg.log(header);
    Dbg.log(middleLine);

    cars.stream().forEach(car -> {
      String renavamPadding = " ".repeat(maxRenavamLength - car.getRenavam().length());
      String namePadding = " ".repeat(maxNameLength - car.getName().length());
      String categoryPadding = " ".repeat(maxCategoryLength - car.getCategory().toString().length());
      String yearPadding = " ".repeat(maxYearLength - String.valueOf(car.getYear()).length());
      String quantityPadding = " ".repeat(maxQuantityLength - String.valueOf(car.getQuantity()).length());
      String pricePadding = " ".repeat(maxPriceLength - String.valueOf(car.getPrice()).length());

      String renavam = Color.colored(Color.BLUE, car.getRenavam()) + renavamPadding;
      String name = Color.colored(Color.BLUE, car.getName()) + namePadding;
      String category = coloredCategory(car.getCategory()) + categoryPadding;
      String year = Color.colored(Color.BLUE, String.valueOf(car.getYear())) + yearPadding;
      String quantity = Color.colored(Color.BLUE, String.valueOf(car.getQuantity())) + quantityPadding;
      String price = Color.colored(Color.GREEN_BRIGHT, String.format("$%.2f", car.getPrice()).replace(".", ","))
          + pricePadding;

      String line = String.format("| %s | %s | %s | %s | %s | %s |", renavam, name, category, year, quantity, price);

      Dbg.log(line);
    });

    Dbg.log(borderLine);
  }

  public static void main(String[] args) {
    try (Dbg dbg = new Dbg()) {
      Registry register = LocateRegistry.getRegistry("localhost");

      DealershipApi stub = (DealershipApi) register.lookup("dealership");

      Dbg.log(Color.BLUE, "Bem vindo ao sistema de gerenciamento de carros!");

      User user = null;

      while (user == null) {
        try {
          String username = dbg.input(Color.YELLOW_BRIGHT, "Digite seu nome de usuário: ", Color.BLUE);

          try {
            user = stub.login(username, "");
          } catch (Exception e) {
            String password = dbg.input(Color.YELLOW_BRIGHT, "Digite sua senha: ", Color.BLUE);
            user = stub.login(username, password);
          }

          if (user instanceof Admin) {
            Dbg.log(Color.BLUE, "Bem vindo funcionário " + Color.CYAN + user.getUsername());
          } else {
            Dbg.log(Color.BLUE, "Bem vindo cliente " + Color.CYAN + user.getUsername());
          }
        } catch (Exception e) {
          Dbg.log(Color.RED, "Erro ao fazer login: " + e.getCause().getMessage());
        }
      }

      var actions = user.getAllowedActions();

      while (true) {
        for (int i = 0; i < actions.length; i++) {
          Dbg.log(Color.CYAN, (i + 1) + " - " + actions[i].getTitle());
        }
        Dbg.log(Color.CYAN, (actions.length + 1) + " - Sair");

        String input = dbg.input(Color.YELLOW, "", Color.GREEN);

        try {
          int option = Integer.parseInt(input);

          if (option == actions.length + 1) {
            break;
          } else if (option < 1 || option > actions.length + 1) {
            Dbg.log(Color.RED, "Opção inválida");
            continue;
          }

          Action action = actions[option - 1];

          switch (action) {
            case CREATE: {
              String renavam = dbg.input(Color.YELLOW_BRIGHT, "Digite o renavam do carro: ", Color.GREEN);

              Car carByRenavam = stub.getCarByRenavam(user, renavam);

              if (carByRenavam != null) {
                Dbg.log(Color.BLUE, "Carro com este renavam já está cadastrado");
                Dbg.log(Color.BLUE, "Carro: " + carByRenavam);
                String quantityString = dbg.input(
                    Color.BLUE,
                    "Gostaria de adicionar que quantidade a este carro?",
                    Color.GREEN);

                try {
                  int quantity = Integer.parseInt(quantityString);
                  carByRenavam.setQuantity(carByRenavam.getQuantity() + quantity);
                  stub.updateCar(user, renavam, carByRenavam);
                } catch (Exception e) {
                  Dbg.log(Color.RED, "Quantidade inválida");
                }

                break;
              }

              String name = dbg.input(Color.YELLOW_BRIGHT, "Digite o nome do carro: ", Color.GREEN);

              Category category = null;
              while (category == null) {
                String categoryInput = dbg.input(Color.YELLOW_BRIGHT,
                    "Digite a categoria do carro " + categories + ": ",
                    Color.GREEN);

                try {
                  category = Category.fromString(categoryInput.toUpperCase());
                } catch (Exception e) {
                  Dbg.log(Color.RED, "Categoria inválida");
                }
              }

              int year = 0;
              while (year == 0) {
                String yearInput = dbg.input(Color.YELLOW_BRIGHT, "Digite o ano de lançamento do carro: ", Color.GREEN);

                try {
                  year = Integer.parseInt(yearInput);
                } catch (Exception e) {
                  Dbg.log(Color.RED, "Ano inválido");
                }
              }

              double price = 0;
              while (price == 0) {
                String priceInput = dbg.input(Color.YELLOW_BRIGHT, "Digite o preço do carro: $", Color.GREEN);

                try {
                  price = Double.parseDouble(priceInput.replace(",", "."));
                } catch (Exception e) {
                  Dbg.log(Color.RED, "Preço inválido");
                }
              }

              int quantity = 0;
              while (quantity == 0) {
                String quantityInput = dbg.input(Color.YELLOW_BRIGHT, "Digite a quantidade do carro: ", Color.GREEN);

                try {
                  quantity = Integer.parseInt(quantityInput);
                } catch (Exception e) {
                  Dbg.log(Color.RED, "Quantidade inválida");
                }
              }

              Car car = new Car(renavam, name, category, year, quantity, price);

              stub.addCar(user, car);
            }
              break;
            case DELETE: {
              String renavam = dbg.input(Color.YELLOW_BRIGHT, "Digite o renavam do carro: ", Color.GREEN);

              stub.removeCar(user, renavam);
            }
              break;
            case LIST: {
              String listForm = dbg.input(Color.YELLOW_BRIGHT,
                  "Deseja listar de forma geral (G) ou por categoria (C)? ",
                  Color.GREEN);

              List<Car> cars = null;

              if (listForm.equalsIgnoreCase("G")) {
                cars = stub.listCars(user);
              } else if (listForm.equalsIgnoreCase("C")) {
                String categoryInput = dbg.input(
                    Color.YELLOW_BRIGHT,
                    "Digite a categoria do carro " + categories + ": ",
                    Color.GREEN);

                try {
                  Category category = Category.fromString(categoryInput.toUpperCase());
                  cars = stub.listCarsByCategory(user, category);
                } catch (Exception e) {
                  Dbg.log(Color.RED, "Categoria inválida");
                }
              } else {
                Dbg.log(Color.RED, "Opção inválida");
                break;
              }

              showCars(cars);
            }
              break;
            case SEARCH: {
              String search = dbg.input(Color.YELLOW_BRIGHT, "Digite o termo de busca (Nome ou Renavam): ",
                  Color.GREEN);

              List<Car> cars = stub.searchCars(user, search);

              showCars(cars);
            }
              break;
            case UPDATE: {
              String renavam = dbg.input(Color.YELLOW_BRIGHT, "Digite o renavam do carro a ser alterado: ",
                  Color.GREEN);

              Car carByRenavam = stub.getCarByRenavam(user, renavam);

              if (carByRenavam == null) {
                Dbg.log(Color.RED, "Carro não encontrado");
                break;
              }

              showCars(List.of(carByRenavam));

              renavam = dbg.input(Color.YELLOW_BRIGHT, "Digite o novo renavam: ", Color.GREEN);

              String name = dbg.input(Color.YELLOW_BRIGHT, "Digite o novo nome: ", Color.GREEN);

              Category category = null;
              while (category == null) {
                String categoryInput = dbg.input(Color.YELLOW_BRIGHT, "Digite a nova categoria " + categories + ": ",
                    Color.GREEN);

                try {
                  category = Category.fromString(categoryInput.toUpperCase());
                } catch (Exception e) {
                  Dbg.log(Color.RED, "Categoria inválida");
                }
              }

              int year = 0;
              while (year == 0) {
                String yearInput = dbg.input(Color.YELLOW_BRIGHT, "Digite o novo ano de lançamento: ", Color.GREEN);

                try {
                  year = Integer.parseInt(yearInput);
                } catch (Exception e) {
                  Dbg.log(Color.RED, "Ano inválido");
                }
              }

              double price = 0;
              while (price == 0) {
                String priceInput = dbg.input(Color.YELLOW_BRIGHT, "Digite o novo preço: $", Color.GREEN);

                try {
                  price = Double.parseDouble(priceInput.replace(",", "."));
                } catch (Exception e) {
                  Dbg.log(Color.RED, "Preço inválido");
                }
              }

              int quantity = 0;
              while (quantity == 0) {
                String quantityInput = dbg.input(Color.YELLOW_BRIGHT, "Digite a nova quantidade: ", Color.GREEN);

                try {
                  quantity = Integer.parseInt(quantityInput);
                } catch (Exception e) {
                  Dbg.log(Color.RED, "Quantidade inválida");
                }
              }

              Car car = new Car(renavam, name, category, year, quantity, price);

              stub.updateCar(user, renavam, car);
            }
              break;
            case QUANTITY: {
              int quantity = stub.getTotalQuantity(user);

              Dbg.log(Color.YELLOW_BRIGHT, "Quantidade de carros: " + quantity);
            }
              break;
            case BUY: {
              String renavam = dbg.input(Color.YELLOW_BRIGHT, "Digite o renavam do carro a ser comprado: ",
                  Color.GREEN);

              Car car = stub.getCarByRenavam(user, renavam);

              if (car == null) {
                Dbg.log(Color.RED, "Carro não encontrado");
                break;
              }

              if (car.getQuantity() == 0) {
                Dbg.log(Color.RED, "Carro sem estoque");
                break;
              }

              showCars(List.of(car));

              String priceInput = dbg.input(
                  Color.YELLOW_BRIGHT,
                  "Confirme o valor do carro a ser comprado: $",
                  Color.GREEN);

              try {
                double price = Double.parseDouble(priceInput.replace(",", "."));

                if (price != car.getPrice()) {
                  Dbg.log(Color.RED, "Preço inválido. Compra cancelada");
                  break;
                }
              } catch (Exception e) {
                Dbg.log(Color.RED, "Preço inválido. Compra cancelada");
                break;
              }

              // confirmar compra
              String confirmInput = dbg.input(Color.YELLOW_BRIGHT, "Deseja confirmar a compra (S/N)? ", Color.GREEN);

              if (confirmInput.equalsIgnoreCase("S")) {
                stub.buyCar(user, renavam);

                Dbg.log(Color.GREEN_BRIGHT, "Compra realizada com sucesso");
              } else {
                Dbg.log(Color.RED, "Compra cancelada");
              }
            }
              break;
          }
        } catch (NumberFormatException e) {
          Dbg.log(Color.RED, "Opção inválida");
          continue;
        }
      }

    } catch (Exception e) {
      System.err.println("Cliente: " + e.toString());
      e.printStackTrace();
    }
  }

}
