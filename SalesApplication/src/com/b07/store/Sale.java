package com.b07.store;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import com.b07.inventory.Item;
import com.b07.users.User;


public interface Sale extends Serializable {
  
  public int getId();
  
  public void setId(int id);
  
  public User getUser();
  
  public void setUser(User id);
  
  public BigDecimal getTotalPrice();
  
  public void setTotalPrice(BigDecimal id);
  
  public HashMap<Item,Integer> getItemMap();
  
  public void setItemMap(HashMap<Item,Integer> itemMap);
  
}
