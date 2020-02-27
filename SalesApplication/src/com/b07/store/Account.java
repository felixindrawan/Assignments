package com.b07.store;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import com.b07.database.helper.DatabaseSelectHelper;

public class Account implements Serializable {

  private ArrayList<HashMap<Integer, Integer>> allAccounts;
  // i can get an arraylist holding all of their active and inactive accounts
  private ArrayList<Integer> activeAccounts;
  private ArrayList<Integer> inactiveAccounts;
  private int userId;

  public Account(int id) throws SQLException {

    this.userId = id;
    this.allAccounts = new ArrayList<HashMap<Integer, Integer>>();
    this.activeAccounts = (ArrayList<Integer>) DatabaseSelectHelper.getUserActiveAccounts(id);
    this.inactiveAccounts = (ArrayList<Integer>) DatabaseSelectHelper.getUserInactiveAccounts(id);
  }

  public void addAccount(HashMap<Integer, Integer> account) {
    allAccounts.add(account);
  }

  public ArrayList<HashMap<Integer, Integer>> getAccountDetails() {
    return this.allAccounts;
  }

  public int getId() {
    return this.userId;
  }

  public void setId(int id) {
    this.userId = id;
  }

  public void setAccounts(ArrayList<HashMap<Integer, Integer>> accounts) {
    this.allAccounts = accounts;
  }

  public ArrayList<Integer> getActiveAccounts() {
    return this.activeAccounts;
  }

  public void setActiveAccounts(ArrayList<Integer> accountList) {
    this.activeAccounts = accountList;
  }


  public ArrayList<Integer> getInactiveAccounts() {
    return this.inactiveAccounts;
  }

  public void setInactiveAccounts(ArrayList<Integer> accountList) {
    this.inactiveAccounts = accountList;
  }

}
