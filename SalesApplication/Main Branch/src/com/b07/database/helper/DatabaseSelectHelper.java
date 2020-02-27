package com.b07.database.helper;

import com.b07.database.DatabaseSelector;
import com.b07.exceptions.DatabaseUpdateException;
import com.b07.inventory.BaseInventory;
import com.b07.inventory.Inventory;
import com.b07.inventory.Item;
import com.b07.inventory.items.BaseItem;
import com.b07.store.Sale;
import com.b07.store.SalesImp;
import com.b07.store.SalesLog;
import com.b07.store.SalesLogImp;
import com.b07.users.Admin;
import com.b07.users.Customer;
import com.b07.users.Employee;
import com.b07.users.User;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
 * TODO: Complete the below methods to be able to get information out of the database. TODO: The
 * given code is there to aide you in building your methods. You don't have TODO: to keep the exact
 * code that is given (for example, DELETE the System.out.println()) TODO: and decide how to handle
 * the possible exceptions
 */
public class DatabaseSelectHelper extends DatabaseSelector {

	public static List<Integer> getRoleIds() throws SQLException {
		Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
		ResultSet results = DatabaseSelector.getRoles(connection);
		List<Integer> ids = new ArrayList<>();
		int roleId = -1;

		while (results.next()) {
			roleId = results.getInt("ID");
			ids.add(roleId);
		}

		results.close();
		connection.close();
		return ids;
	}

	public static String getRoleName(int roleId) throws SQLException {
		Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
		String role = DatabaseSelector.getRole(roleId, connection);
		connection.close();
		return role;
	}

	public static int getUserRole(int userId) throws SQLException {
		Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
		int roleId = DatabaseSelector.getUserRole(userId, connection);
		connection.close();
		return roleId;
	}

	public static List<Integer> getUsersByRole(int roleId) throws SQLException {
		Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
		ResultSet results = DatabaseSelector.getUsersByRole(roleId, connection);
		List<Integer> userIds = new ArrayList<>();
		int userId = -1;
		int userRoleId = -1;

		while (results.next()) {
			userId = results.getInt("USERID");
			userRoleId = DatabaseSelectHelper.getUserRole(userId);
			if (userRoleId == roleId) {
				userIds.add(userId);
			}

		}
		results.close();
		connection.close();
		return userIds;

	}

	// based on the user id and there role create the correct type of use and added
	// to the list
	private static User createUser(int id, String name, int age, String address)
			throws SQLException, DatabaseUpdateException {
		int roleId = DatabaseSelectHelper.getUserRole(id);
		String role = DatabaseSelectHelper.getRoleName(roleId);

		if (role.equals("ADMIN")) {
			// attach admin role
			User user = new Admin(id, name, age, address);
			return user;
		} else if (role.equals("EMPLOYEE")) {
			User user = new Employee(id, name, age, address);
			return user;

		} else if (role.equals("CUSTOMER")) {
			User user = new Customer(id, name, age, address);
			return user;

		} else {
			// SHOUDLNT HAPPEN
			throw new DatabaseUpdateException();
		}
	}

	public static List<User> getUsersDetails() throws SQLException, DatabaseUpdateException {
		Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
		ResultSet results = DatabaseSelector.getUsersDetails(connection);
		List<User> users = new ArrayList<>();
		int id = -1;
		int age = -1;
		String name = "";
		String address = "";

		while (results.next()) {
			id = results.getInt("ID");
			age = results.getInt("AGE");
			name = results.getString("NAME");
			address = results.getString("ADDRESS");

			User user = createUser(id, name, age, address);
			users.add(user);
		}

		results.close();
		connection.close();
		return users;
	}

	public static User getUserDetails(int userId) throws SQLException, DatabaseUpdateException {
		Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
		ResultSet results = DatabaseSelector.getUserDetails(userId, connection);
		int id = -1;
		int age = -1;
		String name = "";
		String address = "";

		while (results.next()) {
			id = results.getInt("ID");
			age = results.getInt("AGE");
			name = results.getString("NAME");
			address = results.getString("ADDRESS");

			if (id == userId) {
				User user = createUser(id, name, age, address);
				results.close();
				connection.close();
				return user;
			}
		}
		results.close();
		connection.close();
		return null;
	}

	public static String getPassword(int userId) throws SQLException {
		Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
		String password = DatabaseSelector.getPassword(userId, connection);
		connection.close();
		return password;
	}

	public static List<Item> getAllItems() throws SQLException {
		Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
		ResultSet results = DatabaseSelector.getAllItems(connection);
		List<Item> items = new ArrayList<>();

		int id = -1;
		String name = "";
		BigDecimal price = new BigDecimal("0.00");
		price.setScale(2, BigDecimal.ROUND_HALF_EVEN);

		while (results.next()) {
			id = results.getInt("ID");
			name = results.getString("NAME");
			price = new BigDecimal(results.getString("PRICE"));

			Item newItem = new BaseItem(id, name, price);
			items.add(newItem);
		}
		results.close();
		connection.close();
		return items;
	}

    public static Item getItem(int itemId) throws SQLException {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      ResultSet results = DatabaseSelector.getItem(itemId, connection);

      int id = -1;
      String name = "";
      BigDecimal price = new BigDecimal("0.00");
      price.setScale(2, BigDecimal.ROUND_HALF_EVEN);

      while (results.next()) {
        id = results.getInt("ID");
        name = results.getString("NAME");
        price = new BigDecimal(results.getString("PRICE"));

        if (id == itemId) {
          Item newItem = new BaseItem(id, name, price);
          results.close();
          connection.close();
          return newItem;
        }
      }
      results.close();
      connection.close();
      return null;
    }

