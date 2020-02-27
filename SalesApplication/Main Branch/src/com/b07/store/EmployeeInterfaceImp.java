package com.b07.store;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.DatabaseUpdateException;
import com.b07.inventory.Inventory;
import com.b07.inventory.Item;
import com.b07.users.Employee;
import com.b07.users.User;

public class EmployeeInterfaceImp {

	public static void employeeInterfaceMenu()
			throws IOException, SQLException, DatabaseUpdateException, DatabaseInsertException {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String role, password;
		int userId, roleId, userChoice = 0;
		boolean authenticated = false;
		User currentUser = null;
		boolean valid = false;
		String roleName;

		while (!authenticated) {
			try {
				System.out.print("Enter your employee id: ");
				userId = Integer.parseInt(br.readLine());
				
				currentUser = DatabaseSelectHelper.getUserDetails(userId);
				
				if(currentUser != null) {
					roleId = DatabaseSelectHelper.getUserRole(userId);
					role = DatabaseSelectHelper.getRoleName(roleId);
					
					if(role.contentEquals("EMPLOYEE")) {
						System.out.print("Enter your password: ");
						password = br.readLine();
						System.out.println("");
						authenticated = currentUser.authenticate(password);
						
						if (!authenticated) {
			                System.out.println("Incorrect password please try again");
			              }
					}
					else {
						System.out.println("the id you entered does not belong to an employee");
					}
				}
				else {
					System.out.println("Invalid Id");
				}

			} catch (Exception e) {
				System.out.println("Invalid Input");
				userId = -1;
				password = "";
			}
		}
				
		Inventory inventory = DatabaseSelectHelper.getInventory();
		EmployeeInterface e = new EmployeeInterface((Employee) currentUser, inventory);

		System.out.println("Welcome " + currentUser.getName());

		while (userChoice != 6) {
			System.out.println(e.currEmp() + " is the current user");
			System.out.println("Enter\n1. Authenticate new employee");
			System.out.println("2. Make new user\n3. Make new account");
			System.out.println("4. Make new employee\n5. Restock Inventory");
			System.out.println("6. Exit");

			userChoice = Integer.parseInt(br.readLine());

			switch (userChoice) {
			case 1:
				valid = false;
				while(!valid) {
					try {
						System.out.println("Authenticating new Employee" + "\nEnter employee Id");
						userId = Integer.parseInt(br.readLine());
						User emp = DatabaseSelectHelper.getUserDetails(userId);
						roleId = DatabaseSelectHelper.getUserRole(userId);
						System.out.println("Role Id = is " + roleId);
						roleName = DatabaseSelectHelper.getRoleName(roleId);
						if(roleName.equals("EMPLOYEE")) {
							valid = true;
							emp.manualAuthenticated(true);
							e.setCurrentEmployee((Employee)emp);
							System.out.println(e.currEmp() + " has now been authenticated");
						}
						else
							System.out.println("Must enter an Employee Id");
					}
					catch (Exception w) {
						System.out.println("\nInvalid Input Try again");
						
					}
				}
				break;
			case 2:
				System.out.println("Creating new user");
				createUser(e, "CUSTOMER");
				break;
			case 3: // Same as above
				userId = -1;
				valid = false;
				while(!valid) {
					try {
						System.out.println("Creating new account" + "Enter customer id");
						userId = Integer.parseInt(br.readLine());
						User emp = DatabaseSelectHelper.getUserDetails(userId);
						roleId = DatabaseSelectHelper.getUserRole(userId);
						roleName = DatabaseSelectHelper.getRoleName(roleId);
						if(roleName.equals("CUSTOMER"))
							valid = true;
						else
							System.out.println("Must enter a Customer Id");
					}
					catch (Exception w) {
						System.out.println("\nInvalid Input Try again");

					}
				}
				
				e.createCustomerAccount(userId);
				
				break;
			case 4:
				System.out.println("Creating new employee");
				createUser(e, "EMPLOYEE");
				break;
			case 5:
				System.out.println("Enter id of item to be restocked");
				int itemId = Integer.parseInt(br.readLine());
				Item restockedItem = DatabaseSelectHelper.getItem(itemId);

				System.out.println("Enter quantity to restock with");
				int quantity = Integer.parseInt(br.readLine());

				e.restockInventory(restockedItem, quantity);
				break;
			}
		}
		System.out.println("Exited");
	}

	private static void createUser(EmployeeInterface e, String role)
			throws IOException, DatabaseInsertException, SQLException, DatabaseUpdateException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int age;
		String name, address, password;

		System.out.print("Enter name:");
		name = br.readLine();

		System.out.print("Enter age:");
		age = Integer.parseInt(br.readLine());

		System.out.print("Enter address:");
		address = br.readLine();

		System.out.print("Enter password:");
		password = br.readLine();

		if (role.contentEquals("CUSTOMER")) {
			e.createCustomer(name, age, address, password);
		} else if (role.contentEquals("EMPLOYEE")) {
			e.createEmployee(name, age, address, password);
		}
	}
}
