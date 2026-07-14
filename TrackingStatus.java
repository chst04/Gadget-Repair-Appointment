package testing6;

import java.util.*;
import java.io.*;

public class TrackingStatus {
	private static Scanner input = new Scanner(System.in);
	public static void TrackingRepairStatus() {
		System.out.println("------Tracking Repair Status------");
		System.out.print("Enter Appointment ID: ");
		int app_ID =  input.nextInt();
		
		String status = findStatus(app_ID);
        
        if (status != null) {
            System.out.println("Appointment ID: " + app_ID);
            System.out.println("Status: " + status);
        } else {
            System.out.println("Appointment not found!");
        }
		
		
	}
	
	//find the appointment ID
	public static String findStatus(int appointmentID) {
        try {
            Scanner fileScanner = new Scanner(new File("appointment.txt"));
            
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                
                if (parts.length > 0 && Integer.parseInt(parts[0]) == appointmentID) {
                    fileScanner.close();
                    return parts.length > 10 ? parts[10] : "Pending";
                }
            }
            fileScanner.close();
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return null;
    }
	
	public static void main(String[] args) {
        TrackingRepairStatus();
    }
}
