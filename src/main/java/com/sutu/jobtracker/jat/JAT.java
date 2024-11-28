package com.sutu.jobtracker.jat;

import model.DatabaseHelper;
import controller.MainController;


public class JAT {

    public static void main(String[] args) {
        // Initialize the database (creates tables if they don't exist)
        DatabaseHelper.initializeDatabase();
        
        // Start the application with the MainController
        MainController mainController = new MainController();
    
    }
}
