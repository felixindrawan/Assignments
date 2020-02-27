package com.b07.store;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.exceptions.DatabaseUpdateException;
import com.b07.inventory.Item;
import com.b07.users.User;

public class SalesImp implements Sale {
  
  private int salesId;
  private Item item;
  private User user;
  private HashMap<Item, Integer>itemMap;
  private BigDecimal totalPrice;
  
  
  public SalesImp(int userId, int salesId) throws SQLException, DatabaseUpdateException {
    
    user = DatabaseSelectHelper.getUserDetails(userId);
    totalPrice = new BigDecimal("0.00");
    this.salesId = salesId;
    
    
  }

  @Override
  public int getId() {
    // TODO Auto-generated method stub
    return this.salesId;
  }

  @Override
  public void setId(int id) {
    // TODO Auto-generated method stub
    this.salesId = id;
  }

  @Override
  public User getUser() {
    // TODO Auto-generated method stub
    return this.user;
  }

  @Override
  public void setUser(User id) {
    // TODO Auto-generated method stub
    this.user = id;
   
  }

  @Override
  public BigDecimal getTotalPrice() {
    // TODO Auto-generated method stub
    return totalPrice;
  }

  @Override
  public void setTotalPrice(BigDecimal id) {
    // TODO Auto-generated method stub
    totalPrice = id;

  }

  @Override
  public HashMap<Item, Integer> getItemMap() {
    // TODO Auto-generated method stub
    return this.itemMap;
  }

  @Override
  public void setItemMap(HashMap<Item, Integer> itemMap) {
    // TODO Auto-generated method stub
    this.itemMap = itemMap;
  }
  
  public String toString() {
    // i have a sales Id and i have a user Id
    //first get customer name
    String customerName = this.user.getName();
    String itemized = "Itemized BreakDown: ";
    String output = "";
    boolean first = true;
    output += "\nCustomer: " + customerName + ""
        + "\nPurchase Number: " + this.salesId + ""
            + "\nTotal Purchase Price: " + this.getTotalPrice();
        
   
    
    for (Map.Entry mapElement : this.getItemMap().entrySet()) { 
      Item item = (Item)mapElement.getKey(); 
      int quantity = ((int)mapElement.getValue()); 
      
      output += "\n" + itemized + item.getName() + ": " + quantity;
      
      if(first) {
        itemized = "                    ";
        first = false;
      }

     
    }
    

    
    
    return output;
  }
  
  

}
