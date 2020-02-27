package com.b07.store;

import java.sql.SQLException;
import java.util.List;
import com.b07.database.helper.*;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.DatabaseUpdateException;
import com.b07.inventory.*;
import com.b07.users.*;


public class EmployeeInterface {
  
  private Employee currentEmployee;
  private Inventory inventory;

  public EmployeeInterface( Employee employee, Inventory inventory ) {
    this.currentEmployee = employee;
    this.inventory = inventory;
  }

  public EmployeeInterface( Inventory inventory) {
    this.inventory = inventory;
  }

  public void setCurrentEmployee( Employee employee) throws SQLException {
    int employeeId = employee.getId();
    String password = DatabaseSelectHelper.getPassword(employeeId);
    
    if(employee.isAuthenticatedUser()) {
      this.currentEmployee = employee;
    } else {
      System.out.println("Employee is not authenticated");
    }

  }

  public boolean hasCurrentEmployee() {
    return this.currentEmployee == null;
  }

  public boolean restockInventory(Item item, int quantity) throws SQLException, DatabaseInsertException, DatabaseUpdateException {
    //check if the item is inventory 
    List<Item> itemList = DatabaseSelectHelper.getAllItems();
    if(itemList.contains(item)) {
      return DatabaseUpdateHelper.updateInventoryQuantity(quantity, item.getId());
    }else {
      System.out.println("Item is not sold at the store therefore cannot be restocked");
      return false;
    }

  }

  public int createCustomer(String name, int age, String address, String password) throws SQLException, DatabaseUpdateException, DatabaseInsertException {
    int customerId = DatabaseInsertHelper.insertRole("CUSTOMER");
    int newUserId = DatabaseInsertHelper.insertNewUser(name, age, address, password);
    DatabaseInsertHelper.insertUserRole(newUserId, customerId);

    return newUserId;
  }
  
  public int createEmployee (String name, int age, String address, String password) throws SQLException, DatabaseUpdateException, DatabaseInsertException {
    int customerId = DatabaseInsertHelper.insertRole("EMPLOYEE");
    int newUserId = DatabaseInsertHelper.insertNewUser(name, age, address, password); 
    DatabaseInsertHelper.insertUserRole(newUserId, customerId);
    
    return newUserId;
  }
  
  public int createCustomerAccount(int userId) throws DatabaseInsertException, SQLException {
	  int accId = DatabaseInsertHelper.insertAccount(userId);
	  return accId;
  }
  
  public String currEmp() {
	  return currentEmployee.getName();
  }
  

}