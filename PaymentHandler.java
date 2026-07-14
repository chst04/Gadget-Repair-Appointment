package testing6;

import java.util.*;
import java.io.*;

public class PaymentHandler {
    private static Scanner input = new Scanner(System.in);
    
    // Payment menu for users
    public static void makePaymentMenu(String loggedInUsername, String loggedInName) {
        System.out.println("\n=== PAYMENT MENU ===");
        System.out.println("1. Make Deposit Payment (10%)");
        System.out.println("2. Make Final Payment (90%)");
        System.out.println("3. Back to Main Menu");
        System.out.print("Enter your choice (1/2/3): ");
        int choice = input.nextInt();
        
        switch(choice) {
        case 1:
            processUserPayment("deposit", loggedInUsername, loggedInName);
            break;
            
        case 2:
            processUserPayment("final", loggedInUsername, loggedInName);
            break;
            
        case 3:
            GadgetRapirAppointment.userView();
            break;
            
        default:
            System.out.println("Invalid choice! Please try again.");
            makePaymentMenu(loggedInUsername, loggedInName);
        }
    }
    
    // Process payment for the logged-in user
    public static void processUserPayment(String paymentType, String loggedInUsername, String loggedInName) {
        // First, display user's appointments
        displayUserAppointments(loggedInUsername);
        
        System.out.println("RPD - Ready Pay Deposit");
        System.out.print("Enter Appointment ID to make payment: ");
        int appointmentID = input.nextInt();
        
        // Verify the appointment belongs to the logged-in user
        if (verifyUserAppointment(appointmentID, loggedInUsername)) {
            // Check payment eligibility
            if (checkPaymentEligibility(appointmentID, paymentType)) {
                Payment.processPayment(appointmentID, paymentType);
            } else {
                System.out.println("❌ Payment not eligible for this appointment!");
                System.out.println("Please check your appointment status.");
                if (paymentType.equals("deposit")) {
                    System.out.println("Deposit can only be made for 'RPD' appointments.");
                } else {
                    System.out.println("Final payment can only be made for'Fixed' appointments.");
                }
            }
        } else {
            System.out.println("❌ Appointment not found or doesn't belong to you!");
        }
        
        System.out.println("\nPress Enter to continue...");
        input.nextLine(); // consume newline
        input.nextLine(); // wait for user input
        GadgetRapirAppointment.userView();
    }
    
