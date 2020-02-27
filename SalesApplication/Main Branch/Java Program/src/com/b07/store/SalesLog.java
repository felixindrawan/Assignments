package com.b07.store;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import com.b07.users.User;

public interface SalesLog extends Serializable{
  
  //will have a hashtable,
  //able to return that hashtable
  
  public List<Sale> getLog();
  
  public void setLog(List<Sale>log);
  
  public void UpdateLog(Sale sale);
  



}
