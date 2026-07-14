package testing6;

import java.io.*;
import java.util.*;

public class VerifyAppointment {
	public static void verifyAppointment() {
		GadgetRapirAppointment app = new GadgetRapirAppointment();
		Scanner input = new Scanner(System.in);
	    System.out.println("------Admin Verify Appointment------");
	    System.out.println();
	    
	    try {
	        File appointmentFile = new File("appointment.txt");
	        
	        if (!appointmentFile.exists()) {
	            System.out.println("No appointments found!");
	            app.adminView();
	            return;
	        }
	        
	        // Display pending appointments only
	        displayPendingAppointments();
	        
	        System.out.print("Enter Appointment ID to verify: ");
	        String appointmentID = input.nextLine();
	        
	        // Check if appointment exists and is pending
	        if (!isValidPendingAppointment(appointmentID)) {
	            System.out.println("Invalid appointment ID or appointment already processed!");
	            System.out.print("Try again? (yes/no): ");
	            String retry = input.nextLine();
	            if (retry.equalsIgnoreCase("yes")) {
	                verifyAppointment();
	            } else {
	                app.adminView();
	            }
	            return;
	        }
	        
	        // Show appointment details
	        showAppointmentDetails(appointmentID);
	        
	        System.out.print("Accept this appointment? (yes/no): ");
	        String decision = input.nextLine();
	        
	        while (!decision.equalsIgnoreCase("yes") && !decision.equalsIgnoreCase("no")) {
	            System.out.println("Invalid input! Please enter yes or no.");
	            System.out.print("Accept this appointment? (yes/no): ");
	            decision = input.nextLine();
	        }
	        
	        // Update appointment status
	        String newStatus = decision.equalsIgnoreCase("yes") ? "RPD" : "Denied";
	        updateAppointmentStatus(appointmentID, newStatus);
	        
	        System.out.println("Appointment " + appointmentID + " has been " + newStatus.toLowerCase() + "!");
	        System.out.println();
	        
	        app.adminView();
	        
	    } catch (IOException e) {
	        System.out.println("Error: " + e.getMessage());
	        app.adminView();
	    }
	}

	// Display only pending appointments
	private static void displayPendingAppointments() throws IOException {
	    File file = new File("appointment.txt");
	    Scanner scanner = new Scanner(file);
	    boolean hasPending = false;
	    
	    System.out.println("Pending Appointments:");
	    System.out.printf("%-5s | %-8s | %-20s | %-12s | %-8s%n", "ID", "User", "Device", "Place", "Price");
	    System.out.println("-".repeat(60));
	    
	    while (scanner.hasNextLine()) {
	        String line = scanner.nextLine();
	        String[] parts = line.split(",");
	        
	        if (parts.length >= 11) {
	            String status = parts[10].trim();
	            if (status.equals("Pending")) {
	                String id = parts[0];
	                String user = parts[1];
	                String device = parts[2] + " " + parts[3];
	                String place = parts[8];
	                String price = "RM" + parts[7];
	                
	                System.out.printf("%-5s | %-8s | %-20s | %-12s | %-8s%n", id, user, device, place, price);
	                hasPending = true;
	            }
	        }
	    }
	    
	    if (!hasPending) {
	        System.out.println("No pending appointments to verify.");
	        scanner.close();
	        
			GadgetRapirAppointment.adminView();
	        return;
	    }
	    
	    System.out.println("-".repeat(60));
	    scanner.close();
	}

	// Check if appointment ID is valid and pending
	private static boolean isValidPendingAppointment(String appointmentID) {
	    try {
	        File file = new File("appointment.txt");
	        Scanner scanner = new Scanner(file);
	        
	        while (scanner.hasNextLine()) {
	            String line = scanner.nextLine();
	            String[] parts = line.split(",");
	            
	            if (parts.length >= 11 && parts[0].equals(appointmentID)) {
	                String status = parts[10].trim();
	                scanner.close();
	                return status.equals("Pending");
	            }
	        }
	        scanner.close();
	    } catch (IOException e) {
	        System.out.println("Error checking appointment: " + e.getMessage());
	    }
	    return false;
	}

	// Show detailed appointment information
	private static void showAppointmentDetails(String appointmentID) {
	    try {
	        File file = new File("appointment.txt");
	        Scanner scanner = new Scanner(file);
	        
	        while (scanner.hasNextLine()) {
	            String line = scanner.nextLine();
	            String[] parts = line.split(",");
	            
	            if (parts.length >= 11 && parts[0].equals(appointmentID)) {
	                System.out.println("\n--- Appointment Details ---");
	                System.out.println("ID: " + parts[0]);
	                System.out.println("Customer: " + parts[1]);
	                System.out.println("Device: " + parts[2] + " " + parts[3] + " " + parts[4]);
	                System.out.println("Components: " + parts[5]);
	                System.out.println("Time Slot: " + parts[6]);
	                System.out.println("Price: RM" + parts[7]);
	                System.out.println("Service: " + parts[8]);
	                System.out.println("Address: " + parts[9]);
	                System.out.println("Status: " + parts[10]);
	                System.out.println("-".repeat(30));
	                break;
	            }
	        }
	        scanner.close();
	    } catch (IOException e) {
	        System.out.println("Error reading appointment details: " + e.getMessage());
	    }
	}

	// Update appointment status in appointment.txt
	private static void updateAppointmentStatus(String appointmentID, String newStatus) {
		 try {
		        File inputFile = new File("appointment.txt");
		        List<String> lines = new ArrayList<>();
		        Scanner scanner = new Scanner(inputFile);
		        boolean found = false;
		        
		        // Read all lines
		        while (scanner.hasNextLine()) {
		            String line = scanner.nextLine();
		            String[] parts = line.split(",");
		            
		            if (parts.length >= 11 && parts[0].equals(appointmentID)) {
		                parts[10] = newStatus;
		                line = String.join(",", parts);
		                found = true;
		            }
		            lines.add(line);
		        }
		        scanner.close();
		        
		        if (!found) {
		            System.out.println("Appointment not found!");
		            return;
		        }
		        
		        // Write back to original file
		        PrintWriter writer = new PrintWriter(inputFile);
		        for (String line : lines) {
		            writer.println(line);
		        }
		        writer.close();
		        
		        System.out.println("Status updated to: " + newStatus);
		        
		    } catch (IOException e) {
		        System.out.println("Error: " + e.getMessage());
		    }
	}
}
