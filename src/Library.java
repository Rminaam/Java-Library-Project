import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Library {
    private List<Media> mediaItems = new ArrayList<>();
    private LocalDate currentDate;

    public HashMap<String, Integer> itemAvailability = new HashMap<>();

    public void addSampleItems(){
        mediaItems.add(new Book("B0001", "The Great Gatsby", "F. Scott Fitzgerald", "1925"));
        mediaItems.add(new DVD("D0001", "Inception", "Christopher Nolan", "2010"));
        mediaItems.add(new VideoGame("V0001", "God of War", "Santa Monica", "2018", "PS4,PS5,PC"));
        mediaItems.add(new BoardGame("BG001", "Istgahe Akhar", "Dorehami"));
        mediaItems.add(new CD("C0001", "Game of Thrones SoundTrack", "Ramin Djawadi"));
        mediaItems.add(new DVD("D0002", "Fight Club", "David Fincher", "2005"));
        mediaItems.add(new VideoGame("V0002", "The Last of Us", "Naughty Dog", "2013", "PS3,PS4,PS5,PC"));
        mediaItems.add(new Book("B0002", "Brief Answers to Big Questions", "Steven Hawking", "2018"));
        mediaItems.add(new CD("C0002", "Red", "Taylor Swift"));
        mediaItems.add(new BoardGame("BG002", "Exploding Kittens", "The Oatmeal"));
        mediaItems.add(new VideoGame("V0003", "Batman Arkham Knight", "Rocksteady", "2015", "PS4,XBOX,PC"));
        mediaItems.add(new DVD("D0003", "SpiderMan into the Spider Verse", "Sony", "2018"));
        mediaItems.add(new Book("B0003", "Harry Potter and the Goblet of Fire", "J.K Rolling", "2000"));
        mediaItems.add(new CD("C0003", "Red Dead Redemption Soundtrack", "Rockstar"));
        mediaItems.add(new BoardGame("BG0003", "UNO", "International Games"));
        mediaItems.add(new VideoGame("V0004", "Batman Arkham Origins", "Rocksteady", "2012", "PS3,PS4,XBOX,PC"));
        mediaItems.add(new Book("B0004", "Batman: Year One", "Frank Miller", "1987"));
        mediaItems.add(new Book("B0005", "Batman: The Killing Joke", "Alan Moore", "1988"));

        for (Media item : mediaItems){
            itemAvailability.put(item.id, 1);
        }
    }

    public boolean loanItem(User user, Media item) {
        if (getItemAvailability(item)) {
            user.borrowedItems.add(item);
            itemAvailability.put(item.getId(), 0);
            item.borrow(LocalDate.now());
            item.setBorrowed(true);
            System.out.println("You successfully loaned the " + item.getType() + " " + item.getTitle() + " for " + item.getLoanPeriod() + " weeks.");
            return true;
        }
        return false;
    }

    public boolean getItemAvailability(Media item){
        return itemAvailability.getOrDefault(item.id, 0) > 0;
    }

    public List<Media> getAllItems(){
        return mediaItems;
    }

    public List<Media> getAvailableItems(){
        return mediaItems.stream()
                .filter(item -> !item.isBorrowed())
                .collect(Collectors.toList());
    }

    public boolean returnItem(User user, Media item) {
        if (user.borrowedItems.contains(item)) {
            LocalDate now = LocalDate.now();

            if (item.dueDate != null && now.isAfter(item.dueDate)) {
                long daysLate = java.time.temporal.ChronoUnit.DAYS.between(item.dueDate, now);
                double totalFee;

                if (daysLate <= 7) {
                    totalFee = daysLate * 1.0;
                } else {
                    totalFee = (7 * 1.0) + ((daysLate - 7) * 2.0);
                }
                if (user.getUserType().equals("Student")){
                    totalFee -= totalFee/2;
                }

                user.addFee(totalFee);
                System.out.println("Item returned late by " + daysLate + " days. Late fee: €" + totalFee);
            }

            itemAvailability.put(item.getId(), 1);
            item.returnItem();
            user.returnItem(item);
            return true;
        }
        return false;
    }


    public boolean renewItem(User user, Media item) {
        if (user.borrowedItems.contains(item)) {
            if (item.canRenew()) {
                item.renew();
                return true;
            } else {
                System.out.println("Renewal limit reached for " + item.getTitle());
                return false;
            }
        }
        return false;
    }

    public void addMedia (Media item){
        mediaItems.add(item);
    }

    public double fineCalculate(User user, Media item) {
        if (!user.borrowedItems.contains(item)) {
            return 0.0;
        }

        LocalDate now = LocalDate.now();
        if (item.dueDate == null || !now.isAfter(item.dueDate)) {
            return 0.0;
        }

        long daysLate = java.time.temporal.ChronoUnit.DAYS.between(item.dueDate, now);
        double totalFee;

        if (daysLate <= 7) {
            totalFee = daysLate * 1.0;
        } else {
            totalFee = (7 * 1.0) + ((daysLate - 7) * 2.0);
        }
        if (user.getUserType().equals("Student")){
            totalFee -= totalFee/2;
        }

        user.addFee(totalFee);
        return totalFee;
    }

}
