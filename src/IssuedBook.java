import java.time.LocalDate;
public class IssuedBook {

    private int issueId;
    private int bookId;
    private int userId;
    private LocalDate issueDate;
    private LocalDate returnDate;
    private LocalDate actualReturnDate;
    private String status;

    public IssuedBook(int bookId, int userId,LocalDate issueDate, LocalDate returnDate, String status){
        this.bookId=bookId;
        this.userId=userId;
        this.issueDate=issueDate;
        this.returnDate=returnDate;
        this.status=status;
    }
    public int getIssueId(){
        return issueId;
    }
    public void setIssueId(int issueId){
        this.issueId=issueId;
    }
    public int getUserId(){
        return userId;
    }
    public void setUserId(int userId){
        this.userId=userId;
    }
    public int getBookId(){
        return bookId;
    }
    public void setBookId(int bookId){
        this.bookId=bookId;
    }
    public LocalDate getIssueDate(){
        return issueDate;
    }
    public void setIssueDate(LocalDate issueDate){
        this.issueDate=issueDate;
    }
    public LocalDate getReturnDate(){
        return returnDate;
    }
    public void setReturnDate(LocalDate returnDate){
        this.returnDate=returnDate;
    }

    public LocalDate getActualReturnDate(){
        return actualReturnDate;
    }
    public void setActualReturnDate(LocalDate actualReturnDate){
        this.actualReturnDate=actualReturnDate;
    }
    public String getStatus(){
        return status;
    }
    public void setString(String status){
        this.status=status;
    }


    
}
