# Camnewb's JavaFX Application Template
This project was originally developed by Eric Rabil, Andre Roberts, 
and Cameron Newborn. It was broken down by Cameron, myself, to create a
simple and modular template for JavaFX applications.

This template provides simple interfaces for building components, dialogs,
and tables, as well as managing scenes, config files, and background operations

This software is available under the M.I.T liscense.
##Project Structure 
The project package is divided into four sections and a few extra classes.
####API
Folder intended to contain most of the back-end for the application
####COMMANDS
Directory that contains the code to support the command interface
####GUI
Has the code supporting the graphical interface, including the Interface
Manager, Dialog Builder, and Components
####AppMain Class
Contains the main method for launching the Background Worker, Command-line and
Logger, and the Gui
####Config Class
This class supports interaction between an external properties file and the
application, allowing the storing and fetching of user preferences, file paths,
or other variables that should be stored after exiting
####World Class
Provides an interface for tracking or changing the current date
 