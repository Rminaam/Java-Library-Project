    import java.time.LocalDate;

    public class Book extends Media{
        private String author;
        private String yearOfPublish;
        private int renewCount;

        public Book (String id, String title, String author, String yearOfPublish){
            super(id,title);
            this.author = author;
            this.yearOfPublish = yearOfPublish;
            this.maxRenewal = 3;
        }

        @Override
        public int getLoanPeriod(){
            return 4;
        }

        @Override
        public boolean canRenew(){
            return renewCount < 3;
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

        public String getTitle(){
            return title;
        }

        public String getAuthor(){
            return author;
        }

        public String getYearOfPublish(){
            return yearOfPublish;
        }

        public String getID(){
            return id;
        }

        public LocalDate getDueDate(){
            return dueDate;
        }

        public boolean getIsBorrowed(){
            return isBorrowed;
        }

        public String getType(){
            return "[Book]";
        }

        @Override
        public String toString(){
            return "[Book] " + title + " by " + author +
                    " | ID: " + id + (isBorrowed ? " | Due: " + dueDate : " | Available\n");
        }
    }