    // Display appointments for the logged-in user
    public static void displayUserAppointments(String loggedInUsername) {
        System.out.println("\n=== YOUR APPOINTMENTS ===");
        try {
            File file = new File("appointment.txt");
            if (!file.exists()) {
                System.out.println("No appointments found!");
                return;
            }
            
            Scanner scanner = new Scanner(file);
            boolean hasAppointments = false;
            
            System.out.printf("%-5s | %-20s | %-12s | %-8s%n", "ID", "Device", "Status", "Price");
            System.out.println("-".repeat(55));
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                
                if (parts.length > 1 && parts[1].equals(loggedInUsername)) {
                    hasAppointments = true;
                    String appointmentID = parts[0];
                    String device = parts[2] + " " + parts[3] + " " + parts[4]; // model + brand + type
                    String status = parts.length > 10 ? parts[10] : "Pending";
                    String price = "RM" + parts[7];
                    
                    System.out.printf("%-5s | %-20s | %-12s | %-8s%n", appointmentID, device, status, price);
                }
            }
            
            if (!hasAppointments) {
                System.out.println("No appointments found for your account!");
            }
            
            scanner.close();
            System.out.println("-".repeat(55));
            
        } catch (FileNotFoundException e) {
            System.out.println("Error reading appointments: " + e.getMessage());
        }
    }
    
    // Verify that the appointment belongs to the logged-in user
    public static boolean verifyUserAppointment(int appointmentID, String username) {
        try {
            File file = new File("appointment.txt");
            if (!file.exists()) {
                return false;
            }
            
            Scanner scanner = new Scanner(file);
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                
                if (parts.length > 1 && 
                    Integer.parseInt(parts[0]) == appointmentID && 
                    parts[1].equals(username)) {
                    scanner.close();
                    return true;
                }
            }
            scanner.close();
        } catch (FileNotFoundException | NumberFormatException e) {
            System.out.println("Error verifying appointment: " + e.getMessage());
        }
        return false;
    }
    
    // Check if payment is eligible based on appointment status
    public static boolean checkPaymentEligibility(int appointmentID, String paymentType) {
        try {
            File file = new File("appointment.txt");
            if (!file.exists()) {
                return false;
            }
            
            Scanner scanner = new Scanner(file);
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                
                if (parts.length > 0 && Integer.parseInt(parts[0]) == appointmentID) {
                    String status = parts.length > 10 ? parts[10] : "Pending";
                    scanner.close();
                    
                    // Check payment eligibility based on current status and payment type
                    if (paymentType.equals("deposit")) {
                        // Can make deposit if status is "Pending"
                        return status.equals("RPD");
                    } else if (paymentType.equals("final")) {

                    	 // ← Only RPD or Deposited, NOT Pending  
                    	return status.equals("Fixed"); 
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException | NumberFormatException e) {
            System.out.println("Error checking payment eligibility: " + e.getMessage());
        }
        return false;
    }
    
    // Method to get appointment status (useful for other parts of the system)
    public static String getAppointmentStatus(int appointmentID) {
        try {
            File file = new File("appointment.txt");
            if (!file.exists()) {
                return "Not Found";
            }
            
            Scanner scanner = new Scanner(file);
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                
                if (parts.length > 0 && Integer.parseInt(parts[0]) == appointmentID) {
                    String status = parts.length > 10 ? parts[10] : "Pending";
                    scanner.close();
                    return status;
                }
            }
            scanner.close();
        } catch (FileNotFoundException | NumberFormatException e) {
            System.out.println("Error getting appointment status: " + e.getMessage());
        }
        return "Error";
    }
    
    // View invoices for the logged-in user
    public static void viewUserInvoices(String loggedInUsername, String loggedInName) {
        System.out.println("\n=== YOUR INVOICES ===");
        try {
            File file = new File("payments.txt");
            if (!file.exists()) {
                System.out.println("No payment records found!");
                System.out.println("\nPress Enter to continue...");
                input.nextLine();
                input.nextLine();
                GadgetRapirAppointment.userView();
                return;
            }
            
            Scanner scanner = new Scanner(file);
            boolean hasInvoices = false;
            List<String[]> userPayments = new ArrayList<>();
            
            // Collect all payments for the user
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                
                if (parts.length >= 9 && parts[1].equals(loggedInUsername)) {
                    userPayments.add(parts);
                    hasInvoices = true;
                }
            }
            scanner.close();
            
            if (!hasInvoices) {
                System.out.println("No invoices found for your account!");
                System.out.println("\nPress Enter to continue...");
                input.nextLine();
                input.nextLine();
                GadgetRapirAppointment.userView();
                return;
            }
            
            // Display invoice summary
            System.out.printf("%-12s | %-5s | %-8s | %-10s | %-12s | %-15s%n", 
                "Invoice No.", "App ID", "Type", "Amount", "Status", "Date");
            System.out.println("-".repeat(75));
            
            for (String[] payment : userPayments) {
                String invoiceNum = payment[8];
                String appointmentID = payment[0];
                String paymentType = payment[2];
                String amount = "RM" + payment[3];
                String status = payment[5];
                String date = payment[6];
                
                System.out.printf("%-12s | %-5s | %-8s | %-10s | %-12s | %-15s%n",
                    invoiceNum, appointmentID, paymentType, amount, status, date);
            }
            
            System.out.println("-".repeat(75));
            System.out.println("1. View Detailed Invoice");
            System.out.println("2. Back to Main Menu");
            System.out.print("Enter your choice (1/2): ");
            
            int choice = input.nextInt();
            input.nextLine(); // consume newline
            
            if (choice == 1) {
                System.out.print("Enter Invoice Number to view details: ");
                String invoiceNum = input.nextLine();
                viewDetailedInvoice(invoiceNum, loggedInUsername);
            } else {
                GadgetRapirAppointment.userView();
            }
            
        } catch (FileNotFoundException e) {
            System.out.println("Error reading payment records: " + e.getMessage());
            System.out.println("\nPress Enter to continue...");
            input.nextLine();
            input.nextLine();
            GadgetRapirAppointment.userView();
        }
    }
    
    // View detailed invoice
    public static void viewDetailedInvoice(String invoiceNumber, String loggedInUsername) {
        try {
            File paymentsFile = new File("payments.txt");
            if (!paymentsFile.exists()) {
                System.out.println("Payment records not found!");
                return;
            }
            
            Scanner scanner = new Scanner(paymentsFile);
            String[] paymentRecord = null;
            
            // Find the specific payment record
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                
                if (parts.length >= 9 && parts[8].equals(invoiceNumber) && parts[1].equals(loggedInUsername)) {
                    paymentRecord = parts;
                    break;
                }
            }
            scanner.close();
            
            if (paymentRecord == null) {
                System.out.println("Invoice not found or doesn't belong to you!");
                System.out.println("\nPress Enter to continue...");
                input.nextLine();
                viewUserInvoices(loggedInUsername, "");
                return;
            }
            
            // Get appointment details
            String appointmentID = paymentRecord[0];
            RepairDetails appointment = getAppointmentDetails(Integer.parseInt(appointmentID));
            
            // Display detailed invoice
            System.out.println("\n" + "=".repeat(60));
            System.out.println("                    DETAILED INVOICE");
            System.out.println("=".repeat(60));
            System.out.println("Invoice Number: " + paymentRecord[8]);
            System.out.println("Date: " + paymentRecord[6]);
            System.out.println("Payment Type: " + paymentRecord[2]);
            System.out.println("-".repeat(60));
            System.out.println("Customer: " + loggedInUsername);
            System.out.println("Appointment ID: " + appointmentID);
            
            if (appointment != null) {
                System.out.println("Device: " + appointment.getModel() + " " + appointment.getBrand() + " " + appointment.getType());
                System.out.println("Components: " + String.join(", ", appointment.getComponent()));
                System.out.println("Repair Place: " + appointment.getPlace());
                System.out.println("Time Slot: " + appointment.getTimeSlot());
            }
            
            System.out.println("-".repeat(60));
            System.out.println("Payment Method: " + paymentRecord[4]);
            System.out.println("Amount Paid: RM" + paymentRecord[3]);
            System.out.println("Total Repair Cost: RM" + paymentRecord[7]);
            System.out.println("Payment Status: " + paymentRecord[5]);
            System.out.println("-".repeat(60));
            
            if (paymentRecord[2].equals("DEPOSIT")) {
                double totalCost = Double.parseDouble(paymentRecord[7]);
                double remaining = totalCost * 0.90;
                System.out.println("Remaining Balance: RM" + String.format("%.2f", remaining));
            }
            
            System.out.println("=".repeat(60));
            System.out.println("Thank you for choosing GadgetRepair!");
            System.out.println("=".repeat(60));
            
            System.out.println("\nPress Enter to continue...");
            input.nextLine();
            viewUserInvoices(loggedInUsername, "");
            
        } catch (FileNotFoundException | NumberFormatException e) {
            System.out.println("Error viewing invoice: " + e.getMessage());
            System.out.println("\nPress Enter to continue...");
            input.nextLine();
            viewUserInvoices(loggedInUsername, "");
        }
    }
    
    // Helper method to get appointment details
    private static RepairDetails getAppointmentDetails(int appointmentID) {
        try {
            File file = new File("appointment.txt");
            if (!file.exists()) {
                return null;
            }
            
            Scanner scanner = new Scanner(file);
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                
                if (parts.length > 0 && Integer.parseInt(parts[0]) == appointmentID) {
                    String userID = parts[1];
                    String model = parts[2];
                    String brand = parts[3];
                    String type = parts[4];
                    List<String> components = Arrays.asList(parts[5].split("\\|"));
                    int timeSlot = Integer.parseInt(parts[6]);
                    double price = Double.parseDouble(parts[7]);
                    String place = parts[8];
                    String address = parts[9];
                    
                    RepairDetails repair = new RepairDetails(appointmentID, userID, model, brand, type, components, timeSlot, price, place,address);
                    if (parts.length > 10) {
                        repair.setStatus(parts[10]);
                    }
                    scanner.close();
                    return repair;
                }
            }
            scanner.close();
        } catch (FileNotFoundException | NumberFormatException e) {
            System.out.println("Error reading appointment details: " + e.getMessage());
        }
        return null;
    }
}