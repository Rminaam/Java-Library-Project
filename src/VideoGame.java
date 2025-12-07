import java.time.LocalDate;

public class VideoGame extends Media{
    private String publisher;
    private String yearOfPublish;
    private String platform;
    private int renewCount;

    public VideoGame(String id, String title, String publisher, String yearOfPublish, String platform){
        super(id, title);
        this.publisher = publisher;
        this.yearOfPublish = yearOfPublish;
        this.platform = platform;
    }

    @Override
    public int getLoanPeriod(){
        return 4;
    }

    @Override
    public boolean canRenew(){
        return renewCount < 1;
    }

    @Override
    public void renew(){
        if (canRenew()){
            dueDate = dueDate.plusWeeks(getLoanPeriod());
            renewCount++;
        } else {
            System.out.println("Renewal limit reached for " + title);
        }
    }

    public String getType(){
        return "[Video Game]";
    }

    public String getID(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getPublisher(){
        return publisher;
    }

    public String getYearOfPublish(){
        return yearOfPublish;
    }

    public String getPlatform(){
        return platform;
    }

    public boolean getIsBorrowed(){
        return isBorrowed;
    }

    public LocalDate getDueDate(){
        return dueDate;
    }

    @Override
    public String toString(){
        return "[Video Game] " + title + " by " + publisher + " | Year: "
                + yearOfPublish + " | Platform: " + platform
                + " | ID: " + id + (isBorrowed ? " | Due: " + dueDate : " | Available\n");
    }
}
