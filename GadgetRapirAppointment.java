package testing6;

import java.util.*;
import java.io.*;

public class GadgetRapirAppointment{
	private static Scanner input = new Scanner(System.in);
	private static String[] allUser = new String[50];
	private static int userCount = 0;

	private static String loggedInUsername = "";
    private static String loggedInName = "";
	
	public static void main(String [] args) {
		MainMenu();
	}
	
	//for main menu
	public static void MainMenu()
	{	
		int choice;
		
		System.out.println("------Welcome to Gadget Repair Shop------");
		System.out.println();
		System.out.println("1. Register");
		System.out.println("2. Login");
		System.out.println("3. Exit");
		System.out.print("Enter your choice(1/2/3):");
		choice = input.nextInt();
			
		switch(choice)
		{
		case 1:
			//Register
			Register();
			break;
				
		case 2: 
			//Login
			clearScreen();
			Login();
			break;
			
		case 3:
			System.out.println("Thank you for using our system!");
			break;
				
		default:
			System.out.println("Invalid input");
			clearScreen();
			MainMenu();
		}	
	}
	
	//1.Register
	public static void Register()
	{
		input.nextLine();
		System.out.print("Enter your name: ");
		String name = input.nextLine();
		System.out.print("Enter your phone number (eg: 0123456789): ");
		String phoneNum = input.nextLine();
		
		//validation 
		while(phoneNum.length() < 10 || phoneNum.length() > 11)
		{
			System.out.println("Invalid phone number. Please try again!");
			System.out.println();
			System.out.print("Enter your phone number: ");
			phoneNum = input.nextLine();
		}
		System.out.print("Enter your username: ");
		String username = input.nextLine();
		
		while(isUsernameTaken(username)) {
            System.out.println("Username already exists. Please try again!");
            System.out.println();
            System.out.print("Enter your username: ");
    		username = input.nextLine();
        }
		System.out.print("Enter your password (min 8 character): ");
		String password = input.nextLine();
			
		while (password.length() < 8)
		{
			System.out.println("Invalid password. Please try again!");
			System.out.println();
			System.out.print("Enter your password: ");
			password = input.nextLine();
		}
		User newUser = new User(username, password, name, phoneNum);
		userCount++;
			
		String[] allUser = new String[50];
		for(int i = 1; i < userCount; i++)
		{
			allUser[i] = username;
		}
			
		writeUser(newUser);
		clearScreen();
		System.out.println("You have successfully registered!");
		System.out.println();
		MainMenu();
	}
	
	//validation
	public static boolean isUsernameTaken(String username)
	{
		try 
		{
			File myFile = new File("Users.txt");
			Scanner inputFile = new Scanner(myFile);
			
			String username1;
			
			while(inputFile.hasNext())
			{
				String str = inputFile.nextLine();
				String[] details = str.split(", ");
				
				for(int i = 0; i < 2; i++)
				{
					username1 = details[0];
					
					if(username.equals(username1))
					{
						return true;
					}	
				}
			}
			inputFile.close();
			return false;
		}
		catch(IOException exc)
		{
			System.out.println("File error: " + exc.getMessage());
			return false;
		}
	}
	
	//for new user (register)
	public static void writeUser(User newUser)
	{
		try(PrintWriter outputFile = new PrintWriter(new FileWriter("Users.txt", true)))
		{
			outputFile.println(newUser.getUsername() + ", " + newUser.getPassword() + ", "
			+ newUser.getName() + ", " + newUser.getPhoneNum());
					
			outputFile.close();
		}
		catch(IOException exc)
		{
			System.out.println("File error: " + exc.getMessage());
		}
		
	}
	
