
import java.util.Scanner;

public class PlaneManagement {
    static Ticket[] tickets = new Ticket[60];
    static int ticketCount = 0;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        char[][] planeSeat = createSeat();
        printMenu();
        handleUserInput(validateUserInput(input), input, planeSeat);
    }

    private static int validateUserInput(Scanner input) {
        try {
            int userInput = input.nextInt();
            input.nextLine(); // Consume newline character
            if (userInput >= 0 && userInput <= 6) {
                // Handle user input here
                return userInput;
            } else {
                System.out.println("Please enter a valid number (0 - 6)");
                printMenu();
                return validateUserInput(input);
            }
        } catch (Exception e) {
            System.out.println("Please enter a valid number (0 - 6)");
            input.nextLine(); // Consume invalid input
            printMenu();
            validateUserInput(input);
        }
        return 0;
    }

    private static void printMenu() {
        System.out.println("\nWelcome to the Plane Management application\n");
        System.out.println("*****************************************************************");
        System.out.println("*\t\t\t\tMENU\t\t\t\t*");
        System.out.println("*****************************************************************\n");
        System.out.println("""
                1) Buy a seat.
                2) Cancel a seat.
                3) Find first available seat.
                4) Show seating plan.
                5) Print ticket information and total sale.
                6) Search ticket.
                0) Quit
                """);
        System.out.println("*****************************************************************");
        System.out.print("Please Select An Option : ");
    }

    private static void handleUserInput(int userInput, Scanner input, char[][] planeSeat) {

        switch (userInput) {
            case 1:

                buySeat(input, planeSeat);
                break;

            case 2:

                cancelSeat(input, planeSeat);
                break;

            case 3:
                findFirstAvailable(planeSeat, input);
                break;

            case 4:
                showSeatingPlane(planeSeat, input);
                break;
            case 5:
                print_tickets_info(planeSeat, input);
                break;
            case 6:
                search_Ticket(input, tickets, planeSeat);
                break;

            case 0:
                System.out.println("Thank you for using Plane Management");
                break;

            default:
                printMenu();
                userInput = validateUserInput(input);
                handleUserInput(userInput, input, planeSeat);
                break;
        }

    }

    private static void search_Ticket(Scanner input, Ticket[] tickets, char[][] planeSeat) {
        System.out.print("\nEnter desired seat Index to find ticket (A1 - A2) :");
        String seatIndex = input.nextLine();

        try {
            int column = Integer.parseInt(seatIndex.substring(1));
            int row = Character.toUpperCase(seatIndex.charAt(0)) - 65;

            if (row < 0 || row >= planeSeat.length || column < 0 || column >= planeSeat[0].length) {
                System.out.println("Please enter a valid seat index.\n");
                search_Ticket(input, tickets, planeSeat);
                return;
            }

            if (planeSeat[row][column] == '1') {
                planeSeat[row][column] = '0';
                int ticketIndex = -1;
                for (int i = 0; i < ticketCount; i++) {
                    if (tickets[i].getRow() == row + 1 && tickets[i].getSeat() == column) {
                        ticketIndex = i;
                        break;
                    }
                }
                if (ticketIndex != -1) {
                    tickets[ticketIndex].printTicketInfo();
                } else {
                    System.out.println("Ticket not found");
                }
                printMenu();
                handleUserInput(validateUserInput(input), input, planeSeat);

            } else {
                System.out.println("Seat Index " + seatIndex + " is Already available .\n");
                printMenu();
                handleUserInput(validateUserInput(input),input,planeSeat);
            }


        } catch (NumberFormatException e) {
            System.out.println("\nEnter valid seat number in the given Format\n");
            search_Ticket(input, tickets, planeSeat);
        }


    }

    private static void print_tickets_info(char[][] planeSeat, Scanner input) {
        double totalSale = 0;
        for (int i = 0; i < ticketCount; i++) {
            tickets[i].printTicketInfo();
            totalSale += tickets[i].getPrice();
        }
        if (totalSale > 0) {
            System.out.println("Total sale: £" + totalSale);
        } else {
            System.out.println("No tickets sold yet.");
        }

        printMenu();
        handleUserInput(validateUserInput(input), input, planeSeat);
    }

    private static char[][] createSeat() {
        char[][] planeSeat = new char[4][15];

        for (int i = 0; i < planeSeat.length; i++) {
            for (int j = 0; j < planeSeat[i].length; j++) {
                if (j == 0) {
                    planeSeat[i][j] = (char) ('A' + i);
                } else if (i == 1 || i == 2) {
                    for (int k = 1; k <= 12; k++)
                        planeSeat[i][k] = (char) '0';

                } else {
                    planeSeat[i][j] = (char) '0';
                }
            }
        }
        return planeSeat;
    }

    private static void showSeatingPlane(char[][] planeSeat, Scanner input) {

        // to print the number of columns

        for (int i = 0; i < planeSeat[1].length; i++) {
            if (i == 0) {
                System.out.print("   ");
            } else {
                System.out.printf("%-3d", i);
            }

        }
        System.out.println();

        for (char[] row : planeSeat) {
            for (char column : row) {
                if (column == '0') {
                    System.out.printf("%-3c", 'O');
                } else if (column == '1') {
                    System.out.printf("%-3c", 'X');
                } else {
                    System.out.printf("%-3c", column);
                }

            }
            System.out.println();
        }
        printMenu();
        handleUserInput(validateUserInput(input), input, planeSeat);

    }

    private static void buySeat(Scanner input, char[][] planeSeat) {
        System.out.print("\nEnter desired seat Index (A1 - A2) :");
        String seatIndex = input.nextLine();

        try {
            int column = Integer.parseInt(seatIndex.substring(1));
            int row = Character.toUpperCase(seatIndex.charAt(0)) - 65;

            if (row < 0 || row >= planeSeat.length || column < 0 || column >= planeSeat[0].length) {
                System.out.println("Please enter a valid seat index.\n");
                buySeat(input, planeSeat);
                return;
            }

            if (planeSeat[row][column] == '0') {
                planeSeat[row][column] = '1';

                Person person = createPerson(input);
                Ticket ticket = createTicket(row, column, person);

                tickets[ticketCount] = ticket;
                ticketCount++;
                ticket.save();

                System.out.println("Seat Index " + seatIndex + " is Successfully reserved.\n");

                printMenu();
                handleUserInput(validateUserInput(input), input, planeSeat);
            } else {
                System.out.println("Seat Index " + seatIndex + " is Already booked.\n");
                buySeat(input, planeSeat);
            }

        } catch (Exception e) {
            System.out.println("Please enter a valid seatIndex.\n");
            buySeat(input, planeSeat);
        }
    }

    private static Ticket createTicket(int row, int column, Person person) {
        int price = 0;

        if (column <= 5) {
            price = 200;
        } else if (column > 5 && column <= 9) {
            price = 150;
        } else {
            price = 180;
        }
        Ticket ticket = new Ticket(row, column, price, person);
        // ticket.printTicketInfo();
        return ticket;
    }

    private static Person createPerson(Scanner input) {
        System.out.print("Enter your name: ");
        String name = input.nextLine();
        System.out.print("Enter your surname: ");
        String sureName = input.nextLine();
        System.out.print("Enter your email: ");
        String email = input.nextLine();
        return new Person(name, sureName, email);
    }

    private static void cancelSeat(Scanner input, char[][] planeSeat) {
        System.out.println("\nEnter desired seat Index (A1 - A2) :");
        String seatIndex = input.nextLine();

        try {
            int column = Integer.parseInt(seatIndex.substring(1));
            int row = Character.toUpperCase(seatIndex.charAt(0)) - 65;

            if (row < 0 || row >= planeSeat.length || column < 0 || column >= planeSeat[0].length) {
                System.out.println("Please enter a valid seat index.\n");
                cancelSeat(input, planeSeat);
                return;
            }

            if (planeSeat[row][column] == '1') {
                planeSeat[row][column] = '0';
                int ticketIndex = -1;
                for (int i = 0; i < ticketCount; i++) {
                    if (tickets[i].getRow() == row + 1 && tickets[i].getSeat() == column) {
                        ticketIndex = i;
                        break;
                    }
                }
                if (ticketIndex != -1) {
                    Ticket[] updatedTicketArray = new Ticket[tickets.length - 1];
                    for (int i = 0, k = 0; i < ticketCount; i++) {
                        if (i == ticketIndex) {
                            continue;
                        }
                        updatedTicketArray[k++] = tickets[i];
                    }
                    ticketCount--;
                    tickets = updatedTicketArray;
                    System.out.println("Seat Index " + seatIndex + " is Successfully cenceld.\n");
                } else {
                    System.out.println("Ticket not found");
                }

                printMenu();
                handleUserInput(validateUserInput(input), input, planeSeat);
            } else {
                System.out.println("Seat Index " + seatIndex + " is Already available .\n");
                cancelSeat(input, planeSeat);
            }

        } catch (Exception e) {
            System.out.println("Please enter a valid seatIndex.\n");
            cancelSeat(input, planeSeat);
        }
    }

    private static void findFirstAvailable(char[][] planeSeat, Scanner input) {
        boolean seatFound = false;

        for (int i = 0; i < planeSeat.length; i++) {
            for (int k = 1; k < planeSeat[i].length; k++) {
                if (planeSeat[i][k] == '0') {
                    System.out.println("First available seat: " + (char) (i + 'A') + (k));
                    seatFound = true;
                    break;
                }
            }
            if (seatFound) {
                break;
            }
        }

        if (!seatFound) {
            System.out.println("All seats are reserved.");
        }

        printMenu();
        handleUserInput(validateUserInput(input), input, planeSeat);
    }

}
