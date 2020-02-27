package com.b07.users;

import java.io.Serializable;
import java.sql.SQLException;
import com.b07.security.PasswordHelpers;
import com.b07.database.helper.*;

public abstract class User implements Serializable{
  
  private int id;
  private int age;
  private int roleId;
  private String name;
  private String address;
  private boolean authenticated;
  
  public User(int id, String name, int age, String address) throws SQLException {
    this.id = id;
    this.name = name;
    this.age = age;
    this.address = address;
    this.roleId = DatabaseSelectHelper.getUserRole(this.id);
    
  }
  
  public User(int id, String name, int age, String address, boolean authenticated) throws SQLException {
    this.id = id;
    this.name = name;
    this.age = age;
    this.address = address;
    this.authenticated = authenticated;
    this.roleId = DatabaseSelectHelper.getUserRole(this.id);
    
  }
  
  
  
  public int getId() {
    return this.id;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return this.age;
  }
  
  public void setAge(int age) {
    this.age= age;
  }
  
  public String getAddress() {
    return this.address;
  }
  
  public void setAddress(String address) {
    this.address= address;
  }
  
  public int getRoleId() {
    return this.roleId;
  }
  
  //IMPLEMENT!! //is a final DO NOT FORGET TO IMPLEMENT
  public final boolean authenticate(String password) throws SQLException {
    String hash = DatabaseSelectHelper.getPassword(this.id);
    this.authenticated = PasswordHelpers.comparePassword(hash, password);
    return this.authenticated;

  }
  
  //added for employeeInterface --Authenticate new employee
  public void manualAuthenticated(boolean isAuthenticated) {
	  this.authenticated = true;
  }
  public boolean isAuthenticatedUser() {
	  return authenticated;
  }
  

}
