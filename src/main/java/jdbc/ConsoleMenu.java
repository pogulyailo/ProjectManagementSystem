package jdbc;

import java.util.Scanner;

public class ConsoleMenu {
    private Scanner scanner = new Scanner(System.in);
    private static Realization storage = new Realization();

    public void startMenu() {
        System.out.println("Enter transaction number");
        System.out.println("Create new data - 1");
        System.out.println("Read data from tables - 2");
        System.out.println("Change data in tables - 3");
        System.out.println("Delete data in tables - 4");
        System.out.println("Exit - 0");
        int choice = scanner.nextInt();
        if (choice == 1) createMenu();

        if (choice == 2) readMenu();

        if (choice == 3) updateMenu();

        if (choice == 4) deleteMenu();

        if (choice == 0) exitMenu();
    }
    private void createMenu() {
        System.out.println("Enter transaction number");
        System.out.println("Create new tables - 1");
        System.out.println("Create a new project - 2");
        System.out.println("Create a new developer - 3");
        System.out.println("Create a new customer - 4");
        System.out.println("Exit - 0");
        int choice = scanner.nextInt();
        if (choice == 1) {
            System.out.println("Enter the name of the table");
            scanner.nextLine();
            String tableName = scanner.nextLine();
            System.out.println("Enter all the fields with a pointer, for example: id INT AUTO_INCREMENT PRIMARY KEY, firstName VARCHAR(100) NOT NULL ");
            String sqlColumns = scanner.nextLine();
            storage.createNewTable(tableName, sqlColumns);
        }
        if (choice == 2) {
            System.out.println("Enter the project name");
            scanner.nextLine();
            String prName = scanner.nextLine();
            System.out.println("Enter the project theme");
            String prDesc = scanner.nextLine();
            System.out.println("Enter the cost of the project");
            int prCost = scanner.nextInt();
            storage.addNewProject(prName, prDesc, prCost);
        }
        if (choice == 3) {
            System.out.println("Enter the developer FirstName");
            scanner.nextLine();
            String devName = scanner.nextLine();
            System.out.println("Enter the developer LastName");
            String devSecName = scanner.nextLine();
            System.out.println("Enter developer age");
            int devAge = scanner.nextInt();
            System.out.println("Enter the developer's gender");
            scanner.nextLine();
            String devGend = scanner.nextLine();
            System.out.println("Enter the developer's salary");
            int devSalary = scanner.nextInt();
            storage.addNewDeveloper(devFirstName, devLastName, devAge, devGend, devSalary);
        }
        if (choice == 4) {
            System.out.println("Enter the name of the customer");
            scanner.nextLine();
            String custName = scanner.nextLine();
            System.out.println("State or private customer?");
            System.out.println("State - 1");
            System.out.println("Private - 2");
            int custStOrPr = scanner.nextInt();
            boolean stOrPr = true;
            if (custStOrPr == 1) stOrPr = true;
            if (custStOrPr == 2) stOrPr = false;
            storage.addNewCustomer(custName, stOrPr);
        }
        if (choice == 0) {
            startMenu();
        }
        if (choice != 0) otherOperation();
    }
    private void readMenu() {
        System.out.println("Select the data you want to read");
        System.out.println("Salary of all developers of a separate project - 1");
        System.out.println("Output of all developers of a separate project - 2");
        System.out.println("Output of all Java Developers - 3");
        System.out.println("Output of all middle Developers - 4");
        System.out.println("Output of the list of projects and the number of developers on them - 5");
        System.out.println("Exit this menu - 0");
        int choice = scanner.nextInt();
        if (choice == 1) {
            System.out.println("Output id project");
            storage.getSumOfProjectSalary(scanner.nextInt());
        }
        if (choice == 2) {
            System.out.println("Output id project");
            storage.getProjectDevelopers(scanner.nextInt());
        }
        if (choice == 3) {
            storage.getJavaDevelopers();
        }
        if (choice == 4) {
            storage.getMiddleDevelopers();
        }
        if (choice == 5) {
            System.out.println("The cost of the project - the name of the project - the number of developers");
            storage.getProjectsInfo();
        }
        if (choice == 0) {
            startMenu();
        }
        if (choice != 0)
            otherOperation();
    }
    private void updateMenu() {
        System.out.println("Enter transaction number");
        System.out.println("Update project - 1");
        System.out.println("Update developer - 2");
        System.out.println("Update customer - 3");
        System.out.println("Exit - 0");
        int choice = scanner.nextInt();
        if (choice == 1) {
            System.out.println("Enter Id project");
            int prId = scanner.nextInt();
            System.out.println("Enter a new project name");
            scanner.nextLine();
            String prName = scanner.nextLine();
            System.out.println("Enter a new project subject");
            String prDescr = scanner.nextLine();
            System.out.println("Enter a new project cost");
            int prCost = scanner.nextInt();
            storage.updateProject(prName, prDescr, prCost, prId);
        }
        if (choice == 2) {
            System.out.println("Enter Id developer");
            int devId = scanner.nextInt();
            System.out.println("Enter the new developer FirstName");
            scanner.nextLine();
            String devName = scanner.nextLine();
            System.out.println("Enter the new developer LastName");
            String devSecName = scanner.nextLine();
            System.out.println("Enter a new developer age.");
            int devAge = scanner.nextInt();
            System.out.println("Enter a new developer gender");
            scanner.nextLine();
            String devGend = scanner.nextLine();
            System.out.println("Enter a new developer salary");
            int devSalary = scanner.nextInt();
            storage.updateDeveloper(devFirstName, devLastName, devAge, devGend, devSalary, devId);
        }
        if (choice == 3) {
            System.out.println("Enter Id customer");
            int custId = scanner.nextInt();
            System.out.println("Enter the new customer name");
            scanner.nextLine();
            String custName = scanner.nextLine();
            System.out.println("State or private customer?");
            System.out.println("State - 1");
            System.out.println("Private - 1");
            int custStOrPr = scanner.nextInt();
            boolean stOrPr = true;
            if (custStOrPr == 1) stOrPr = true;
            if (custStOrPr == 2) stOrPr = false;
            storage.updateCustomer(custName, stOrPr, custId);
        }
        if (choice == 0) {
            startMenu();
        }
        if (choice != 0) otherOperation();
    }
    private void deleteMenu() {
        System.out.println("Enter transaction number");
        System.out.println("Delete project - 1");
        System.out.println("Delete developer - 2");
        System.out.println("Delete customer - 3");
        System.out.println("Exit - 0");
        int choice = scanner.nextInt();
        if (choice == 1) {
            System.out.println("Enter Id project");
            int prId = scanner.nextInt();
            storage.deleteProject(prId);
        }
        if (choice == 2) {
            System.out.println("Enter Id developer");
            int devId = scanner.nextInt();
            storage.deleteDeveloper(devId);
        }
        if (choice == 3) {
            System.out.println("Enter Id customer");
            int custId = scanner.nextInt();
            storage.deleteCustomer(custId);
        }
        if (choice == 0) {
            startMenu();
        }
        if (choice != 0) otherOperation();
    }
    private void otherOperation() {
        System.out.println("Select the operation");
        System.out.println("Return to main menu - 1");
        System.out.println("Close the program - 2");
        int choice = scanner.nextInt();
        if (choice == 1 || choice == 0) {
            if (choice == 1) {
                startMenu();
            }
            if (choice == 0) {
                exitMenu();
            }
        } else {
            System.out.println("Enter one of the options");
            otherOperation();
        }
    }
    private void exitMenu() {
        storage.closeConnection();
        System.out.println("Good luck");

    }


}
