import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class OnlineBankingSystem {
    private static Map<Integer, BankAccount> accounts = new HashMap<>();
    private static int nextAccountNumber = 1001;
    private static List<Transaction> transactions = new ArrayList<>();
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        
        System.out.println("\nWelcome to Online Banking System!");
        
        while (!exit) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer Funds");
            System.out.println("5. View Transaction History");
            System.out.println("6. Exit");
            System.out.print("Enter your choice (1-6): ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    createAccount(scanner);
                    break;
                case 2:
                    deposit(scanner);
                    break;
                case 3:
                    withdraw(scanner);
                    break;
                case 4:
                    transferFunds(scanner);
                    break;
                case 5:
                    viewTransactionHistory();
                    break;
                case 6:
                    exit = true;
                    continue;
                default:
                    System.out.println("Invalid choice. Please enter a number from 1 to 6.");
                    continue;
            }
        }
        
        System.out.println("\nThank you for using Online Banking System!");
        scanner.close();
    }
    
    private static void createAccount(Scanner scanner) {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter initial deposit amount: ");
        double initialDeposit = scanner.nextDouble();
        
        int accountNumber = nextAccountNumber++;
        BankAccount newAccount = new BankAccount(accountNumber, name, initialDeposit);
        accounts.put(accountNumber, newAccount);
        
        System.out.println("Account created successfully with account number: " + accountNumber);
    }
    
    private static void deposit(Scanner scanner) {
        System.out.print("Enter account number: ");
        int accountNumber = scanner.nextInt();
        
        if (accounts.containsKey(accountNumber)) {
            System.out.print("Enter deposit amount: ");
            double amount = scanner.nextDouble();
            
            BankAccount account = accounts.get(accountNumber);
            account.deposit(amount);
            transactions.add(new Transaction(accountNumber, "Deposit", amount));
            
            System.out.println("Deposit successful. Current balance: " + account.getBalance());
        } else {
            System.out.println("Account not found.");
        }
    }
    
    private static void withdraw(Scanner scanner) {
        System.out.print("Enter account number: ");
        int accountNumber = scanner.nextInt();
        
        if (accounts.containsKey(accountNumber)) {
            System.out.print("Enter withdrawal amount: ");
            double amount = scanner.nextDouble();
            
            BankAccount account = accounts.get(accountNumber);
            boolean success = account.withdraw(amount);
            
            if (success) {
                transactions.add(new Transaction(accountNumber, "Withdrawal", amount));
                System.out.println("Withdrawal successful. Current balance: " + account.getBalance());
            } else {
                System.out.println("Insufficient funds.");
            }
        } else {
            System.out.println("Account not found.");
        }
    }
    
    private static void transferFunds(Scanner scanner) {
        System.out.print("Enter your account number: ");
        int fromAccountNumber = scanner.nextInt();
        
        if (accounts.containsKey(fromAccountNumber)) {
            System.out.print("Enter recipient's account number: ");
            int toAccountNumber = scanner.nextInt();
            
            if (accounts.containsKey(toAccountNumber)) {
                System.out.print("Enter transfer amount: ");
                double amount = scanner.nextDouble();
                
                BankAccount fromAccount = accounts.get(fromAccountNumber);
                BankAccount toAccount = accounts.get(toAccountNumber);
                
                boolean success = fromAccount.transfer(toAccount, amount);
                
                if (success) {
                    transactions.add(new Transaction(fromAccountNumber, toAccountNumber, "Transfer", amount));
                    System.out.println("Transfer successful.");
                    System.out.println("Your current balance: " + fromAccount.getBalance());
                } else {
                    System.out.println("Transfer failed. Check your account balance.");
                }
            } else {
                System.out.println("Recipient's account not found.");
            }
        } else {
            System.out.println("Your account not found.");
        }
    }
    
    private static void viewTransactionHistory() {
        System.out.println("\nTransaction History:");
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }
}

class BankAccount {
    private int accountNumber;
    private String customerName;
    private double balance;
    
    public BankAccount(int accountNumber, String customerName, double initialDeposit) {
        this.accountNumber = accountNumber;
        this.customerName = customerName;
        this.balance = initialDeposit;
    }
    
    public int getAccountNumber() {
        return accountNumber;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public void deposit(double amount) {
        balance += amount;
    }
    
    public boolean withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            return true;
        } else {
            return false;
        }
    }
    
    public boolean transfer(BankAccount recipient, double amount) {
        if (amount <= balance) {
            withdraw(amount);
            recipient.deposit(amount);
            return true;
        } else {
            return false;
        }
    }
}

class Transaction {
    private int fromAccountNumber;
    private int toAccountNumber;
    private String type;
    private double amount;
    
    public Transaction(int accountNumber, String type, double amount) {
        this.fromAccountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
    }
    
    public Transaction(int fromAccountNumber, int toAccountNumber, String type, double amount) {
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.type = type;
        this.amount = amount;
    }
    
    @Override
    public String toString() {
        if (type.equals("Transfer")) {
            return "From Account " + fromAccountNumber + " to Account " + toAccountNumber + ": " + type + " $" + amount;
        } else {
            return "Account " + fromAccountNumber + ": " + type + " $" + amount;
        }
    }
}
