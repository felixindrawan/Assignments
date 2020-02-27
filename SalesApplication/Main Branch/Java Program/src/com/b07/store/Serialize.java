package com.b07.store;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.b07.database.helper.DatabaseDriverHelper;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
import com.b07.exceptions.ConnectionFailedException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.DatabaseUpdateException;
import com.b07.inventory.*;
import com.b07.users.User;

public class Serialize {



  // method create the giant ass object that will hold all of the objects
  public static String beginSerialization() throws SQLException, DatabaseUpdateException {


    ArrayList<User> users = (ArrayList<User>) DatabaseSelectHelper.getUsersDetails();
    ArrayList<String> passwords = new ArrayList<>();
    ArrayList<Account> accounts = new ArrayList<Account>();

    for (User user : users) {
      passwords.add(DatabaseSelectHelper.getPassword(user.getId())); // hashed passwords find a way
                                                                     // to get around this later

      Account currentUserAccount = new Account(user.getId());
      currentUserAccount.setActiveAccounts(
          (ArrayList<Integer>) DatabaseSelectHelper.getUserActiveAccounts(user.getId()));
      currentUserAccount.setInactiveAccounts(
          (ArrayList<Integer>) DatabaseSelectHelper.getUserInactiveAccounts(user.getId()));

      ArrayList<Integer> userAccounts =
          (ArrayList<Integer>) DatabaseSelectHelper.getUserAccounts(user.getId());
      for (int userAccount : userAccounts) {
        // get details of each account
        HashMap<Integer, Integer> specific = DatabaseSelectHelper.getAccountDetails(userAccount);
        currentUserAccount.addAccount(specific);
      }

      accounts.add(currentUserAccount);

    }

    // Role hashMap
    ArrayList<Integer> roleIds = (ArrayList<Integer>) DatabaseSelectHelper.getRoleIds();
    HashMap<Integer, String> roles = new HashMap<>();
    for (int id : roleIds) {
      roles.put(id, DatabaseSelectHelper.getRoleName(id));
    }

    // Item List
    ArrayList<Item> items = (ArrayList<Item>) DatabaseSelectHelper.getAllItems();

    // inventory
    Inventory inventory = DatabaseSelectHelper.getInventory(); // there is nothing here


    // Sales
    SalesLog log = DatabaseSelectHelper.getSales();


    /*
     * users (User ArrayList) passwords (password ArrayList) Accounts (account ArrayList) roles
     * (HashMap(Id, name)) items (item ArrayList) inventory (inventory) log (sales Log)
     * 
     */

    SerializeDatabase base =
        new SerializeDatabase(users, passwords, accounts, roles, items, inventory, log);

    String name = serialize(base);

    System.out.println("Complete");
    return name;
  }


  // reform the database

  public static void updateDatabase(SerializeDatabase base)
      throws ConnectionFailedException, DatabaseInsertException, SQLException {
    String name = "";
    String address = "";
    String password = "";
    String roleName = "";
    int userId = -1;
    int age = -1;
    int roleId = -1;
    int itemId = -1;
    int saleId = -1;
    int accountId = -1;

    
    // close the connection
    // clear the original database
    //close database connection

    DatabaseDriverExtender.createEmptyDatabase();

    // start inserting everything

    // Insert all users, hard write the passwords
    ArrayList<User> users = base.getUserList();
    HashMap<Integer, String> roles = base.getRoles();

    for (User user : users) {
      // start inserting stuff
      name = user.getName();
      age = user.getAge();
      address = user.getAddress();
      userId = DatabaseInsertHelper.insertNewUser(name, age, address, name); // Note the written
                                                                             // password is the
                                                                             // user's name
      roleId = user.getRoleId();
      roleName = roles.get(roleId);

      roleId = DatabaseInsertHelper.insertRole(roleName);
      DatabaseInsertHelper.insertUserRole(userId, roleId);
    }

    // Insert Items and inventory
    ArrayList<Item> items = base.getItems();
    Inventory inventory = base.getInventory();
    HashMap<Item, Integer> itemMap = inventory.getItemMap();
    for (Item item : items) {
      itemId = DatabaseInsertHelper.insertItem(item.getName(), item.getPrice());
      Item hashedKey = getMapItem(itemMap, item.getId());

      System.out.println(hashedKey.getName());
      int quantity = itemMap.get(hashedKey);
      System.out.println(hashedKey.getName() + " has" + quantity);
      DatabaseInsertHelper.insertInventory(itemId, quantity);
    }

    // Sales
    SalesLog log = base.getLog();
    ArrayList<Sale> logList = (ArrayList<Sale>) log.getLog();

    for (Sale sale : logList) {
      saleId = DatabaseInsertHelper.insertSale(sale.getUser().getId(), sale.getTotalPrice());
      
      HashMap<Item, Integer> saleDetail = sale.getItemMap();

      try {
        for (Map.Entry mapElement : saleDetail.entrySet()) {
          Item itemized = (Item) mapElement.getKey();
          int quantity = (int) saleDetail.get(itemized);
          DatabaseInsertHelper.insertItemizedSale(saleId, itemized.getId(), quantity);
        }
      } catch (Exception e) {
        System.out.println("No Sales");
      }

    }

    // Accounts
    ArrayList<Account> accounts = base.getAccounts();

    for (Account account : accounts) { // each user
      ArrayList<HashMap<Integer, Integer>> allUserAccounts = account.getAccountDetails();
      ArrayList<Integer> activeAccounts = account.getActiveAccounts();

      for (HashMap<Integer, Integer> cart : allUserAccounts) {
        accountId = DatabaseInsertHelper.insertAccount(account.getId());
        boolean active = false;
        if (activeAccounts.contains(accountId)) {
          active = true;
        }
        DatabaseUpdateHelper.updateAccountStatus(accountId, active);

        for (Map.Entry mapElement : cart.entrySet()) {
          int currentItemId = (int) mapElement.getKey();
          int quantity = (int) cart.get(currentItemId);

          int recordId = DatabaseInsertHelper.insertAccountLine(accountId, currentItemId, quantity);
        }

      }
    }

  }

  private static Item getMapItem(HashMap<Item, Integer> shopMap, int itemId) {
    Item currentItem = null;
    for (Map.Entry mapElement : shopMap.entrySet()) {
      currentItem = (Item) mapElement.getKey();

      // Add some bonus marks
      // to all the students and print it
      if (currentItem.getId() == itemId) {
        return currentItem;
      }

    }

    return null;

  }



  // For now keep the file location the same
  public static Object deserialize(String fileName) {
    try {
      FileInputStream fileIn = new FileInputStream("Serialized Database\\" + fileName);
      ObjectInputStream in = new ObjectInputStream(fileIn);
      Object z = in.readObject();
      in.close();
      fileIn.close();
      return z;
    } catch (IOException i) {
      i.printStackTrace();
      return null;
    } catch (ClassNotFoundException c) {
      System.out.println("Cat class not found");
      c.printStackTrace();
      return null;
    }
  }

  private static String serialize(Serializable x) {
    try {
      FileOutputStream fileOut = new FileOutputStream("Serialized Database\\" + "database_copy");
      ObjectOutputStream out = new ObjectOutputStream(fileOut);
      out.writeObject(x);
      out.close();
      fileOut.close();
      System.out.println("Serialized data is saved in Serialized Database" + x.toString());
    } catch (IOException i) {
      i.printStackTrace();
    }
    return x.toString();
  }

}
