package testing6;

import java.util.*;
import java.io.*;


public class Appointment {
	private static int appointmentCounter = 101; // Start from 101
	private static Scanner input = new Scanner(System.in);
	private static Map<String, Double> priceMap = new HashMap<>();
	String userID;
	
	static {
		loadPrices();
		loadLastAppointmentID(); // Load the last used appointment ID
	}
	
	// Method to get available components for a specific device combination
	private static List<String> getAvailableComponents(String model, String type, String brand) {
		List<String> availableComponents = new ArrayList<>();
		
		for (Map.Entry<String, Double> entry : priceMap.entrySet()) {
			String[] keyParts = entry.getKey().split(",");
			if (keyParts.length >= 4 && 
				keyParts[0].equals(model) && 
				keyParts[1].equals(type) && 
				keyParts[2].equals(brand)) {
				
				String component = keyParts[3];
				if (!availableComponents.contains(component)) {
					availableComponents.add(component);
				}
			}
		}
		
		return availableComponents;
	}
	
	// Method to load the last appointment ID from file
	private static void loadLastAppointmentID() {
		try {
			File file = new File("appointment.txt");
			if (!file.exists()) {
				return; // If file doesn't exist, keep default counter (101)
			}
			
			Scanner scanner = new Scanner(file);
			int maxID = 100; // Start from 100 so next will be 101
			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] parts = line.split(",");
				
				// Assuming the first part is the appointment ID
				if (parts.length > 0) {
					try {
						int id = Integer.parseInt(parts[0].trim());
						if (id > maxID) {
							maxID = id;
						}
					} catch (NumberFormatException e) {
						// Skip invalid lines
					}
				}
			}
			scanner.close();
			appointmentCounter = maxID + 1; // Set counter to next available ID
		} catch (FileNotFoundException e) {
			// File doesn't exist, keep default counter
		}
	}
	
	// Method to get next appointment ID
	private static synchronized int getNextAppointmentID() {
		int currentID = appointmentCounter;
		appointmentCounter++;
		System.out.println("Generating appointment ID: " + currentID);
		return currentID;
	}
	
	private static void loadPrices() {
		try {
			File file = new File("Price.txt");
			Scanner scanner = new Scanner(file);
			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] parts = line.split(", ");
				
				if (parts.length >= 5) {
					String key = parts[0] + "," + parts[1] + "," + parts[2] + "," + parts[3];
					double price = Double.parseDouble(parts[4]);
					priceMap.put(key, price);
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("Price file not found: " + e.getMessage());
		}
	}
	
	private static double calculatePrice(String model, String type, String brand, List<String> components) {
		double totalPrice = 0.0;
		
		for (String component : components) {
			String key = model + "," + type + "," + brand + "," + component;
			Double price = priceMap.get(key);
			
			if (price != null) {
				totalPrice += price;
			} else {
				System.out.println("Warning: No price found for " + component + " in " + model + " " + type + " " + brand);
			}
		}
		
		return totalPrice;
	}
	
	private static void displayPriceList(String model, String type, String brand) {
		System.out.println("\n--- Price List for " + brand + " " + model + " " + type + " ---");
		
		for (Map.Entry<String, Double> entry : priceMap.entrySet()) {
			String[] keyParts = entry.getKey().split(",");
			if (keyParts.length >= 4 && 
				keyParts[0].equals(model) && 
				keyParts[1].equals(type) && 
				keyParts[2].equals(brand)) {
				
				System.out.println(keyParts[3] + ": RM" + entry.getValue());
			}
		}
		System.out.println("-----------------------------");
	}

	public void MakeAppointment(String userName) {
		this.userID = userName;
		boolean valid = true;
		String model= null;
		String brand = null;
		String repairType = null;
		
		while(valid) {	
			
			boolean validModel = false;
			while(!validModel) {
				System.out.println("---------");
				System.out.println("Model:");
	            System.out.println("IOS");
	            System.out.println("Android");
	            System.out.println("Windows");  
				System.out.println("---------");
	            System.out.print("Enter Phone Model: ");
	            model = input.nextLine();
	            
	            switch(model.toLowerCase()){
	            case "ios":
                    model = "IOS";
                    validModel = true;
                    System.out.println();
                    System.out.println("-------------");
                    System.out.println("Brand: Apple");
                    System.out.println("-------------");
                    brand = "Apple";
                    boolean validType = false;

                    while(!validType) {
                    	System.out.println();
                    	System.out.println("-------------");
                        System.out.println("Repair type:\nPhone\nTablet\nLaptop");
                        System.out.println("-------------");
                        System.out.print("Enter your type:");
                        repairType = input.nextLine();

                        if(repairType.equalsIgnoreCase("Phone")) {
                            repairType = "Phone";
                            validType = true;
                        }
                        else if(repairType.equalsIgnoreCase("Tablet")) {
                            repairType = "Tablet";
                            validType = true;
                        }
                        else if(repairType.equalsIgnoreCase("Laptop")) {
                            repairType = "Laptop";
                            validType = true;
                        }
                        else {
                            System.out.println("Invalid Input");
                        }
                    }
                    break;
	            		
	            	case "android":
	            		model = "Android";
	            		validModel = true;
	            		boolean validBrand = false;
	            		while(!validBrand){
	            			System.out.println();
	            			System.out.println("-------------");
		            		System.out.println("Brand:\nSamsung\nHuawei");
		            		System.out.println("-------------");
		            		System.out.print("Enter your brand:");
		            		brand = input.nextLine();
		            		if(brand.equalsIgnoreCase("Samsung")) {
		            			brand = "Samsung";
		            			validBrand = true;
		            			System.out.println();
		            			System.out.println("-------------");
		            			System.out.println("Repair type:\nPhone\nTablet");
		            			System.out.println("-------------");
			            		System.out.print("Enter your type:");
			            		repairType = input.nextLine();
			            		
			            		boolean validSamsungType = false;
			            		while (!validSamsungType) {
				            		if(repairType.equalsIgnoreCase("Phone")) {
				            			repairType = "Phone";
				            			validSamsungType = true;
				            		}
				            		else if(repairType.equalsIgnoreCase("Tablet")) {
				            			repairType = "Tablet";
				            			validSamsungType = true;
				            		}
				            		else {
				            			System.out.println("Invalid input.");
				            		}
				            	}
		            		}
		            		else if (brand.equalsIgnoreCase("Huawei")) {
		            			brand = "Huawei";
		            			validBrand = true;
		            			System.out.println();
		            			System.out.println("-------------");
		            			System.out.println("Repair type:\nPhone\nTablet");
		            			System.out.println("-------------");
			            		System.out.print("Enter your type:");
			            		repairType = input.nextLine();
			            		
			            		boolean validHuaweiType = false;
			            		while(!validHuaweiType) {
				            		if(repairType.equalsIgnoreCase("Phone")) {
				            			repairType = "Phone";
				            			validHuaweiType = true;
				            		}
				            		else if(repairType.equalsIgnoreCase("Tablet")) {
				            			repairType = "Tablet";
				            			validHuaweiType = true;
				            		}
				            		else {
				            			System.out.println("Invalid input.");
				            		}
			            		}
		            		}
			            	else {
			            		System.out.println("Invalid input.");
			            	}
		            	}
	            		break;
	            		
	            	case "windows":  
	            		model = "Windows";
	            		System.out.println();
	            		System.out.println("-------------");
	            		System.out.println("Brand:\nAcer\nHuawei");
	            		System.out.println("-------------");
	            		System.out.print("Enter your brand:");
	            		brand = input.nextLine();
	            		
	            		boolean validbrand = false;
	            		validModel = true;
	            		while(!validbrand) {
	            			if(brand.equalsIgnoreCase("Acer")) {
	            				brand = "Acer";
	            				System.out.println();
	            				System.out.println("--------------------");
		  	            		System.out.println("Repair Type: Laptop");
		  	            		System.out.println("--------------------");
	            				repairType = "Laptop";
	            				validbrand = true;
	            			}
	            			else if (brand.equalsIgnoreCase("Huawei")) {	
	            				brand = "Huawei";
	            				System.out.println();
	            				System.out.println("--------------------");
	            				System.out.println("Repair Type: Laptop");
	            				System.out.println("--------------------");
	            				repairType = "Laptop";
	            				validbrand = true;
	            			}
		            		else {
		            			System.out.println("Invalid input.");
		            		}
		            	}
	            		break;
	            	
	            	default:
	            		System.out.println("Invalid input.");
	            		break;
	            }
			}
			
			// Display price list for the selected device
			displayPriceList(model, repairType, brand);
            
            // Component selection - Updated to match price list                  
            List<String> components = new ArrayList<>();
            boolean vaildComponents = false;
            
            while (!vaildComponents) {
                System.out.println("Select components to repair (separate by commas):");
                
                // Show different components based on device type
                if (repairType.equals("Laptop")) {
                    System.out.println("1. Screen");
                    System.out.println("2. Battery");
                    System.out.println("3. Fan");  // For laptops
                    System.out.println("4. Charging Port");
                    System.out.println("5. Motherboard");
                    } 
                else {
                    System.out.println("1. Screen");
                    System.out.println("2. Battery");
                    System.out.println("3. Lens");  
                    System.out.println("4. Charging Port");
                    System.out.println("5. Motherboard");
                }
                System.out.print("Your choices (e.g. 1,3,5): ");

                String compInput = input.nextLine().trim();
                String[] compChoices = compInput.split(",");
                components.clear();
                boolean allValid = true;

                for (String c : compChoices) {
                    switch (c.trim()) {
                        case "1": components.add("Screen"); break;
                        case "2": components.add("Battery"); break;
                        case "3": 
                            if (repairType.equals("Laptop")) {
                                components.add("Fan");
                            } else {
                                components.add("Lens");
                            }
                            break;
                        case "4": components.add("Charging Port"); break;
                        case "5": 
                            if (repairType.equals("Laptop")) {
                                components.add("Motherboard");
                            } else {
                                components.add("Motherboard");
                            }
                            break;
                        default:
                            allValid = false;
                            break;
                    }
                }

                if (components.isEmpty() || !allValid) {
                    System.out.println("Invalid option(s). Please try again.\n");
                } else {
                	vaildComponents = true;
                }
            }
            
            // Calculate and display price
            double totalPrice = calculatePrice(model, repairType, brand, components);
            System.out.println("Total estimated price: RM" + totalPrice);
            
            // Ask for confirmation
            System.out.print("Confirm appointment? (yes/no): ");
            String confirm = input.nextLine();

            if (!confirm.equalsIgnoreCase("yes")) {
                System.out.println("Appointment cancelled.");
                valid = false;
                return;
            }
            
         // Repair place selection
            String place = "";
            String address = null;
            while (true) {
                System.out.println("Choose Repair Place:");
                System.out.println("1. Walk-in Store Repair");
                System.out.println("2. Home Services");
                System.out.print("Enter choice (1 or 2): ");
                int placeChoice = input.nextInt();
                input.nextLine(); // consume newline
                if (placeChoice == 1) {
                    place = "Walk-in Store Repair";
                    address = "Store";
                    break;
                } else if (placeChoice == 2) {
                    place = "Home Serives";
                    String building = "";
                    while (true) {
                        System.out.print("Enter building (eg.Taman rose): ");
                        building = input.nextLine().trim();
                        if (!building.isEmpty()&& building.toLowerCase().startsWith("taman")) {
                            break;
                        } else {
                            System.out.println("Building cannot be empty. Please try again.");
                        }
                    }
                    
                    String unit = "";
                    while (true) {
                        System.out.print("Enter unit number: ");
                        unit = input.nextLine().trim();
                        if (!unit.isEmpty()) {
                            break;
                        } else {
                            System.out.println("Unit number cannot be empty. Please try again.");
                        }
                    }
                    
                    address = building + " " + unit;          
                    break;
                } else {
                    System.out.println("Invalid choice, please enter 1 or 2.");
                }
            }

            int timeslot = 0;
            boolean validTime = false;

            while (!validTime) {
                System.out.println("Available Time Slots:");
                System.out.println("1. 9:00 AM");
                System.out.println("2. 10:00 AM");
                System.out.println("3. 11:00 AM"); 
                System.out.println("4. 12:00 PM");
                System.out.println("5. 1:00 PM");
                System.out.println("6. 2:00 PM");
                System.out.println("7. 3:00 PM");
                System.out.println("8. 4:00 PM");
                System.out.println("9. 5:00 PM");
                System.out.print("Choose time slot (1-9): ");
                
                timeslot = input.nextInt();
                input.nextLine();
                
                if (timeslot >= 1 && timeslot <= 9) {
                    validTime = true;
                    System.out.println("Time slot selected!");
                } else {
                    System.out.println("Invalid choice. Please enter 1-9.");
                }
            }

            // Generate appointment ID
            int appointmentID = getNextAppointmentID();
            
            RepairDetails repair_d = new RepairDetails(appointmentID, userID, model, brand, repairType, components, timeslot, totalPrice, place,address);
            repair_d.saveToFile();
            System.out.println("Appointment confirmed!");
            System.out.println("Appointment ID: " + appointmentID);
            System.out.println("Status: " + repair_d.getStatus());
            System.out.println("Total price: RM" + totalPrice);
			GadgetRapirAppointment.userView();
            valid=false;
		}
	}
}
