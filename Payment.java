package testing6;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class Payment {
    private static Scanner input = new Scanner(System.in);
    private int appointmentID;
    private String userID;
    private double totalPrice;
    private double depositAmount;
    private double remainingAmount;
    private String paymentMethod;
    private String paymentStatus;
    private Date paymentDate;
    private String invoiceNumber;
    private static int invoiceCounter = 1001;

    // Constructor
    public Payment(int appointmentID, String userID, double totalPrice) {
        this.appointmentID = appointmentID;
        this.userID = userID;
        this.totalPrice = totalPrice;
        this.depositAmount = totalPrice * 0.10; // 10% deposit
        this.remainingAmount = totalPrice * 0.90; // 90% remaining
        this.paymentDate = new Date();
        this.invoiceNumber = "INV" + (invoiceCounter++);
        loadLastInvoiceNumber(); // Load last invoice number from file
    }

    // Load last invoice number to maintain sequence
    private void loadLastInvoiceNumber() {
        try {
            File file = new File("payments.txt");
            if (!file.exists()) {
                return;
            }
            
            Scanner scanner = new Scanner(file);
            int maxInvoice = 1000;
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                
                if (parts.length >= 9) {
                    String invNum = parts[8].trim().replace("INV", "");
                    try {
                        int num = Integer.parseInt(invNum);
                        if (num > maxInvoice) {
                            maxInvoice = num;
                        }
                    } catch (NumberFormatException e) {
                        // Skip invalid lines
                    }
                }
            }
            scanner.close();
            invoiceCounter = maxInvoice + 1;
            this.invoiceNumber = "INV" + invoiceCounter;
        } catch (FileNotFoundException e) {
            // File doesn't exist, keep default
        }
    }

    // Method to process deposit payment
    public boolean processDepositPayment() {
        System.out.println("\n=== DEPOSIT PAYMENT ===");
        System.out.println("Total Repair Cost: RM" + String.format("%.2f", totalPrice));
        System.out.println("Deposit Required (10%): RM" + String.format("%.2f", depositAmount));
        System.out.println("Remaining Amount: RM" + String.format("%.2f", remainingAmount));
        
        if (selectPaymentMethod()) {
            this.paymentStatus = "Deposited";
            savePaymentRecord("DEPOSIT");
            printInvoice("DEPOSIT");
            updateAppointmentStatus("Deposited");
            return true;
        }
        return false;
    }

    // Method to process final payment
    public boolean processFinalPayment() {
        System.out.println("\n=== FINAL PAYMENT ===");
        System.out.println("Remaining Amount to Pay: RM" + String.format("%.2f", remainingAmount));
        
        if (selectPaymentMethod()) {
            this.paymentStatus = "Fully Paid";
            savePaymentRecord("FINAL");
            printInvoice("FINAL");
            updateAppointmentStatus("Fully Paid");
            return true;
        }
        return false;
    }

    // Method to select payment method
    private boolean selectPaymentMethod() {
        boolean validChoice = false;
        
        while (!validChoice) {
            System.out.println("\n--- Payment Methods ---");
            System.out.println("1. Cash");
            System.out.println("2. E-Wallet");
            System.out.println("3. Bank Transfer");
            System.out.print("Select payment method (1-3): ");
            
            int choice = input.nextInt();
            input.nextLine(); // consume newline
            
            switch (choice) {
                case 1:
                    return processCashPayment();
                case 2:
                    return processEWalletPayment();
                case 3:
                    return processBankTransfer();
                default:
                    System.out.println("Invalid choice. Please select 1, 2, or 3.");
            }
        }
        return false;
    }

    // Cash payment processing
    private boolean processCashPayment() {
        this.paymentMethod = "Cash";
        System.out.println("\n--- Cash Payment ---");
        System.out.println("Please prepare the exact amount.");
        System.out.print("Confirm cash payment? (yes/no): ");
        String confirm = input.nextLine();
        
        if (confirm.equalsIgnoreCase("yes")) {
            System.out.println("✅ Cash payment accepted!");
            return true;
        } else {
            System.out.println("❌ Payment cancelled.");
            return false;
        }
    }

    // E-Wallet payment processing
    private boolean processEWalletPayment() {
        this.paymentMethod = "E-Wallet";
        System.out.println("\n--- E-Wallet Payment ---");
        System.out.println("Supported E-Wallets: GrabPay, Touch 'n Go, Boost, ShopeePay");
        System.out.print("Enter your e-wallet type: ");
        String walletType = input.nextLine();
        
        System.out.print("Enter your e-wallet phone number: ");
        String phoneNumber = input.nextLine();
        
        System.out.println("Please complete the payment on your e-wallet app.");
        System.out.print("Payment completed? (yes/no): ");
        String confirm = input.nextLine();
        
        if (confirm.equalsIgnoreCase("yes")) {
            this.paymentMethod = "E-Wallet (" + walletType + ")";
            System.out.println("✅ E-Wallet payment successful!");
            return true;
        } else {
            System.out.println("❌ Payment cancelled.");
            return false;
        }
    }

    // Bank transfer processing
    private boolean processBankTransfer() {
        this.paymentMethod = "Bank Transfer";
        System.out.println("\n--- Bank Transfer Payment ---");
        System.out.println("Transfer Details:");
        System.out.println("Bank: Maybank");
        System.out.println("Account Number: 1234567890");
        System.out.println("Account Name: GadgetRepair Sdn Bhd");
        
        System.out.print("Enter your reference number: ");
        String referenceNumber = input.nextLine();
        
        System.out.print("Payment completed? (yes/no): ");
        String confirm = input.nextLine();
        
        if (confirm.equalsIgnoreCase("yes")) {
            this.paymentMethod = "Bank Transfer (Ref: " + referenceNumber + ")";
            System.out.println("✅ Bank transfer payment successful!");
            return true;
        } else {
            System.out.println("❌ Payment cancelled.");
            return false;
        }
    }

    // Print invoice
    private void printInvoice(String paymentType) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("                PAYMENT INVOICE");
        System.out.println("=".repeat(50));
        System.out.println("Invoice Number: " + invoiceNumber);
        System.out.println("Date: " + sdf.format(paymentDate));
        System.out.println("Payment Type: " + paymentType);
        System.out.println("-".repeat(50));
        System.out.println("Appointment ID: " + appointmentID);
        System.out.println("Customer ID: " + userID);
        System.out.println("Payment Method: " + paymentMethod);
        System.out.println("-".repeat(50));
        
        if (paymentType.equals("DEPOSIT")) {
            System.out.println("Total Repair Cost: RM" + String.format("%.2f", totalPrice));
            System.out.println("Deposit Paid (10%): RM" + String.format("%.2f", depositAmount));
            System.out.println("Remaining Balance: RM" + String.format("%.2f", remainingAmount));
        } else {
            System.out.println("Final Payment: RM" + String.format("%.2f", remainingAmount));
            System.out.println("Total Paid: RM" + String.format("%.2f", totalPrice));
        }
        
        System.out.println("-".repeat(50));
        System.out.println("Status: " + paymentStatus);
        System.out.println("=".repeat(50));
        System.out.println("Thank you for choosing GadgetRepair!");
        System.out.println("=".repeat(50));
    }

    // Save payment record to file
    private void savePaymentRecord(String paymentType) {
        try (FileWriter fw = new FileWriter("payments.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            double paidAmount = paymentType.equals("DEPOSIT") ? depositAmount : remainingAmount;
            
            String record = appointmentID + "," + userID + "," + paymentType + "," + 
                          String.format("%.2f", paidAmount) + "," + paymentMethod + "," + 
                          paymentStatus + "," + sdf.format(paymentDate) + "," + 
                          String.format("%.2f", totalPrice) + "," + invoiceNumber;
                          
            out.println(record);
            System.out.println("💾 Payment record saved!");

        } catch (IOException e) {
            System.out.println("⚠ Error saving payment record: " + e.getMessage());
        }
    }

    // Update appointment status in appointment.txt
    private void updateAppointmentStatus(String newStatus) {
        try {
            File inputFile = new File("appointment.txt");
            File tempFile = new File("appointment_temp.txt");
            
            if (!inputFile.exists()) {
                System.out.println("⚠ Appointment file not found!");
                return;
            }

            Scanner scanner = new Scanner(inputFile);
            PrintWriter writer = new PrintWriter(tempFile);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                
                // Check if this is the appointment we want to update
                if (parts.length > 0 && Integer.parseInt(parts[0]) == appointmentID) {
                    // Update the status (last field)
                    if (parts.length >= 10) {
                        parts[10] = newStatus; // Status is the 10th field (index 9)
                    } else {
                        // If status field doesn't exist, add it
                        line += "," + newStatus;
                        writer.println(line);
                        continue;
                    }
                    line = String.join(",", parts);
                }
                writer.println(line);
            }

            scanner.close();
            writer.close();

            // Replace original file with updated file
            if (inputFile.delete()) {
                tempFile.renameTo(inputFile);
                System.out.println("📝 Appointment status updated to: " + newStatus);
            } else {
                System.out.println("⚠ Error updating appointment file!");
                tempFile.delete();
            }

        } catch (IOException | NumberFormatException e) {
            System.out.println("⚠ Error updating appointment status: " + e.getMessage());
        }
    }

    // Static method to process payment for an appointment
    public static void processPayment(int appointmentID, String paymentType) {
        // Find appointment details
        RepairDetails appointment = findAppointment(appointmentID);
        if (appointment == null) {
            System.out.println("❌ Appointment not found!");
            return;
        }

        Payment payment = new Payment(appointmentID, appointment.getUserID(), appointment.getPrice());
        
        if (paymentType.equalsIgnoreCase("deposit")) {
            payment.processDepositPayment();
        } else if (paymentType.equalsIgnoreCase("final")) {
            payment.processFinalPayment();
        } else {
            System.out.println("❌ Invalid payment type! Use 'deposit' or 'final'.");
        }
    }

    // Helper method to find appointment by ID
    private static RepairDetails findAppointment(int appointmentID) {
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
                    // Create RepairDetails object from file data
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
            System.out.println("⚠ Error reading appointment file: " + e.getMessage());
        }
        return null;
    }

    // Getters
    public int getAppointmentID() { return appointmentID; }
    public String getUserID() { return userID; }
    public double getTotalPrice() { return totalPrice; }
    public double getDepositAmount() { return depositAmount; }
    public double getRemainingAmount() { return remainingAmount; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getPaymentStatus() { return paymentStatus; }
    public String getInvoiceNumber() { return invoiceNumber; }
}