package com.b07.database.helper;

import com.b07.database.DatabaseUpdater;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.DatabaseUpdateException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DatabaseUpdateHelper extends DatabaseUpdater {

  public static boolean updateRoleName(String name, int id) throws SQLException, DatabaseUpdateException {
    // TODO Implement this method as stated on the assignment sheet (Strawberry)
    // hint: You should be using these three lines in your final code
    String nameUpper = name.toUpperCase();
    
    if(!nameUpper.equals("ADMIN") && !nameUpper.equals("CUSTOMER") && !nameUpper.equals("EMPLOYEE")) {
      System.out.print("Invalid Role, Please try Admin, Employee or Customer");
      throw new DatabaseUpdateException();
    }
    
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateRoleName(nameUpper, id, connection);
    connection.close();
    return complete;


  }

  public static boolean updateUserName(String name, int userId) throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateUserName(name, userId, connection);
    connection.close();
    return complete;
  }

  public static boolean updateUserAge(int age, int userId) throws SQLException, DatabaseUpdateException {
    if (age < 0) {
      System.out.print("Age is not valid");
      throw new DatabaseUpdateException();
    }
    
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateUserAge(age, userId, connection);
    connection.close();
    return complete;
  }

  public static boolean updateUserAddress(String address, int userId) throws SQLException, DatabaseUpdateException {
    
    if(address.length() > 100) {  
      System.out.print("Adress length Exceeded 100 character limit");
      throw new DatabaseUpdateException();
    }
    
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateUserAddress(address, userId, connection);
    connection.close();
    return complete;

  }
  
  private static boolean IsRoleValid(int roleId) throws SQLException {
    List<Integer> idList = DatabaseSelectHelper.getRoleIds();
    return idList.contains(roleId);
     
  }

  public static boolean updateUserRole(int roleId, int userId) throws SQLException, DatabaseUpdateException {
    
    if(IsRoleValid(roleId) == false) {
      System.out.print("Invalid RoleId");
      throw new DatabaseUpdateException();
    }
    
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateUserRole(roleId, userId, connection);
    connection.close();
    return complete;

  }

  public static boolean updateItemName(String name, int itemId) throws SQLException, DatabaseUpdateException {
    int nameSize = name.length();
    if(nameSize >= 64 || name == null) {
      System.out.println("Name must not be Null, and less than 64 characters");
      throw new DatabaseUpdateException();
    }
    
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateItemName(name, itemId, connection);
    connection.close();
    return complete;

  }

  public static boolean updateItemPrice(BigDecimal price, int itemId) throws SQLException, DatabaseUpdateException {
    BigDecimal zero = new BigDecimal("ZERO");
    
    if(price.compareTo(zero) == -1) {
      System.out.println("Price must be >= zero");
      throw new DatabaseUpdateException();
    }
    
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateItemPrice(price, itemId, connection);
    connection.close();
    return complete;
  }

  public static boolean updateInventoryQuantity(int quantity, int itemId) throws SQLException, DatabaseUpdateException {
    
    if(quantity < 0) {
      throw new DatabaseUpdateException();
    }
    
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateInventoryQuantity(quantity, itemId, connection);
    connection.close();
    return complete;
  }
  
  public static boolean updateAccountStatus(int accountId, boolean active) throws SQLException {
  	  Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
  	  boolean successUp = DatabaseUpdater.updateAccountStatus(accountId, active, connection);
  	  connection.close();
  	  return successUp;
    }
  
}
