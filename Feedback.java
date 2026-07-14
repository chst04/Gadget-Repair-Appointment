package testing6;

import java.io.*;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Feedback {
    private static Scanner input = new Scanner(System.in);
    
    // Original method to provide feedback (keeping it working as before)
    public static void provideFeedback(String username, String name) {
        System.out.println("------Provide Feedback------");
        System.out.println();
        
        System.out.print("Enter Appointment ID: ");
        String appointmentId = input.nextLine();
        
        System.out.print("Enter your rating (1-5): ");
        int rating = input.nextInt();
        
        // Validate rating
        while (rating < 1 || rating > 5) {
            System.out.println("Invalid rating! Please enter a rating between 1 and 5.");
            System.out.print("Enter your rating (1-5): ");
            rating = input.nextInt();
        }
        
        input.nextLine(); // consume newline
        System.out.print("Enter your comment: ");
        String comment = input.nextLine();
        
        // Get current date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateTime = now.format(formatter);
        
        // Write feedback to file
        writeFeedbackToFile(username, name, appointmentId, rating, comment, dateTime);
        
        System.out.println("Thank you for your feedback!");
        System.out.println();
    }
    
    // Method to write feedback to file
    private static void writeFeedbackToFile(String username, String name, String appointmentId, int rating, String comment, String dateTime) {
        try (PrintWriter outputFile = new PrintWriter(new FileWriter("Feedback.txt", true))) {
            outputFile.println(username + ", " + name + ", " + appointmentId + ", " + rating + ", " + comment + ", " + dateTime);
        } catch (IOException exc) {
            System.out.println("Error writing feedback: " + exc.getMessage());
        }
    }
    
    // Method to review user's feedback
    public static void reviewUserFeedback(String username, String name) {
        try {
            File feedbackFile = new File("Feedback.txt");
            
            if (!feedbackFile.exists()) {
                System.out.println("No feedback file found. You haven't provided any feedback yet.");
                System.out.println();
                pressEnterToContinue();
                return;
            }
            
            Scanner fileReader = new Scanner(feedbackFile);
            boolean foundFeedback = false;
            int feedbackCount = 0;
            
            System.out.println("------Your Feedback History------");
            System.out.println("User: " + name);
            System.out.println();
            
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                if (line.trim().isEmpty()) continue; // Skip empty lines
                
                String[] details = line.split(", ");
                
                // Check if this feedback belongs to the current user
                if (details.length >= 6 && (details[0].trim().equals(username) || username.equals("admin"))) {
                    foundFeedback = true;
                    feedbackCount++;
                    
                    System.out.println("Feedback #" + feedbackCount);
                    System.out.println("Appointment ID: " + details[2].trim());
                    System.out.println("Rating: " + details[3].trim() + "/5");
                    System.out.println("Comment: " + details[4].trim());
                    System.out.println("Date: " + details[5].trim());
                    System.out.println("----------------------------------------");
                }
            }
            
            if (!foundFeedback) {
                System.out.println("You haven't provided any feedback yet.");
                System.out.println("Go to 'Provide Feedback' to share your experience!");
            } else {
                System.out.println("Total feedback provided: " + feedbackCount);
            }
            
            fileReader.close();
            System.out.println();
            pressEnterToContinue();
            
        } catch (IOException exc) {
            System.out.println("Error reading feedback file: " + exc.getMessage());
            pressEnterToContinue();
        }
    }
    
    // Method to check if user has already provided feedback for a specific appointment
    public static boolean hasProvidedFeedback(String username, String appointmentId) {
        try {
            File feedbackFile = new File("Feedback.txt");
            
            if (!feedbackFile.exists()) {
                return false;
            }
            
            Scanner fileReader = new Scanner(feedbackFile);
            
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                if (line.trim().isEmpty()) continue;
                
                String[] details = line.split(", ");
                
                if (details.length >= 3 && 
                    details[0].trim().equals(username) && 
                    details[2].trim().equals(appointmentId)) {
                    fileReader.close();
                    return true;
                }
            }
            
            fileReader.close();
            return false;
            
        } catch (IOException exc) {
            System.out.println("Error checking feedback: " + exc.getMessage());
            return false;
        }
    }
    
    // Enhanced method to provide feedback with duplicate checking
    public static void provideFeedbackWithCheck(String username, String name) {
        System.out.println("------Provide Feedback------");
        System.out.println();
        
        System.out.print("Enter Appointment ID: ");
        String appointmentId = input.nextLine();
        
        // Check if feedback already exists
        if (hasProvidedFeedback(username, appointmentId)) {
            System.out.println("You have already provided feedback for Appointment ID: " + appointmentId);
            System.out.print("Do you want to provide feedback for a different appointment? (y/n): ");
            String choice = input.nextLine().toLowerCase();
            
            if (choice.equals("y") || choice.equals("yes")) {
                provideFeedbackWithCheck(username, name); // Recursive call
                return;
            } else {
                System.out.println("Returning to main menu...");
                return;
            }
        }
        
        System.out.print("Enter your rating (1-5): ");
        int rating = input.nextInt();
        
        // Validate rating
        while (rating < 1 || rating > 5) {
            System.out.println("Invalid rating! Please enter a rating between 1 and 5.");
            System.out.print("Enter your rating (1-5): ");
            rating = input.nextInt();
        }
        
        input.nextLine(); // consume newline
        System.out.print("Enter your comment: ");
        String comment = input.nextLine();
        
        // Get current date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateTime = now.format(formatter);
        
        // Write feedback to file
        writeFeedbackToFile(username, name, appointmentId, rating, comment, dateTime);
        
        System.out.println("Thank you for your feedback!");
        System.out.println();
    }
    
    // Utility method for pausing
    private static void pressEnterToContinue() {
        System.out.println("Press Enter to continue...");
        try {
            System.in.read();
        } catch (IOException e) {
            // Handle exception if needed
        }
    }
    
}
