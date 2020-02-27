package com.b07.users;

import java.sql.SQLException;
import com.b07.database.helper.*;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.DatabaseUpdateException;

public class Admin extends User {
  
  public Admin(int id, String name, int age, String address) {
    super(id, name, age, address); 
  }
  
  public Admin(int id, String name, int age, String address, boolean authenticated) {
    super(id, name, age, address,authenticated); 
  }
  
  public boolean promoteEmployee(Employee employee) throws SQLException, DatabaseInsertException, DatabaseUpdateException {
    int employeeId = employee.getId();
    
    boolean success = DatabaseUpdateHelper.updateRoleName("ADMIN", employeeId);
    
    return success;
    
  }

}
