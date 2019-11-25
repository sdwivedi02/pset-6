import java.util.Scanner;

public class ATM {

    private Scanner in;
    private BankAccount activeAccount;

    public static final int VIEW = 1;
    public static final int DEPOSIT = 2;
    public static final int WITHDRAW = 3;
    public static final int LOGOUT = 5;
    public static final int LOGOUT = 4;

    public static final int INVALID = 0;
    public static final int INSUFFICIENT = 1;
    public static final int SUCCESS = 2;
    public static final int MAXIMUM = 3;
    public static final int INVALID_AMOUNT = 4;

    public static final int FIRST_NAME_MIN_WIDTH = 1;
    public static final int FIRST_NAME_WIDTH = 20;
    public static final int LAST_NAME_MIN_WIDTH = 1;
    public static final int LAST_NAME_WIDTH = 30;

    public static final int PIN_WIDTH = 4;
    public static final int PIN_MIN = 1000;
    public static final int PIN_MAX = 9999;

    public static final int ACCOUNT_NO_WIDTH = 9;
    public static final long ACCOUNT_NO_MIN = 100000001;
    public static final long ACCOUNT_NO_MAX = 999999999;

    public static final int BALANCE_WIDTH = 15;
    public static final double BALANCE_MIN = 0.00;
    public static final double BALANCE_MAX = 999999999999.99;

    public static final double TRANSFER_MIN = 0.00;

    boolean session = true;
    String accountNoString = "";
    int pin;

    public ATM() {
        this.in = new Scanner(System.in);
        try {
        this.bank = new Bank();
      } catch (IOException e) {
     // cleanup any resources (i.e., the Scanner) and exit
      }
    }

    public void startup() {
        System.out.println("Welcome to the AIT ATM!\n");

        long accountNo;
		    int pin;
		    boolean creatingAccount = true;

        System.out.print("Account No.: ");
        String accountNoString = in.next();
        while (!accountNoString.contentEquals("+") && !isValidAcctNo(100000001, 999999999, accountNoString)) {
			        System.out.print("\nInvalid entry.\n\nAccount No.: ");
			        accountNoString = in.next();
		         }

		    while (creatingAccount) {
			       if (accountNoString.equals("+")) {
				           createAccount();
      			       System.out.print("\nAccount No.: ");
				               while (!accountNoString.contentEquals("+") && !isValidAcctNo(100000001, 999999999, accountNoString)) {
					                       System.out.print("\nInvalid entry.\n\nAccount No.: ");
					                            accountNoString = in.next();
				}
				accountNoString = in.next();
			} else if (!(accountNoString.equals("+")) && isNumeric(accountNoString)) {
				creatingAccount = false;
			}
		}

		System.out.print("Pin: ");
		pin = in.nextInt();
		while (!isValidPin(1000, 9999, pin)) {
			       System.out.print("\nInvalid entry.\n\nPin: ");
			       pin = in.nextInt();
		}

		accountNo = Long.parseLong(accountNoString);

		while (true) {

			if (accountNo == -1 && pin == -1) {
				shutdown();
				return;
			}

			activeAccount = bank.getAccount(accountNo);

		if (isValidLogin(accountNo, pin)) {
				System.out.println("\nHello, again, " + activeAccount.getAccountHolder().getFirstName() + "!\n");

				boolean validLogin = true;
				while (validLogin) {
					switch (getSelection()) {
					case VIEW:
						showBalance();
						bank.save();
						break;
					case DEPOSIT:
						deposit();
						bank.save();
						break;
					case WITHDRAW:
						withdraw();
						bank.save();
						break;
					case TRANSFER:
						transfer();
						bank.save();
						break;
					case LOGOUT:
						validLogin = false;
						startup();
						break;
					default:
						System.out.println("\nInvalid selection.\n");
						break;
					}
				}
			} else {
				    if (accountNo == -1 && pin == -1) {
					System.out.println("h");
					shutdown();
				    } else {
					System.out.println("\nInvalid account number and/or PIN.\n");
					startup();
				}
			}
		}
  }

    public boolean isValidLogin(long accountNo, int pin) {
        return accountNo == activeAccount.getAccountNo() && pin == activeAccount.getPin();
    }

    public int getSelection() {
        System.out.println("[1] View balance");
        System.out.println("[2] Deposit money");
        System.out.println("[3] Withdraw money");
        System.out.println("[4] Transfer money");
        System.out.println("[5] Logout");

        return in.nextInt();
    }

    public void showBalance() {
        System.out.println("\nCurrent balance: " + activeAccount.getBalance() + "\n");
    }

    public void deposit() {
      System.out.print("\nEnter amount: ");
      double amount = in.nextDouble();

      int status = activeAccount.deposit(amount);
      if (status == ATM.INVALID) {
          System.out.println("\nDeposit rejected. Amount must be greater than $0.00.\n");
      } else if (status == ATM.SUCCESS) {
          System.out.println("\nDeposit accepted.\n");
      }
    }

    public void withdraw() {
      System.out.print("\nEnter amount: ");
      double amount = in.nextDouble();

      int status = activeAccount.withdraw(amount);
      if (status == ATM.INVALID) {
          System.out.println("\nWithdrawal rejected. Amount must be greater than $0.00.\n");
      } else if (status == ATM.INSUFFICIENT) {
          System.out.println("\nWithdrawal rejected. Insufficient funds.\n");
      } else if (status == ATM.SUCCESS) {
          System.out.println("\nWithdrawal accepted.\n");
      }
    }

    public void shutdown() {
        if (in != null) {
            in.close();
        }

        System.out.println("\nGoodbye!");
        System.exit(0);
    }

    public static void main(String[] args) {
        ATM atm = new ATM();

        atm.startup();
    }
}
