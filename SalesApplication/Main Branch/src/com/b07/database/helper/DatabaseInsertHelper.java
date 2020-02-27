package com.b07.database.helper;

import com.b07.database.DatabaseInserter;
import com.b07.database.helper.DatabaseDriverHelper;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.inventory.Item;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DatabaseInsertHelper extends DatabaseInserter {

	// Note invalid role returns will be Labelled as -1
	public static int insertRole(String name) throws DatabaseInsertException, SQLException {
		// TODO Implement this method as stated on the assignment sheet
		// hint: You should be using these three lines in your final code
		String nameUpper = name.toUpperCase();

		if (!nameUpper.equals("ADMIN") && !nameUpper.equals("CUSTOMER") && !nameUpper.equals("EMPLOYEE")) {
			System.out.print("Invalid Role, Please try Admin, Employee or Customer");
			throw new DatabaseInsertException();
		}

		Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
		int roleId = DatabaseInserter.insertRole(name, connection);
		connection.close();

		return roleId;
	}

	public static int insertNewUser(String name, int age, String address, String password)
			throws DatabaseInsertException, SQLException {
		// TODO Implement this method as stated on the assignment sheet
		// hint: You should be using these three lines in your final code
		int userId = -1;

		if (address.length() > 100) {
			System.out.print("Adress length Exceeded 100 character limit");
			throw new DatabaseInsertException();
		}

		if (age < 0) {
			System.out.print("Age is not valid");
			throw new DatabaseInsertException();
		}

		Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
		userId = DatabaseInserter.insertNewUser(name, age, address, password, connection);
		connection.close();

		return userId;
	}

	public static int insertUserRole(int userId, int roleId) throws SQLException, DatabaseInsertException {
		// TODO Implement this method as stated on the assignment sheet
		// hint: You should be using these three lines in your final code

		if (IsRoleValid(roleId) == false) {
			System.out.print("Invalid RoleId");
			throw new DatabaseInsertException();
		}

		// (Additions)Check if user Id is valid

		Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
		int userRoleId = DatabaseInserter.insertUserRole(userId, roleId, connection);
		connection.close();
		return userRoleId;
	}

	public static int insertItem(String name, BigDecimal price) throws DatabaseInsertException, SQLException {
		// TODO Implement this method as stated on the assignment sheet
		// hint: You should be using these three lines in your final code
		BigDecimal zero = new BigDecimal(0.00);
		int nameSize = name.length();

		if (price.compareTo(zero) == -1) {
			System.out.println("Price must be >= zero");
			throw new DatabaseInsertException();
		}

		if (nameSize >= 64 || name == null) {
			System.out.println("Name must not be Null, and less than 64 characters");
			throw new DatabaseInsertException();
		}

		// check if price's scale is 2

		Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
		int itemId = DatabaseInserter.insertItem(name, price, connection);
		connection.close();
		return itemId;
	}

	public static int insertInventory(int itemId, int quantity) throws DatabaseInsertException, SQLException {
		// TODO Implement this method as stated on the assignment sheet
		// hint: You should be using these three lines in your final code
		List<Item> itemList = DatabaseSelectHelper.getAllItems();
		boolean found = false;

		for (int i = 0; i < itemList.size(); i++) {
			int currentId = itemList.get(i).getId();
			if (currentId == itemId) {
				found = true;
				break;
			}
		}

		if (quantity < 0) {
			System.out.println("Quanity must be >= zero");
			throw new DatabaseInsertException();
		} else if (!found) {
			System.out.println("Invalid ItemId");
			throw new DatabaseInsertException();
		}

		Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
		int inventoryId = DatabaseInserter.insertInventory(itemId, quantity, connection);
		connection.close();
		return inventoryId;
	}

	public static int insertSale(int userId, BigDecimal totalPrice) throws DatabaseInsertException, SQLException {
		// TODO Implement this method as stated on the assignment sheet
		// hint: You should be using these three lines in your final code
		BigDecimal zero = BigDecimal.valueOf(0.0);
		if (totalPrice.compareTo(zero) == -1) {
			System.out.println("Price must be >= zero");
			throw new DatabaseInsertException();
		}
		// check scale is

		Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
		int saleId = DatabaseInserter.insertSale(userId, totalPrice, connection);
		connection.close();
		return saleId;
	}

	public static int insertItemizedSale(int saleId, int itemId, int quantity)
			throws DatabaseInsertException, SQLException {
		// TODO Implement this method as stated on the assignment sheet
		// hint: You should be using these three lines in your final code
		if (quantity < 0) {
			System.out.println("Quanity must be >= zero");
			throw new DatabaseInsertException();
		}

		Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
		int itemizedId = DatabaseInserter.insertItemizedSale(saleId, itemId, quantity, connection);
		connection.close();
		return itemizedId;
	}

	private static boolean IsRoleValid(int roleId) throws SQLException {
		List<Integer> idList = DatabaseSelectHelper.getRoleIds();
		return idList.contains(roleId);

	}

	/*
	 * PHASE 2 METHODS
	 */

	public static int insertAccount(int userId) throws DatabaseInsertException, SQLException {
		// NEED TO CHECK IF USERID IS VALID
		Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
		int accountId = DatabaseInserter.insertAccount(userId, connection);
		connection.close();
		return accountId;

	}

	public static int insertAccountLine(int accountId, int itemId, int quantity)
			throws DatabaseInsertException, SQLException {
		
		//CHECK ACCOUNTID
		/*List<Item> itemList = DatabaseSelectHelper.getAllItems();
		boolean found = false;

		for (int i = 0; i < itemList.size(); i++) {
			int currentId = itemList.get(i).getId();
			if (currentId == itemId) {
				found = true;
				break;
			}
		}

		if (quantity < 0) {
			System.out.println("Quanity must be >= zero");
			throw new DatabaseInsertException();
		} else if (!found) {
			System.out.println("Invalid ItemId");
			throw new DatabaseInsertException();
		}*/

		Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
		int recordId = DatabaseInserter.insertAccountLine(accountId, itemId, quantity, connection);
		connection.close();
		return recordId;
	}

}