	public static Inventory getInventory() throws SQLException {
		Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
		ResultSet results = DatabaseSelector.getInventory(connection);

		int id = -1;
		int quantity = -1;
		HashMap<Item, Integer> itemMap = new HashMap<>();
		Inventory inventory = new BaseInventory();
		while (results.next()) {
			id = results.getInt("ITEMID");
			quantity = results.getInt("QUANTITY");
			Item item = getItem(id);
			inventory.updateMap(item, quantity); // NULL pointer for some reason
		}

		results.close();
		connection.close();
		return inventory;
	}

	public static int getInventoryQuantity(int itemId) throws SQLException {
		Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
		int quantity = DatabaseSelector.getInventoryQuantity(itemId, connection);
		connection.close();
		return quantity;
	}

	public static SalesLog getSales() throws SQLException, DatabaseUpdateException {
		Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
		ResultSet results = DatabaseSelector.getSales(connection);

		int id = -1;
		int userId = -1;
		BigDecimal totalPrice = new BigDecimal("0.00");
		totalPrice.setScale(2, BigDecimal.ROUND_HALF_EVEN);

		SalesLog log = new SalesLogImp();
		while (results.next()) {
			id = results.getInt("ID");
			userId = results.getInt("USERID");
			totalPrice = new BigDecimal(results.getString("TOTALPRICE"));
			Sale newSale = new SalesImp(userId, id);

			log.UpdateLog(newSale);

			// i need to create a sales object and use those to

			// to get a sales, sales log is very similar to inventory, holds a hash table of
			// salesID and
			// users
		}
		results.close();
		connection.close();
		return log;
	}

	public static Sale getSaleById(int saleId) throws SQLException, DatabaseUpdateException {
		Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
		ResultSet results = DatabaseSelector.getSaleById(saleId, connection);

		int id = -1;
		int userId = -1;
		BigDecimal totalPrice = new BigDecimal("0.00");
		totalPrice.setScale(2, BigDecimal.ROUND_HALF_EVEN);

		while (results.next()) {
			id = results.getInt("ID");
			userId = results.getInt("USERID");
			totalPrice = new BigDecimal(results.getString("TOTALPRICE"));

			if (id == saleId) {
				Sale newSale = new SalesImp(userId, id);
				return newSale;
			}
		}
		results.close();
		connection.close();
		return null;
	}

	public static List<Sale> getSalesToUser(int userId) throws SQLException, DatabaseUpdateException {
		Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
		ResultSet results = DatabaseSelectHelper.getSalesToUser(userId, connection);
		List<Sale> sales = new ArrayList<>();

		int id = -1;
		int uId = -1;
		BigDecimal totalPrice = new BigDecimal("0.00");
		totalPrice.setScale(2, BigDecimal.ROUND_HALF_EVEN);

		while (results.next()) {
			id = results.getInt("ID");
			uId = results.getInt("USERID");
			totalPrice = new BigDecimal(results.getString("TOTALPRICE"));

			if (uId == userId) {
				Sale newSale = new SalesImp(uId, id);
				sales.add(newSale);
			}
		}
		results.close();
		connection.close();
		return sales;
	}

	public static Sale getItemizedSaleById(int saleId) throws SQLException, DatabaseUpdateException {
		Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
		ResultSet results = DatabaseSelector.getItemizedSaleById(saleId, connection);
		int saleData = -1;
		int itemData = -1;
		int quantity = -1;
		HashMap<Item, Integer> itemMap = new HashMap<>();
		Sale newSale = DatabaseSelectHelper.getSaleById(saleId);

		while (results.next()) {
			saleData = results.getInt("SALEID");
			itemData = results.getInt("ITEMID");
			quantity = results.getInt("QUANTITY");

			if (saleId == saleData) {
				// create object
				Item item = DatabaseSelectHelper.getItem(itemData);
				itemMap.put(item, quantity);
				return newSale;
			}
		}

		newSale.setItemMap(itemMap);

		results.close();
		connection.close();
		return newSale;
	}

	public static SalesLog getItemizedSales() throws SQLException, DatabaseUpdateException {
		Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
		ResultSet results = DatabaseSelector.getItemizedSales(connection);
		int saleData = -1;
		int itemData = -1;
		int quantity = -1;
		SalesLog log = new SalesLogImp();

		while (results.next()) {
			saleData = results.getInt("SALEID");
			itemData = results.getInt("ITEMID");
			quantity = results.getInt("QUANTITY");

			Sale newSale = DatabaseSelectHelper.getSaleById(saleData);
			Item item = DatabaseSelectHelper.getItem(itemData);
			HashMap<Item, Integer> itemMap = new HashMap<>();
			itemMap.put(item, quantity);
			newSale.setItemMap(itemMap);
			log.UpdateLog(newSale);

		}
		results.close();
		connection.close();
		return null;
	}

	/*
	 * Phase 2 methods
	 */

	public static List<Integer> getUserAccounts(int userId) throws SQLException {
		Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
		ResultSet results = DatabaseSelector.getUserAccounts(userId, connection);
		List<Integer> accountIds = new ArrayList<>();
		int id = -1;

		while (results.next()) {
			id = results.getInt("ID");
			accountIds.add(id);
		}

		results.close();
		connection.close();
		return accountIds;
	}

	public static HashMap<Integer, Integer> getAccountDetails(int accountId) throws SQLException {
		Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
		ResultSet results = DatabaseSelector.getAccountDetails(accountId, connection);
		HashMap<Integer, Integer> itemMap = new HashMap<>();
		int itemId = -1;
		int quantity = -1;
		
		while(results.next()) {
			itemId = results.getInt("ITEMID");
			quantity = results.getInt("QUANTITY");
			
			//Item item = DatabaseSelectHelper.getItem(itemId);
			itemMap.put(itemId, quantity);
		}

		results.close();
		connection.close();
		return itemMap;

	}

}
