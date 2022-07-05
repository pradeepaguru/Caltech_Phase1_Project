package com.Phase1.FileHandlingOperations;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.Phase1.FileHandlingOperations.FileHandlingOperations.HandleOptions;
import com.Phase1.FileHandlingOperations.FileHandlingOperations.MenuOptions;

import java.util.List;
import java.util.Scanner;

public class FileHandlingOperations {

	public static void createMainFolderIfNotPresent(String folderName) {
		File file = new File(folderName);

		// If file doesn't exist, create the main folder
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public static void displayAllFiles(String path) {
		FileHandlingOperations.createMainFolderIfNotPresent("projectfolder");
		// All required files and folders inside "projectfolder" folder relative to current
		// folder
		System.out.println("Displaying all files with directory structure in ascending order\n");

		// listFilesInDirectory displays files along with folder structure
		List<String> filesListNames = FileHandlingOperations.listFilesInDirectory(path, 0, new ArrayList<String>());

		System.out.println("Displaying all files in ascending order\n");
		Collections.sort(filesListNames);

		filesListNames.stream().forEach(System.out::println);
	}

	public static List<String> listFilesInDirectory(String path, int indentationCount, List<String> fileListNames) {
		File dir = new File(path);
		File[] files = dir.listFiles();
		List<File> filesList = Arrays.asList(files);

		Collections.sort(filesList);

		if (files != null && files.length > 0) {
			for (File file : filesList) {

				System.out.print(" ".repeat(indentationCount * 2));

				if (file.isDirectory()) {
					System.out.println("`-- " + file.getName());

					// Recursively indent and display the files
					fileListNames.add(file.getName());
					listFilesInDirectory(file.getAbsolutePath(), indentationCount + 1, fileListNames);
				} else {
					System.out.println("|-- " + file.getName());
					fileListNames.add(file.getName());
				}
			}
		} else {
			System.out.print(" ".repeat(indentationCount * 2));
			System.out.println("|-- Empty Directory");
		}
		System.out.println();
		return fileListNames;
	}

	
	//***************************************** Create a File  Function **********************************************//
	public static void createFile(String fileToAdd, Scanner sc) {
		FileHandlingOperations.createMainFolderIfNotPresent("projectfolder");
		Path pathToFile = Paths.get("./projectfolder/" + fileToAdd);
		try {
			Files.createDirectories(pathToFile.getParent());
			Files.createFile(pathToFile);
			System.out.println(fileToAdd + " created successfully");
		} catch (IOException e) {
			System.out.println("Failed to create file " + fileToAdd);
			System.out.println(e.getClass().getName());
		}
	}

	public static List<String> displayFileLocations(String fileName, String path) {
		List<String> fileListNames = new ArrayList<>();
		FileHandlingOperations.searchFileRecursively(path, fileName, fileListNames);

		if (fileListNames.isEmpty()) {
			System.out.println("\n\n***** Couldn't find any file with given file name \"" + fileName + "\" *****\n\n");
		} else {
			System.out.println("\n\nFound file at below location(s):");

			List<String> files = IntStream.range(0, fileListNames.size())
					.mapToObj(index -> (index + 1) + ": " + fileListNames.get(index)).collect(Collectors.toList());

			files.forEach(System.out::println);
		}

		return fileListNames;
	}
	
	//***************************************** Search File Function **********************************************//

	public static void searchFileRecursively(String path, String fileName, List<String> fileListNames) {
		File dir = new File(path);
		File[] files = dir.listFiles();
		List<File> filesList = Arrays.asList(files);

		if (files != null && files.length > 0) {
			for (File file : filesList) {

				if (file.getName().startsWith(fileName)) {
					fileListNames.add(file.getAbsolutePath());
				}

				// Need to search in directories separately to ensure all files of required
				// fileName are searched
				if (file.isDirectory()) {
					searchFileRecursively(file.getAbsolutePath(), fileName, fileListNames);
				}
			}
		}
	}
	
	
	//***************************************** Delete File Function **********************************************//

	public static void deleteFileRecursively(String path) {

		File currFile = new File(path);
		File[] files = currFile.listFiles();

		if (files != null && files.length > 0) {
			for (File file : files) {

				String fileName = file.getName() + " at " + file.getParent();
				if (file.isDirectory()) {
					deleteFileRecursively(file.getAbsolutePath());
				}

				if (file.delete()) {
					System.out.println(fileName + " deleted successfully");
				} else {
					System.out.println("Failed to delete " + fileName);
				}
			}
		}

		String currFileName = currFile.getName() + " at " + currFile.getParent();
		if (currFile.delete()) {
			System.out.println(currFileName + " deleted successfully");
		} else {
			System.out.println("Failed to delete " + currFileName);
		}
	}
	
	//**********************************  Menu Options Details ****************************************************************************//
	
	public class HandleOptions {
		public static void handleWelcomeScreenInput() {
			boolean running = true;
			Scanner sc = new Scanner(System.in);
			do {
				try {
					MenuOptions.displayMenu();
					int input = sc.nextInt();

					switch (input) {
					case 1:
						FileHandlingOperations.displayAllFiles("projectfolder");
						break;
					case 2:
						HandleOptions.handleFileMenuOptions();
						break;
					case 3:
						System.out.println("Program exited successfully.");
						running = false;
						sc.close();
						System.exit(0);
						break;
					default:
						System.out.println("Please select a valid option from above.");
					}
				} catch (Exception e) {
					System.out.println(e.getClass().getName());
					handleWelcomeScreenInput();
				} 
			} while (running == true);
		}
		
		public static void handleFileMenuOptions() {
			boolean running = true;
			Scanner sc = new Scanner(System.in);
			do {
				try {
					MenuOptions.displayFileMenuOptions();
					FileHandlingOperations.createMainFolderIfNotPresent("projectfolder");
					
					int input = sc.nextInt();
					switch (input) {
					case 1:
						// Add a File 
						System.out.println("Enter the name of the file to be added to the \"projectfolder\" folder");
						String fileToAdd = sc.next();
						
						FileHandlingOperations.createFile(fileToAdd, sc);
						
						break;
					case 2:
						// Delete the file
						System.out.println("Enter the name of the file to be deleted from \"projectfolder\" folder");
						String fileToDelete = sc.next();
						
						FileHandlingOperations.createMainFolderIfNotPresent("projectfolder");
						List<String> filesToDelete = FileHandlingOperations.displayFileLocations(fileToDelete, "projectfolder");
						
							for (String path : filesToDelete) {
								FileHandlingOperations.deleteFileRecursively(path);
							}

						break;
					case 3:
						// Search the file
						System.out.println("Enter the name of the file to be searched from \"projectfolder\" folder");
						String fileName = sc.next();
						
						FileHandlingOperations.createMainFolderIfNotPresent("projectfolder");
						FileHandlingOperations.displayFileLocations(fileName, "projectfolder");

						
						break;
					case 4:
						// Go Back to Main Menu
						return;
					case 5:
						// Exit from the Program
						System.out.println("Program exited successfully.");
						running = false;
						sc.close();
						System.exit(0);
					default:
						System.out.println("Please select a valid option from above.");
					}
				} catch (Exception e) {
					System.out.println(e.getClass().getName());
					handleFileMenuOptions();
				}
			} while (running == true);
		}
	}
	
	/////**************************************** Display the Contents in the Console ***********************************//
	
	public class MenuOptions {

		public static void WelcomeScreen(String appName, String developerName) {
			String companyDetails = String.format("*****************************************************\n"
					+ "** Welcome to %s.com. \n" + "** This application was developed by %s.\n"
					+ "*****************************************************\n", appName, developerName);
			String appFunction = "You can use this application to :-\n"
					+ "Retrieve all file names in asending order present in the \"projectfolder\" folder\n"
					+ "Add, Search, Delete operationts from the\"projectfolder\" folder.\n";
			System.out.println(companyDetails);
			System.out.println(appFunction);
		}

		public static void displayMenu() {
			String menu = "\n\n****** Select any option number from below and press Enter ******\n\n"
					+ "1) Retrieve all files inside \"projectfolder\" folder\n" + "2) Display menu for File operations\n"
					+ "3) Exit the program\n";
			System.out.println(menu);

		}

		public static void displayFileMenuOptions() {
			String fileMenu = "\n\n****** Select any option number from below Menu and press Enter ******\n\n"
					+ "1) Add a file to \"projectfolder\" folder\n" 
					+ "2) Delete a file from \"projectfolder\" folder\n"
					+ "3) Search for a file from \"projectfolder\" folder\n" 
					+ "4) Show Previous Menu\n" + "5) Exit program\n";

			System.out.println(fileMenu);
		}

	}
	
//////****************************** Main Method to perform Operations with display content   *************************************************************//

	public static void main(String[] args) {
		
		// Create "projectfolder" folder if not present in current folder structure
		FileHandlingOperations.createMainFolderIfNotPresent("projectfolder");
		
		MenuOptions.WelcomeScreen("Phase-1 Project - Core Java", "Pradeepa Kasirajan");
		
		HandleOptions.handleWelcomeScreenInput();
	}
}