	//2.Login
	public static void Login()
	{
		System.out.println("Please log in: ");
		System.out.println("1. User");
		System.out.println("2. Admin");
		System.out.print("Enter your choice (1/2):");
		int choice2 = input.nextInt();
	
		input.nextLine();
		System.out.print("Enter your username: ");
		String username = input.nextLine();
		System.out.print("Enter your password: ");
		String password = input.nextLine();
		
		
		
		switch(choice2)
		{
		case 1:
			String[] loginResult = readUser(username, password);
			
				if(loginResult[0].equals("false")) {
		    	
		        System.out.println("Invalid username or password. Please try again!");
		        System.out.println();
		        Login();
		    }
		    else
		    {
		    	System.out.println("You are successfully logged in!");
		    	System.out.println();
		    	userView();
		    }
		    break;
			
		case 2:
			if(username.equals("admin") && password.equals("admin123"))
			{
				loggedInUsername = "admin";
				loggedInName = "Administrator";
				clearScreen();
				adminView();
			}
			else
			{
				System.out.println("Invalid username or password!");
				System.out.println();
				Login();
			}
			break;
			
		default:
			System.out.println("Invalid input!");
		}
	}
	
	//checking when login
	public static String[] readUser(String username, String password)
	{
		try 
		{
			File myFile = new File("Users.txt");
			Scanner inputFile = new Scanner(myFile);
			
			String username1, password1;
			
			while(inputFile.hasNext())
			{
				String str = inputFile.nextLine();
				String[] details = str.split(", ");
				
				for(int i = 0; i < 2; i++)
				{
					username1 = details[0];
					password1 = details[1];
					
					if(username.equals(username1) && password.equals(password1))
					{
						inputFile.close();
						loggedInUsername = username1;
						loggedInName = details[2];
						return new String[]{"true", loggedInName};
					}	
				}
			}
			inputFile.close();
			return new String[]{"false", ""};
		}
		catch(IOException exc)
		{
			System.out.println("File error: " + exc.getMessage());
			return new String[]{"false", ""};
		}
	}
	
	//UserView
	public static void userView() {
		System.out.println("------Welcome User: "+ loggedInUsername + " ------");
		System.out.println();
		System.out.println("1. Make Appointment");
		System.out.println("2. Make Payment");
		System.out.println("3. Track Status");
		System.out.println("4. View Invoice");
		System.out.println("5. Show Price");
		System.out.println("6. Feedback");
		System.out.println("7. Log out");
		System.out.print("Enter your choice(eg. 1):");
		int choice = input.nextInt();
		
		switch(choice) {
		case 1:
			Appointment apt = new Appointment();
			apt.MakeAppointment(loggedInUsername);
			break;
			
		case 2:
			PaymentHandler.makePaymentMenu(loggedInUsername, loggedInName);
			break;
			
		case 3:
			// Track Status functionality
			TrackingStatus.TrackingRepairStatus();
			
			userView();
			break;
			
		case 4:
			// View Invoice functionality
			PaymentHandler.viewUserInvoices(loggedInUsername, loggedInName);
			break;
			
		case 5:
			readFile();
			userView();
			break;
	        
	        
		case 6:
			// Feedback functionality 
						System.out.println("------Feedback Management------");
				        System.out.println("1. Provide My Feedback");
				        System.out.println("2. Review Feedback");
				        System.out.println("3. Back to Main Menu");
				        System.out.print("Enter your choice: ");
				        
				        int choice1 = input.nextInt();
				        switch(choice1) {
				        case 1:
				        	Feedback.provideFeedback(loggedInUsername, loggedInName);
					        userView();
					        break;
				        case 2:
				        	Feedback.reviewUserFeedback(loggedInUsername, loggedInName);
					        userView();
					        break;
				        case 3:
				        	userView();
				        default:
			                System.out.println("Invalid input. Please try again!");
			                userView();
				        }
			
		case 7:
			System.out.println("Logging out... Thank you!");
			loggedInUsername = "";
			loggedInName = "";
			clearScreen();
			MainMenu();
			break;
			
		default:
			System.out.println("Invalid choice! Please try again.");
			userView();
		}
	}
	
