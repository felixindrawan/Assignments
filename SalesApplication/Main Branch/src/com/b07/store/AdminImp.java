package com.b07.store;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.SQLException;
import com.b07.database.helper.*;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.users.Admin;
import com.b07.users.Employee;
import com.b07.users.User;

public class AdminImp {
  public static void AdminInterface() throws IOException, DatabaseInsertException, SQLException {
    BufferedReader br = new BufferedReader (new InputStreamReader(System.in));
    //stuff
    int userId = -1;
    int roleId = -1;
    String roleName = "";
    Admin administrator = null;
    boolean correctPass = false;
    String promoting = "y";
    int userInput = -1;
    boolean completePromote = false;
    /*
    BigDecimal price = new BigDecimal("10.00");
    DatabaseInsertHelper.insertItem("HockeyStick", price);
    
    price = new BigDecimal("10.00");
    DatabaseInsertHelper.insertItem("Skates", price);
    
    price = new BigDecimal("10.00");
    DatabaseInsertHelper.insertItem("Fishrod", price);
    
    price = new BigDecimal("10.00");
    DatabaseInsertHelper.insertItem("Protein", price);
   */
    // insert into invent
    /*
    DatabaseInsertHelper.insertInventory(1, 8);
    DatabaseInsertHelper.insertInventory(2, 8);
    DatabaseInsertHelper.insertInventory(3, 8);
    DatabaseInsertHelper.insertInventory(4, 8);
    */
    
    
    while (userInput != 3) {
      try {
        System.out.println("1. Promote employee"
            + "\n2. View complete sales History"
            + "\n3. Exit");
        
        userInput = Integer.parseInt(br.readLine());
        
        
        
        switch(userInput) {
          case 1: 
            System.out.println("Enter Employee Id");
            userId = Integer.parseInt(br.readLine());
            roleId = DatabaseSelectHelper.getUserRole(userId);
            roleName = DatabaseSelectHelper.getRoleName(roleId);
            
            if (roleName.equals("EMPLOYEE")) {
              User employee = DatabaseSelectHelper.getUserDetails(userId);
              completePromote = administrator.promoteEmployee((Employee) employee); //<-- this thing isnt updating the database
              employee = null;
            } else {
              System.out.println("User is not am employee");
            }
            
            if(completePromote) {
              System.out.println("Promotions Complete");
            } else {
              System.out.println("Promotion failed");
            } break;
            
          case 2: 
            SalesLog totalLog= DatabaseSelectHelper.getSales();
            System.out.println(totalLog.toString()); 
            break;
          case 3: break;
          default: 
            System.out.println("Invalid Selection"); break;
          
        }
        
      } catch (Exception e) {
        e.printStackTrace();
      }

      
    }

    System.out.println("Complete");
  }

}
