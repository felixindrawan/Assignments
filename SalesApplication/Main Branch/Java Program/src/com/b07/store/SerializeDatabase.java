package com.b07.store;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import com.b07.inventory.Inventory;
import com.b07.inventory.Item;
import com.b07.users.User;

public class SerializeDatabase implements Serializable {
  // this will be the object that stores EVERYTHING

  private ArrayList<User> userList;
  private ArrayList<String> passwords;
  private ArrayList<Account> accounts;
  private HashMap<Integer, String> roles;
  private ArrayList<Item> items;
  private Inventory inventory;
  private SalesLog log;



  public SerializeDatabase(ArrayList<User> userList, ArrayList<String> passwords,
      ArrayList<Account> accounts, HashMap<Integer, String> roles, ArrayList<Item> items,
      Inventory inventory, SalesLog log) {
    
    this.userList = userList;
    this.passwords = passwords;
    this.accounts = accounts;
    this.roles = roles;
    this.items = items;
    this.inventory = inventory;
    this.log = log;
   
  }

  public ArrayList<String> getPasswords() {
    return passwords;
  }

  public void setPasswords(ArrayList<String> passwords) {
    this.passwords = passwords;
  }

  public ArrayList<Account> getAccounts() {
    return accounts;
  }

  public void setAccounts(ArrayList<Account> accounts) {
    this.accounts = accounts;
  }

  public HashMap<Integer, String> getRoles() {
    return roles;
  }

  public void setRoles(HashMap<Integer, String> roles) {
    this.roles = roles;
  }

  public ArrayList<Item> getItems() {
    return items;
  }

  public void setItems(ArrayList<Item> items) {
    this.items = items;
  }

  public Inventory getInventory() {
    return inventory;
  }

  public void setInventory(Inventory inventory) {
    this.inventory = inventory;
  }

  public SalesLog getLog() {
    return log;
  }

  public void setLog(SalesLog log) {
    this.log = log;
  }
  
  public ArrayList<User> getUserList() {
    return this.userList;
  }

  public void setUserList(ArrayList<User> userList) {
    this.userList = userList;
  }
  
  


  
  

}

