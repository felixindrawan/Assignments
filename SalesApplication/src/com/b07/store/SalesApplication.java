package com.b07.store;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import com.b07.database.helper.*;
import com.b07.inventory.BaseInventory;
import com.b07.inventory.Inventory;
import com.b07.users.*;

public class SalesApplication {
  /**
   * This is the main method to run your entire program! Follow the "Pulling it together"
   * instructions to finish this off.
   * 
   * @param argv unused.
   */

  private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

  private List<User> accounts = null;

  public static void main(String[] argv) {
    Inventory inventory = new BaseInventory();
    int userId = -1;
    int roleId = -1;
    int userInput = -1;
    int age = -1;
    String roleName = null;
    String name = null;
    String password = null;
    String address = null;
    String userRole;

    Connection connection = DatabaseDriverExtender.connectOrCreateDataBase();
    if (connection == null) {
      System.out.print("NOOO");
    }
    try {

      if (argv[0].equals("-1")) {

        int relationshipId;
        boolean valid = false;
        DatabaseDriverExtender.initialize(connection);

        while (!valid) {
          try {
            // CREATING ADMIN
            System.out.println("Creating Administrator" + "\nEnter name:");
            name = br.readLine();
            System.out.print("Enter age:");
            age = Integer.parseInt(br.readLine());
            System.out.print("Enter address:");
            address = br.readLine();
            System.out.print("Enter password:");
            password = br.readLine();

            userId = DatabaseInsertHelper.insertNewUser(name, age, address, password);

            roleId = DatabaseInsertHelper.insertRole("ADMIN");

            relationshipId = DatabaseInsertHelper.insertUserRole(userId, roleId);
            System.out.println("Your Admin Id:" + relationshipId);
            valid = true;
          } catch (Exception e) {
            System.out.println("\nInvalid Input Try again");

          }
        }

        valid = false;

        while (!valid) {
          try {
            // CREATING Employee
            System.out.println("\nCreate User");
            System.out.print("Enter name:");
            name = br.readLine();
            System.out.print("Enter age:");
            age = Integer.parseInt(br.readLine());
            System.out.print("Enter address:");
            address = br.readLine();
            System.out.print("Enter password:");
            password = br.readLine();

            userId = DatabaseInsertHelper.insertNewUser(name, age, address, password);
            roleId = DatabaseInsertHelper.insertRole("EMPLOYEE");
            relationshipId = DatabaseInsertHelper.insertUserRole(userId, roleId); // This needs to
            System.out.println("Your User Id:" + relationshipId);
            valid = true;
            // somewhere I
            // think?
          } catch (Exception e) {
            System.out.println("\nPlease try again invalid input");

          }
        }
        // Initalize the Inventory with 10 of each item

        BigDecimal price = new BigDecimal("10.00");
        DatabaseInsertHelper.insertItem("HockeyStick", price);

        price = new BigDecimal("10.00");
        DatabaseInsertHelper.insertItem("Skates", price);

        price = new BigDecimal("10.00");
        DatabaseInsertHelper.insertItem("Fishrod", price);

        price = new BigDecimal("10.00");
        DatabaseInsertHelper.insertItem("Running Shoes", price);

        price = new BigDecimal("10.00");
        DatabaseInsertHelper.insertItem("Protein", price);

        // insert into invent
        DatabaseInsertHelper.insertInventory(1, 10);
        DatabaseInsertHelper.insertInventory(2, 10);
        DatabaseInsertHelper.insertInventory(3, 10);
        DatabaseInsertHelper.insertInventory(4, 10);
        DatabaseInsertHelper.insertInventory(5, 10);

        System.out.println("\nInitalize Inventory.");
        System.out.println("\nDatabase Intialization Complete.");
      } else if (argv[0].equals("1")) {
        // stuff
      
        
        userRole = "";
        Admin administrator = null;
        boolean correctPass = false;

        // authenticating Admin
        while (correctPass == false || !userRole.equals("ADMIN")) {

          try {
            System.out.println("!@!ADMIN MODE!@!@!");

            System.out.println("UserId: ");
            userId = Integer.parseInt(br.readLine());
            System.out.println("Password: ");
            password = br.readLine();

            administrator = (Admin) DatabaseSelectHelper.getUserDetails(userId);

            userRole = DatabaseSelectHelper
                .getRoleName(DatabaseSelectHelper.getUserRole(administrator.getId()));

            correctPass = administrator.authenticate(password);

          } catch (Exception e) {
            System.out.println("\nInvalid Admin UserId or password.");
          }
         
        }
        
        
        SerializeDatabase base = (SerializeDatabase) Serialize.deserialize("database_copy");
        connection.close();
        Serialize.updateDatabase(base);
        boolean deserialize = true;
        
        while(deserialize) {
          deserialize = AdminImp.AdminInterface(administrator); 
        }

        System.out.println("Admin Mode Complete");

      } else { // DOESNT WORK IF ARGV IS EMPTY
        boolean validIn = false;
        userInput = -1;
        while (!validIn) { // ADDED CONTEXT MENU
          try {

            System.out.println("1 - Employee Login\n2 - Customer Login\n0 - Exit");
            System.out.print("Option: ");
            userInput = Integer.parseInt(br.readLine());

            switch (userInput) {
              case 0:
                validIn = true;
                break;
              case 1:
                validIn = true;
                break;
              case 2:
                validIn = true;
                break;
              default:
                System.out.println("Enter 0 1 or 2");
                break;

            }
          } catch (Exception e) {
            System.out.println("Enter integer.");
          }

        }

        if (userInput == 1) {
          System.out.println("Employee Interface");
          EmployeeInterfaceImp.employeeInterfaceMenu();
        } else if (userInput == 2) {
          ShoppingCartImp.ShoppingCartInterface();
        } else {
          System.out.println("Exitting Program");
          System.exit(0);
        }
      }

    } catch (Exception e) {
      // TODO Improve this!
      e.printStackTrace();
      System.out.println("Something went wrong :(");
    } finally {
      try {
        connection.close();
      } catch (Exception e) {
        System.out.println("Looks like it was closed already :)");
      }
    }

  }
  // check if a Id is valid



}
