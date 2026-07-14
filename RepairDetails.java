package testing6;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;

public class RepairDetails {
	private int appointmentID; 
	private String userID;
	private String model;
	private String brand;
	private String type; // phone, tablet, laptop
	private List<String> Component; // screen, battery, etc.
	private int timeSlot;
	private double price;
	private String place; // Walk-in or Send-over
	private String status; // Pending, Approved, Cancelled
	private String address;

	// Constructor - updated to match the Appointment class call
	public RepairDetails(int appointmentID, String userID, String model, String brand, String type, List<String> Component, 
			int timeSlot, double price, String place, String address) {
		this.appointmentID = appointmentID;
		this.userID = userID;
		this.model = model;
		this.brand = brand;
		this.type = type;
		this.Component = Component;
		this.timeSlot = timeSlot;
		this.price = price;
		this.place = place;
		this.status = "Pending";
		this.address = address;
	}

	// Getters and Setters
	public int getAppointmentID() {
		return appointmentID;
	}
	public void setAppointmentID(int appointmentID) {
		this.appointmentID = appointmentID;
	}

	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	
	public String getBrand() {
		return brand; 
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	public String getType() {
		return type; 
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public List<String> getComponent() {
    	return Component;
    }
	public void setComponent(List<String> component) {
		this.Component = component;
	}
	
	public int getTimeSlot() {
		return timeSlot;
	}
	public void setTimeSlot(int timeSlot) {
		this.timeSlot = timeSlot;
	}
	
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}

	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}

	public String getStatus() {
    	return (status != null) ? status : "Pending"; // Returns "Pending" if null
	}

	public void setStatus(String status) {
		this.status = (status != null) ? status : "Pending"; // Sets "Pending" if null
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
        // Handle empty address input in setter as well
        this.address = (address == null || address.trim().isEmpty()) ? "Null" : address.trim();
    }

	// Updated toString method to include status
	@Override
	public String toString() {
		return appointmentID + "," + userID + "," + model + "," + brand + "," + type + "," +
			String.join("|", Component) + "," + timeSlot + "," + price + "," + place + "," + address +","+ status;
	}

    // Save to file (append mode) - changed filename to match what Appointment class expects
    public void saveToFile() {
        try (FileWriter fw = new FileWriter("appointment.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            out.println(this.toString());
            System.out.println("✅ Appointment saved!");

        } catch (IOException e) {
            System.out.println("⚠ Error: " + e.getMessage());
        }
    }
    
    //load the data from file
    public List <String[]> loadFromFile() {
    	List<String[]> allDetails = new ArrayList<>();

    	try {
    		File myFile = new File ("Appointment.txt");
    		Scanner input = new Scanner(myFile);
    		
            // Load file into memory once
            while (input.hasNextLine()) {
                String w = input.nextLine();
                if (w.trim().isEmpty() || w.startsWith("#")) continue; // skip empty or comments
                String[] details = w.split(", ");
                for(int i = 0; i <details.length; i++) {
                	details [i] = details[i].trim();
                	}
                allDetails.add(details);
            }
            input.close();
    		
    		
    	}catch (IOException e) {
            System.out.println("⚠ Error reading appointments: " + e.getMessage());
    	}
		return allDetails;
    }
}
    