	//admin view
	public static void adminView()
    {
        System.out.println("------Welcome Admin------");
        System.out.println();
        System.out.println("1. Verify Appointment");
        System.out.println("2. Show Price of Parts");
        System.out.println("3. View Feedback");
        System.out.println("4. Update Status");
        System.out.println("5. Track Status");
        System.out.println("0. Log out");
        int choice = input.nextInt();

        switch(choice)
        {
        case 1: //Verify Appointment
            VerifyAppointment.verifyAppointment();
            break;

        case 2: //Show Parts Price
            readFile();
            adminView();
            break;

        case 3: //View Feedback
            Feedback.reviewUserFeedback(loggedInUsername, loggedInName);
            adminView();
            break;

        case 4: //Update Status
            updateAppointmentStatus();
            adminView();
            break;
        case 5: //Update Status
            TrackingStatus.TrackingRepairStatus();
            adminView();
            break;


        case 0: //Log out
            System.out.println("You have successfully logged out!");
            System.out.println();
            clearScreen();
            MainMenu();
            break;
        }
    }
	
	public static void readFile()
	{
		try
		{
			String answer;
			do
			{
				File myFile = new File("Price.txt");
				Scanner inputFile = new Scanner(myFile);
				
				System.out.println();
				System.out.println("Please choose the model:");
				System.out.println("IOS");
				System.out.println("Android");
				System.out.println("Windows");
				input.nextLine();//clear buffer
				String choice = input.nextLine();
				
				
				String choice2;
				
				if(!choice.equalsIgnoreCase("IOS") && !choice.equalsIgnoreCase("Android") && !choice.equalsIgnoreCase("Windows"))
				{
					System.out.println("Invalid input. Please try again!");
					System.out.println();
					readFile();
				}
				
				if(choice.equalsIgnoreCase("IOS"))
				{
					System.out.println();
					System.out.println("Please choose the type:");
					System.out.println("Phone");
					System.out.println("Tablet");
					System.out.println("Laptop");
					choice2 = input.nextLine();
	
				}
				
				else if(choice.equalsIgnoreCase("Android"))
				{
					System.out.println();
					System.out.println("Please choose the type:");
					System.out.println("Phone");
					System.out.println("Tablet");
					choice2 = input.nextLine();
		
				}
				
				else
				{
					choice2 = "Laptop";
				}
				
				while(!choice2.equalsIgnoreCase("Phone") && !choice2.equalsIgnoreCase("Tablet") && !choice2.equalsIgnoreCase("Laptop"))
				{
					System.out.println("Invalid input. Please try again!");
					System.out.println();
					
					if(choice.equalsIgnoreCase("IOS"))
					{
						System.out.println();
						System.out.println("Please choose the type:");
						System.out.println("Phone");
						System.out.println("Tablet");
						System.out.println("Laptop");
						choice2 = input.nextLine();
		
					}
					
					else if(choice.equalsIgnoreCase("Android"))
					{
						System.out.println();
						System.out.println("Please choose the type:");
						System.out.println("Phone");
						System.out.println("Tablet");
						choice2 = input.nextLine();
		
					}
					
					else
					{
						choice2 = "Laptop";
					}
				}
				
				String choice3;
				
				if(choice.equalsIgnoreCase("IOS"))
				{
					choice3 = "Apple";
				}
				
				else if(choice.equalsIgnoreCase("Android"))
				{
					System.out.println();
					System.out.println("Please choose the brand:");
					System.out.println("Samsung");
					System.out.println("Huawei");
					choice3 = input.nextLine();
	
				}
				
				else
				{
					System.out.println();
					System.out.println("Please choose the brand:");
					System.out.println("Acer");
					System.out.println("Huawei");
					choice3 = input.nextLine();
	
				}
				while(!choice3.equalsIgnoreCase("Apple") && !choice3.equalsIgnoreCase("Samsung") && !choice3.equalsIgnoreCase("Huawei") && !choice3.equalsIgnoreCase("Acer"))
				{
					System.out.println("Invalid input. Please try again!");
					if(choice.equalsIgnoreCase("IOS"))
					{
						choice3 = "Apple";
					}
					
					else if(choice.equalsIgnoreCase("Android"))
					{
						System.out.println();
						System.out.println("Please choose the brand:");
						System.out.println("Samsung");
						System.out.println("Huawei");
						choice3 = input.nextLine();

					}
					
					else
					{
						System.out.println();
						System.out.println("Please choose the brand:");
						System.out.println("Acer");
						System.out.println("Huawei");
						choice3 = input.nextLine();

					}
				}
				
				clearScreen();
				System.out.printf("%-15s %10s%n", "Parts", "Price(RM)");
				
				while(inputFile.hasNext())
				{
					String str = inputFile.nextLine();
					String[] details = str.split(", ");
					
					for(int i = 0; i < 1; i++)
					{
						if(choice.equalsIgnoreCase(details[0]) && choice2.equalsIgnoreCase(details[1]) && choice3.equalsIgnoreCase(details[2]))
						{
							System.out.printf("%-15s %10s%n", details[3], details[4]);
						}	
					}
				}
				inputFile.close();
				System.out.println();
				System.out.print("Show more (y/n)?");
				answer = input.nextLine();
				answer = answer.toUpperCase();
			}while(answer.equals("Y"));
			clearScreen();
			
		}
		catch(IOException exc)
		{
			System.out.println("File error: " + exc.getMessage());
		}
		
	}
	
