package com.b07.users;

import java.sql.SQLException;

public class Employee extends User {

  public Employee(int id, String name, int age, String address) throws SQLException {
    super(id, name, age, address);
    // TODO Auto-generated constructor stub
  }
  
  public Employee(int id, String name, int age, String address, boolean authenticated) throws SQLException {
    super(id, name, age, address, authenticated);
    // TODO Auto-generated constructor stub
  }

}
