package com.b07.store;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import com.b07.database.helper.*;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.users.Admin;
import com.b07.users.Employee;
import com.b07.users.User;

public class AdminImp {
  public static boolean AdminInterface(Admin administrator)
      throws IOException, DatabaseInsertException, SQLException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    // stuff
    int userId = -1;
    int roleId = -1;
    boolean deserialize = false;
    String roleName = "";
    boolean correctPass = false;
    String promoting = "y";
    int userInput = -1;
    boolean completePromote = false;

    while (userInput != 7) {
      try {
        System.out.println("1. Promote employee" + "\n2. View complete sales History"
            + "\n3. Display list of all inactive accounts for a given customer"
            + "\n4. Display a list of all active acounts for a given user"
            + "\n5. Serialize Database" + "\n6. Deserialize Database to android" + "\n7. Exit"); // atm
                                                                                                 
        userInput = Integer.parseInt(br.readLine());

        switch (userInput) {
          case 1:
            System.out.println("Enter Employee Id");
            userId = Integer.parseInt(br.readLine());
            roleId = DatabaseSelectHelper.getUserRole(userId);
            roleName = DatabaseSelectHelper.getRoleName(roleId);

            if (roleName.equals("EMPLOYEE")) {
              User employee = DatabaseSelectHelper.getUserDetails(userId);
              completePromote = administrator.promoteEmployee((Employee) employee); 
                                                                       
              employee = null;
            } else {
              System.out.println("User is not am employee");
            }

            if (completePromote) {
              System.out.println("Promotions Complete");
            } else {
              System.out.println("Promotion failed");
            }
            break;

          case 2:
            SalesLog totalLog = DatabaseSelectHelper.getSales();
            System.out.println(totalLog.toString());
            break;

          case 3:
            int customerId;
            System.out.println("Enter customerId");
            customerId = Integer.parseInt(br.readLine());
            roleId = DatabaseSelectHelper.getUserRole(customerId);
            roleName = DatabaseSelectHelper.getRoleName(roleId);

            if (!roleName.equals("CUSTOMER")) {
              System.out.println("NOT A CUSTOMER");
              break;
            }

            List<Integer> inactiveAcct = DatabaseSelectHelper.getUserInactiveAccounts(customerId);

            if (!inactiveAcct.isEmpty()) {
              System.out.println("Here are the inactive account ids: ");
              for (int i = 0; i < inactiveAcct.size(); i++) {
                System.out.println(inactiveAcct.get(i));
              }
              System.out.println("End of inactive accounts\n");
            } else
              System.out.println("No inactive accounts found");

            break;

          case 4:
            System.out.println("Enter customerId");
            customerId = Integer.parseInt(br.readLine());
            roleId = DatabaseSelectHelper.getUserRole(customerId);
            roleName = DatabaseSelectHelper.getRoleName(roleId);

            if (!roleName.equals("CUSTOMER")) {
              System.out.println("NOT A CUSTOMER");
              break;
            }

            List<Integer> activeAcct = DatabaseSelectHelper.getUserActiveAccounts(customerId);

            if (!activeAcct.isEmpty()) {
              System.out.println("Here are the active account ids: ");
              for (int i = 0; i < activeAcct.size(); i++) {
                System.out.println(activeAcct.get(i));
              }
              System.out.println("End of active accounts\n");
            } else
              System.out.println("No active accounts found");

            break;

          case 5:
            Serialize.beginSerialization();
            break;
          case 6:
            deserialize = true;
            break;
            
          case 7:
            deserialize = false;
            break;
          

          default:
            System.out.println("Invalid Selection");
            break;

        }

      } catch (Exception e) {
        e.printStackTrace();
      }

    }

    System.out.println("Complete");
    return deserialize;
  }

}