	public static void clearScreen() {
		for (int i = 0; i < 50; i++) {
	            System.out.println();
	        }
    }
	
	public static void updateAppointmentStatus() {
        System.out.println("\n=== UPDATE APPOINTMENT STATUS ===");

        System.out.print("Enter Appointment ID to update: ");
        int appointmentID = input.nextInt();
        input.nextLine(); // consume newline

        System.out.println("\nAvailable Status Options:");
        System.out.println("1. Pending");
        System.out.println("2. RPD (Ready Pay Deposit)");
        System.out.println("3. Deposited");
        System.out.println("4. In Progress");
        System.out.println("5. Fixed");
        System.out.println("6. Completed");
        System.out.print("Enter new status (1-6): ");

        int statusChoice = input.nextInt();
        input.nextLine(); // consume newline

        String newStatus = "";
        switch(statusChoice) {
            case 1: newStatus = "Pending"; break;
            case 2: newStatus = "RPD"; break;
            case 3: newStatus = "Deposited"; break;
            case 4: newStatus = "In Progress"; break;
            case 5: newStatus = "Fixed"; break;
            case 6: newStatus = "Completed"; break;
            default:
                System.out.println("Invalid choice!");
                updateAppointmentStatus();
                return;
        }

        if (updateStatus(appointmentID, newStatus)) {
            System.out.println("✅ Status updated successfully!");
            System.out.println("Appointment ID: " + appointmentID + " -> Status: " + newStatus);
        } else {
            System.out.println("❌ Failed to update status. Appointment not found!");
        }

        System.out.println("\nPress Enter to continue...");
        input.nextLine();
    }
private static boolean updateStatus(int appointmentID, String newStatus) {
        try {
            File file = new File("appointment.txt");
            if (!file.exists()) {
                return false;
            }

            List<String> lines = new ArrayList<>();
            Scanner scanner = new Scanner(file);
            boolean found = false;

            // Read all lines and update the specific appointment
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length > 0 && Integer.parseInt(parts[0]) == appointmentID) {
                    // Update the status (assuming status is at index 10)
                    if (parts.length > 10) {
                        parts[10] = newStatus;
                    } else {
                        // Extend array if needed
                        String[] newParts = new String[11];
                        System.arraycopy(parts, 0, newParts, 0, parts.length);
                        newParts[10] = newStatus;
                        parts = newParts;
                    }
                    line = String.join(",", parts);
                    found = true;
                }
                lines.add(line);
            }
            scanner.close();

            if (!found) {
                return false;
            }

            // Write back to file
            PrintWriter writer = new PrintWriter(file);
            for (String line : lines) {
                writer.println(line);
            }
            writer.close();

            return true;

        } catch (Exception e) {
            System.out.println("Error updating status: " + e.getMessage());
            return false;
        }
    }
}

