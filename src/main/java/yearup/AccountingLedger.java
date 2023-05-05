package yearup;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AccountingLedger {
    static Scanner scanner = new Scanner(System.in);

    static ArrayList<Transaction> transactions = loadData();

    public static ArrayList<Transaction> loadData() {
        FileInputStream fileStream = null;
        ArrayList<Transaction> report = new ArrayList<>();
        Scanner fileScanner = null;
        try {
            fileStream = new FileInputStream("transactions.csv");
            fileScanner = new Scanner(fileStream);
            fileScanner.nextLine();
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().toUpperCase();
                String[] columns = line.split("\\|");
                String date = columns[0];
                String time = columns[1];
                String description = columns[2];
                String vendor = columns[3];
                String amount = columns[4];
                Transaction t = new Transaction(date, time, description, vendor, Double.parseDouble(amount));
                report.add(t);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileStream != null && fileScanner != null) {
                    fileStream.close();
                    fileScanner.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return report;
    } }

    public static void main(String[] args) throws Exception{
        while (true) {
            int selection = displayHomeScreen();
            if (selection == 1) {
                addDeposit();
            } else if (selection == 2) {
                makePayment();
            } else if (selection == 3) {
                displayLedgerMenu();
            } else if (selection == 4) {

            } else if (selection == 0) {
                System.out.println("Exiting the application...");
                break;



            } else {
                System.out.println("Invalid option. Please try again.");

            }
        }
    }


    private static int displayHomeScreen() {
        System.out.println("\n"+ColorCodes.ORANGE+"Home"+ ColorCodes.RESET);
        System.out.println(ColorCodes.YELLOW+"---------------------------------------------------------------"+ColorCodes.RESET);
        System.out.println("\n"+ColorCodes.ORANGE_BACKGROUND+"What do you want to do?"+ ColorCodes.RESET);
        System.out.println("1)"+ColorCodes.YELLOW+ "Add Deposit"+ ColorCodes.RESET);
        System.out.println("2)"+ColorCodes.ORANGE+ "Make Payment (Debit)"+ ColorCodes.RESET);
        System.out.println("3)"+ColorCodes.YELLOW+ "Ledger"+ ColorCodes.RESET);
        System.out.println("4)"+ColorCodes.ORANGE+ "Custom Search"+ ColorCodes.RESET);
        System.out.println("0)"+ColorCodes.YELLOW+ "Exit"+ ColorCodes.RESET);
        System.out.print("Enter your option: ");
        int selection = scanner.nextInt();
        scanner.nextLine();
        return selection;
    }

    private static void addDeposit()throws Exception{
        System.out.println("\nAdd Deposit");
        System.out.println("---------------------------------------------------------------");
        System.out.print("Date (yyyy-MM-dd): ");
        String date = scanner.nextLine();
        System.out.print("Time (HH:mm:ss): ");
        String time = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Vendor: ");
        String vendor = scanner.nextLine();
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

            Transaction deposit = new Transaction(date, time, description, vendor, amount);
            FileWriter writer = new FileWriter("transactions.csv",true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(deposit.getDate()+"|"+deposit.getTime()+"|"+deposit.getDescription()+"|"+deposit.getVendor()+"|"+deposit.getAmount()+"\n");
            bufferedWriter.flush();
            bufferedWriter.close();
            transactions.add(deposit);
    }

    private static void makePayment() throws Exception{
        System.out.println("\nMake Payment (Debit)");
        System.out.println("---------------------------------------------------------------");
        System.out.print("Date (yyyy-MM-dd): ");
        String date = scanner.nextLine();
        System.out.print("Time (HH:mm:ss): ");
        String time = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Vendor: ");
        String vendor = scanner.nextLine();
        System.out.print("Amount: ");
        double amount = -Math.abs(scanner.nextDouble());
        scanner.nextLine();
        Transaction payment = new Transaction(date, time, description, vendor, amount);
        FileWriter writer = new FileWriter("transactions.csv",true);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        bufferedWriter.write(payment.getDate()+"|"+payment.getTime()+"|"+payment.getDescription()+"|"+payment.getVendor()+"|"+payment.getAmount()+"\n");
        bufferedWriter.flush();
        bufferedWriter.close();
        transactions.add(payment);
    }

    private static void displayLedgerMenu() {

            System.out.println("\nLedger Menu");
            System.out.println("---------------------------------------------------------------");
            System.out.println("1) All - Display all entries");
            System.out.println("2) Deposits - Display only the entries that are deposits");
            System.out.println("3) Payments - Display only the negative entries (or payments)");
            System.out.println("4) Back - Return to the main menu");
            System.out.print("Enter the selection: ");
            int selection = scanner.nextInt();
            if (selection ==1){
                displayAllEntries();
            }
            else if (selection ==2){
                displayDeposits();

            }
            else if (selection ==3){
                displayPayments();

            }
            else if (selection ==4){
                displayHomeScreen();

            }
    }

    private static void displayPayments() {
        System.out.println("Payments");
        for(Transaction transaction: transactions)
        {
            double payments = transaction.getAmount();
            if (payments < 0) {
                System.out.printf("%-15s %-17s %-28s %-25s $ %.2f \n", transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
            }
        }

    }

    private static void displayDeposits() {
        System.out.println("Deposits");
        for(Transaction transaction: transactions)
        {
            double deposit = transaction.getAmount();
            if (deposit > 0) {
                System.out.printf("%-15s %-17s %-28s %-25s $ %.2f \n", transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
            }
        }
    }

    private static void displayAllEntries(){
        System.out.println("All Entries");
        for(Transaction transaction: transactions)
        {
            System.out.printf("%-15s %-17s %-28s %-25s $ %.2f \n",transaction.getDate(),transaction.getTime(),transaction.getDescription(),transaction.getVendor(),transaction.getAmount());
        }

    }
}