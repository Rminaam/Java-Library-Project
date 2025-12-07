import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final User admin = new Admin("A001", "admin", "admin", "Admin");
    private final LoginManager loginManager = new LoginManager();
    private final Library library = new Library();
    private final Scanner scanner = new Scanner(System.in);
    private ArrayList<User> newUsers = new ArrayList<>();
    private ArrayList<User> ApprovedUsers = new ArrayList<>();

    public Menu() throws IOException {
        library.addSampleItems();
        admin.approval = true;
        ApprovedUsers.add(admin);
        loginManager.register(admin);
        run();
    }

    private void run() throws IOException {
        boolean running = true;

        while (running) {
            System.out.println("'Library System'");
            System.out.println("-------------------------");
            System.out.println("[1] Login");
            System.out.println("[2] Register");
            System.out.println("[Q] Quit");
            System.out.print("Input: ");

            String choice = scanner.nextLine().trim();

            switch (choice.toLowerCase()) {
                case "1":
                    login();
                    break;
                case "2":
                    register();
                    break;
                case "q":
                    running = false;
                    System.out.println("Exiting system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
                    break;
            }
            System.out.println();
        }

        scanner.close();
    }

    // ----------------- LOGIN -----------------
    private void login() throws IOException {
        System.out.println("\n'Login'");
        System.out.println("-------------------------");

        System.out.print("User ID: ");
        String inputUserId = scanner.nextLine();

        System.out.print("Password: ");
        String inputPass = scanner.nextLine();

        User loggedInUser = loginManager.login(inputUserId, inputPass);

        if (loggedInUser != null) {
            if (loggedInUser.approval) {
                System.out.println("Login successful! Welcome, " + loggedInUser.getUserName() +
                        " [" + loggedInUser.getUserType() + "]\n");

                boolean mainPage = true;
                while (mainPage) {
                    if (loggedInUser.getUserType().equals("Admin")) {
                        showAdminMenu();
                        int menuInput = scanner.nextInt();
                        scanner.nextLine();
                        mainPage = handleAdminChoice(menuInput, loggedInUser);
                    } else {
                        showUserMenu(false);
                        int menuInput = scanner.nextInt();
                        scanner.nextLine();
                        mainPage = handleUserChoice(menuInput, loggedInUser);
                    }
                }
            } else {
                System.out.println("Admin hasn't confirmed you to login.");
            }
        } else {
            System.out.println("Login failed. Please check your userId and password.");
        }
    }

    // ----------------- REGISTER -----------------
    private void register() throws IOException {
        System.out.println("\n'Register'");
        System.out.println("-------------------------");
        System.out.println("Choose account type:");
        System.out.println("[1] Student");
        System.out.println("[2] Regular\n");
        System.out.print("Input: ");
        String choose = scanner.nextLine();

        String userType;
        User newUser = null;

        if ("1".equals(choose)) {
            userType = "Student";
            newUser = new Students("", "", "", userType);
        } else if ("2".equals(choose)) {
            userType = "Regular";
            newUser = new RegularUser("", "", "", userType);
        } else {
            System.out.println("Invalid Input.");
            return;
        }

        System.out.print("Choose a Username: ");
        String inputNewUser = scanner.nextLine();

        System.out.print("Choose a Password: ");
        String inputNewPass = scanner.nextLine();

        newUser.userName = inputNewUser;
        newUser.userPass = inputNewPass;
        newUser.setUserId(loginManager.generateUserId(newUser));

        if (loginManager.register(newUser)) {
            System.out.println("Registered Successfully.\n" +
                    "Your UserId is: " + newUser.getUserId());
            System.out.println("Waiting for admin confirmation.\n");
            newUsers.add(newUser);
        }

        System.out.println("Press any key to return to login page...");
        scanner.nextLine();
    }

    // ----------------- USER MENU -----------------
    private void showUserMenu(boolean isAdmin) {
        System.out.println("'Library System'");
        System.out.println("-------------------------");
        System.out.println("[1] Show available items");
        System.out.println("[2] Search item");
        System.out.println("[3] Loan item");
        System.out.println("[4] Show loaned items");
        System.out.println("[5] Return item");
        System.out.println("[6] Renew item");
        System.out.println("[7] Show fine");
        System.out.println("[8] Logout");

        if (isAdmin) {
            System.out.println("[9] Approve new users");
            System.out.println("[10] Remove a user");
            System.out.println("[11] Add/Remove media items");
        }

        System.out.println("-------------------------");
        System.out.print("Input: ");
    }

    // ----------------- ADMIN MENU -----------------
    private void showAdminMenu() {
        showUserMenu(true);
    }

    private boolean handleUserChoice(int menuInput, User loggedInUser) {
        try{
            switch (menuInput) {
                case 1: handleShowAvailableItems(); break;
                case 2: handleSearchItem(loggedInUser); break;
                case 3: handleLoanItem(loggedInUser); break;
                case 4: handleShowLoanedItems(loggedInUser); break;
                case 5: handleReturnItem(loggedInUser); break;
                case 6: handleRenewItem(loggedInUser); break;
                case 7: handleShowFine(loggedInUser); break;
                case 8:
                    System.out.println("Goodbye.");
                    return false;
                default:
                    handleInvalidInput(loggedInUser);
            }
        } catch (Exception e){
            System.out.println("Invalid Input.");
        }

        return true;
    }

    private boolean handleAdminChoice(int menuInput, User loggedInUser) {
        switch (menuInput) {
            case 1: handleShowAvailableItems(); break;
            case 2: handleSearchItem(loggedInUser); break;
            case 3: handleLoanItem(loggedInUser); break;
            case 4: handleShowLoanedItems(loggedInUser); break;
            case 5: handleReturnItem(loggedInUser); break;
            case 6: handleRenewItem(loggedInUser); break;
            case 7: handleShowFine(loggedInUser); break;
            case 8:
                System.out.println("Goodbye.");
                return false; // Logout
            case 9: handleApproveUsers(loggedInUser); break;
            case 10: handleRemoveUser(loggedInUser); break;
            case 11: handleAddOrRemoveMedia(loggedInUser); break;
            default:
                System.out.println("Invalid input. Please try again.");
        }
        return true;
    }

    // ----------------- HANDLERS -----------------
    private void handleShowAvailableItems() {
        System.out.println("Available Items:");
        System.out.println("--------------------------");
        List<Media> currentMediaList = library.getAvailableItems();
        for (int i = 0; i < currentMediaList.size(); i++) {
            Media item = currentMediaList.get(i);
            System.out.println((i + 1) + ". " + item.toString());
        }
    }

    private void handleSearchItem(User loggedInUser) {
        System.out.println("Search:");
        System.out.println("--------------------------");
        System.out.print("Enter the title you want to Search for: ");
        String searchInput = scanner.nextLine();
        System.out.println("--------------------------");

        if (searchInput.isBlank()) {
            System.out.println("Search input cannot be empty.\n");
            return;
        }

        List<Media> foundList = new ArrayList<>();
        for (Media item : library.getAvailableItems()) {
            if (item.getTitle().toLowerCase().contains(searchInput.toLowerCase()) ||
                    item.getId().contains(searchInput)) {
                foundList.add(item);
            }
        }

        if (foundList.isEmpty()) {
            System.out.println("No item matched your search.\n");
            return;
        } else {
            for (int i = 0; i < foundList.size(); i++) {
                System.out.println((i + 1) + ". " + foundList.get(i));
            }
            System.out.println("Do you wish to loan an item from this list? (Y/N)");
            String inputSearchedLoan = scanner.nextLine().toLowerCase();

            if ("y".equals(inputSearchedLoan)) {
                System.out.println("Enter the item ID you wish to loan: ");
                String selectedItem = scanner.nextLine();
                boolean itemFound = false;
                for (Media item : foundList) {
                    if (item.getId().equals(selectedItem.toUpperCase())) {
                        itemFound = true;
                        if (library.loanItem(loggedInUser, item)) {
                            System.out.println("Thank You!\n");
                        } else {
                            System.out.println("Item not available.\n");
                        }
                        break;
                    }
                }
                if (!itemFound) {
                    System.out.println("Invalid ID.");
                }
            }
        }
    }

    private void handleLoanItem(User loggedInUser) {
        boolean continueLoaning = true;
        while (continueLoaning) {
            List<Media> availableItems = library.getAvailableItems();
            if (availableItems.isEmpty()) {
                System.out.println("No available items to loan.");
                break;
            }
            System.out.println("Available Items:");
            System.out.println("--------------------------");
            int indicator = 1;
            for (Media item : availableItems) {
                System.out.println(indicator + ". " + item.toString());
                indicator++;
            }
            System.out.println("--------------------------");
            System.out.print("Enter the ID number of the item you wish to loan: ");
            String selectedItem = scanner.nextLine().trim();

            boolean itemFound = false;
            for (Media item : availableItems) {
                if (item.getId().equalsIgnoreCase(selectedItem)) {
                    itemFound = true;
                    if (library.loanItem(loggedInUser, item)) {
                        System.out.println("Thank you!\n");
                    } else {
                        System.out.println("Item not available.");
                    }
                    break;
                }
            }
            if (!itemFound) {
                System.out.println("Invalid ID.");
            }
            System.out.print("Do you wish to add anything else to your list? (Y/N): ");
            String addMore = scanner.nextLine().trim().toLowerCase();
            if (!addMore.equals("y")) {
                continueLoaning = false;
            }
        }
    }

    private void handleShowLoanedItems(User loggedInUser) {
        System.out.println("Your Loaned Items:");
        System.out.println("--------------------------");
        for (Media item : loggedInUser.borrowedItems) {
            System.out.println(item);
            System.out.println(" ");
        }
        System.out.println(" ");
    }

    private void handleReturnItem(User loggedInUser) {
        boolean returnAgain = true;
        if (loggedInUser.borrowedItems.isEmpty()) {
            System.out.println("You haven't borrowed anything yet.\n");
            returnAgain = false;
        }
        while (returnAgain) {
            System.out.println("Your Loaned Items:");
            System.out.println("--------------------------");
            for (Media item : loggedInUser.borrowedItems) {
                System.out.println(item);
            }
            System.out.println("--------------------------");
            System.out.print("Enter the ID number of the item you wish to return: ");
            String selectedItem = scanner.nextLine().trim().toUpperCase();
            boolean found = false;
            for (Media item : loggedInUser.borrowedItems) {
                if (item.getId().equalsIgnoreCase(selectedItem)) {
                    found = true;
                    if (library.returnItem(loggedInUser, item)) {
                        System.out.println("Thank you for returning the " + item.getType() +
                                " \"" + item.getTitle() + "\".");
                    } else {
                        System.out.println("You haven't borrowed this item.");
                    }
                    break;
                }
            }
            if (!found) {
                System.out.println("Invalid ID.");
            }
            System.out.print("Do you want to return anything else? Y/N: ");
            String selectInput = scanner.nextLine().toLowerCase();
            if (!selectInput.equals("y")) {
                returnAgain = false;
            }
        }
    }

    private void handleRenewItem(User loggedInUser) {
        boolean renewAgain = true;
        if (loggedInUser.borrowedItems.isEmpty()) {
            System.out.println("You haven't borrowed anything yet.\n");
            renewAgain = false;
        }
        while (renewAgain) {
            System.out.println("Your Loaned Items:");
            System.out.println("--------------------------");
            for (Media item : loggedInUser.borrowedItems) {
                System.out.println(item);
            }
            System.out.println("--------------------------");
            System.out.print("Enter the ID number of the item you wish to renew: ");
            String selectedItem = scanner.nextLine().trim().toUpperCase();
            boolean found = false;
            for (Media item : loggedInUser.borrowedItems) {
                if (item.getId().equalsIgnoreCase(selectedItem)) {
                    found = true;
                    if (library.renewItem(loggedInUser, item)) {
                        System.out.println("You successfully renewed the " + item.getType() +
                                " " + item.getTitle());
                    }
                    break;
                }
            }
            if (!found) {
                System.out.println("Invalid ID.");
            }
            System.out.print("Do you want to renew anything else? Y/N: ");
            String selectInput = scanner.nextLine().toLowerCase();
            if (!selectInput.equals("y")) {
                renewAgain = false;
            }
        }
    }

    private void handleShowFine(User loggedInUser) {
        if (loggedInUser.borrowedItems.isEmpty()) {
            System.out.println("You haven't borrowed anything yet.");
        } else {
            double totalFine = 0.0;
            for (Media item : loggedInUser.borrowedItems) {
                totalFine += library.fineCalculate(loggedInUser, item);
            }
            System.out.println("--------------------------");
            System.out.println("Total fine: $" + totalFine);
        }
    }

    private void handleInvalidInput(User loggedInUser) {
        System.out.println("Invalid input. Please try again.");
    }

    private void handleApproveUsers(User loggedInUser) {
        boolean approveMore = true;

        while (approveMore) {
            if (newUsers.isEmpty()) {
                System.out.println("No users waiting for approval.\n");
                break;
            }

            System.out.println("Pending users to be approved:\n");
            for (User user : newUsers) {
                System.out.println("Username: " + user.getUserName() +
                        " | ID: " + user.getUserId() +
                        " | Type: " + user.getUserType());
            }

            System.out.println("--------------------------");
            System.out.print("Enter UserId to approve: ");
            String inputId = scanner.nextLine().trim();

            boolean found = false;
            User approvedUser = null;

            for (User user : newUsers) {
                if (user.getUserId().equalsIgnoreCase(inputId)) {
                    user.approval = true;
                    ApprovedUsers.add(user);
                    approvedUser = user;
                    found = true;
                    break;
                }
            }

            if (found) {
                newUsers.remove(approvedUser);
                System.out.println("User approved successfully.");

                System.out.print("Do you want to approve another user? (Y/N): ");
                String yesOrNo = scanner.nextLine().trim();
                if (yesOrNo.equalsIgnoreCase("n")) {
                    approveMore = false;
                }
            } else {
                System.out.println("Invalid ID.");
            }
        }
    }

    private void handleRemoveUser(User loggedInUser) {
        boolean removeMore = true;

        while (removeMore) {
            if (ApprovedUsers.isEmpty()) {
                System.out.println("No approved users available.");
                break;
            }

            for (User user : ApprovedUsers) {
                System.out.println("Username: " + user.getUserName() +
                        " | ID: " + user.getUserId() +
                        " | Type: " + user.getUserType());
            }

            System.out.print("Enter UserID to remove: ");
            String removeInput = scanner.nextLine().toUpperCase();

            User toRemove = null;
            for (User user : ApprovedUsers) {
                if (user.getUserId().equalsIgnoreCase(removeInput)) {
                    toRemove = user;
                    break;
                }
            }

            if (toRemove != null) {
                ApprovedUsers.remove(toRemove);
                toRemove.approval = false;
                System.out.println("User removed successfully.");

                System.out.print("Do you want to remove another user? (Y/N): ");
                String yesOrNo = scanner.nextLine().trim();
                if (yesOrNo.equalsIgnoreCase("n")) {
                    removeMore = false;
                }
            } else {
                System.out.println("Invalid ID.");
            }
        }
    }

    private void handleAddOrRemoveMedia(User loggedInUser) {
        boolean RemoveMore = true;
        boolean AddMore = true;
        List<Media> allMediaItems = library.getAllItems();

        System.out.println("[1] Add");
        System.out.println("[2] Remove");
        System.out.print("Input: ");
        String adminInput = scanner.nextLine().trim();

        // ---------- REMOVE ----------
        if (adminInput.equals("2")) {
            while (RemoveMore) {
                if (allMediaItems.isEmpty()) {
                    System.out.println("No media items available.");
                    break;
                }

                for (int i = 0; i < allMediaItems.size(); i++) {
                    Media item = allMediaItems.get(i);
                    System.out.println((i + 1) + ". " + item.toString());
                }

                System.out.println("--------------------------");
                System.out.print("Enter Item Id to remove: ");
                String remove = scanner.nextLine().toUpperCase().trim();

                boolean removed = false;
                for (Media item : allMediaItems) {
                    if (item.getId().equalsIgnoreCase(remove)) {
                        allMediaItems.remove(item);
                        removed = true;
                        System.out.println("Item removed successfully.");
                        break;
                    }
                }

                if (!removed) {
                    System.out.println("Invalid ID.");
                } else {
                    System.out.print("Do you want to remove anything else? (Y/N): ");
                    String yesOrNo = scanner.nextLine();
                    if (yesOrNo.equalsIgnoreCase("n")) {
                        RemoveMore = false;
                    }
                }
            }
        }

        // ---------- ADD ----------
        else if (adminInput.equals("1")) {
            while (AddMore) {
                boolean added = false;
                boolean exists = false;

                System.out.println("What type of media do you want to add? ");
                System.out.println("[1] Book\n[2] CD\n[3] DVD\n[4] Video Game\n[5] Board Game");
                System.out.print("Input: ");
                String selectInput = scanner.nextLine().trim();

                switch (selectInput) {
                    case "1":
                        System.out.print("Enter the id of the book: ");
                        String bookIdInput = scanner.nextLine().toUpperCase();
                        for (Media item : allMediaItems) {
                            if (item.getId().equals(bookIdInput)) {
                                System.out.println("ID already exists.");
                                exists = true;
                                break;
                            }
                        }
                        if (!exists) {
                            System.out.print("Enter the title of the book: ");
                            String bookTitleInput = scanner.nextLine();
                            System.out.print("Enter the author of the book: ");
                            String bookAuthorInput = scanner.nextLine();
                            System.out.print("Enter the publish year of the book: ");
                            String bookYearInput = scanner.nextLine();
                            Media newBook = new Book(bookIdInput, bookTitleInput, bookAuthorInput, bookYearInput);
                            library.addMedia(newBook);
                            newBook.setBorrowed(false);
                            added = true;
                            System.out.println("Book added successfully.");
                        }
                        break;

                    case "2":
                        System.out.print("Enter the id of the CD: ");
                        String cdIdInput = scanner.nextLine().toUpperCase();
                        for (Media item : allMediaItems) {
                            if (item.getId().equals(cdIdInput)) {
                                System.out.println("ID already exists.");
                                exists = true;
                                break;
                            }
                        }
                        if (!exists) {
                            System.out.print("Enter the title of the CD: ");
                            String cdTitleInput = scanner.nextLine();
                            System.out.print("Enter the artist of the CD: ");
                            String cdArtistInput = scanner.nextLine();
                            Media newCD = new CD(cdIdInput, cdTitleInput, cdArtistInput);
                            allMediaItems.add(newCD);
                            newCD.setBorrowed(false);
                            added = true;
                            System.out.println("CD added successfully.");
                        }
                        break;

                    case "3":
                        System.out.print("Enter the id of the DVD: ");
                        String dvdIdInput = scanner.nextLine().toUpperCase();
                        for (Media item : allMediaItems) {
                            if (item.getId().equals(dvdIdInput)) {
                                System.out.println("ID already exists.");
                                exists = true;
                                break;
                            }
                        }
                        if (!exists) {
                            System.out.print("Enter the title of the DVD: ");
                            String dvdTitleInput = scanner.nextLine();
                            System.out.print("Enter the artist of the DVD: ");
                            String dvdArtistInput = scanner.nextLine();
                            System.out.print("Enter the publish year of the DVD: ");
                            String dvdYearInput = scanner.nextLine();
                            Media newDVD = new DVD(dvdIdInput, dvdTitleInput, dvdArtistInput, dvdYearInput);
                            allMediaItems.add(newDVD);
                            newDVD.setBorrowed(false);
                            added = true;
                            System.out.println("DVD added successfully.");
                        }
                        break;

                    case "4":
                        System.out.print("Enter the id of the Video Game: ");
                        String videoGameIdInput = scanner.nextLine().toUpperCase();
                        for (Media item : allMediaItems) {
                            if (item.getId().equals(videoGameIdInput)) {
                                System.out.println("ID already exists.");
                                exists = true;
                                break;
                            }
                        }
                        if (!exists) {
                            System.out.print("Enter the title of the Video Game: ");
                            String videoGameTitleInput = scanner.nextLine();
                            System.out.print("Enter the publisher of the Video Game: ");
                            String videoGamePublisherInput = scanner.nextLine();
                            System.out.print("Enter the publish year of the Video Game: ");
                            String videoGameYearInput = scanner.nextLine();
                            System.out.print("Enter the platform of the Video Game: ");
                            String videoGamePlatformInput = scanner.nextLine();
                            Media newVideoGame = new VideoGame(videoGameIdInput, videoGameTitleInput, videoGamePublisherInput, videoGameYearInput, videoGamePlatformInput);
                            allMediaItems.add(newVideoGame);
                            newVideoGame.setBorrowed(false);
                            added = true;
                            System.out.println("Video Game added successfully.");
                        }
                        break;

                    case "5":
                        System.out.print("Enter the id of the Board Game: ");
                        String boardGameIdInput = scanner.nextLine().toUpperCase();
                        for (Media item : allMediaItems) {
                            if (item.getId().equals(boardGameIdInput)) {
                                System.out.println("ID already exists.");
                                exists = true;
                                break;
                            }
                        }
                        if (!exists) {
                            System.out.print("Enter the title of the Board Game: ");
                            String boardGameTitleInput = scanner.nextLine();
                            System.out.print("Enter the publisher of the Board Game: ");
                            String boardGamePublisherInput = scanner.nextLine();
                            Media newBoardGame = new BoardGame(boardGameIdInput, boardGameTitleInput, boardGamePublisherInput);
                            allMediaItems.add(newBoardGame);
                            newBoardGame.setBorrowed(false);
                            added = true;
                            System.out.println("Board Game added successfully.");
                        }
                        break;

                    default:
                        System.out.println("Invalid Input.");
                        break;
                }

                if (added) {
                    System.out.print("Do you want to add anything else? (Y/N): ");
                    String yesOrNo = scanner.nextLine();
                    if (yesOrNo.equalsIgnoreCase("n")) {
                        AddMore = false;
                    }
                }
            }
        } else {
            System.out.println("Invalid Input.");
        }
    }
}