import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
public class IssuedBookDAO {
    Connection conn;
    public IssuedBookDAO(){
        conn=DBConnection.getConnection();
    }
    public void issueBook(int bookId,int userId){
        try{

            //Check if Users exist
            String  checckUser = "select * from users where user_id = ?";
            PreparedStatement psUser = conn.prepareStatement(checckUser);
            psUser.setInt(1,userId);
            ResultSet userRs = psUser.executeQuery();
            if(!userRs.next()){
                System.out.println("User not Found");
                return;
            }

            // Check if Book Exist
            String checkBook = "select available from books where book_id = ?";
            PreparedStatement pst1 = conn.prepareStatement(checkBook);
            pst1.setInt(1,bookId);
            ResultSet rs = pst1.executeQuery();
            if(!rs.next()){
                System.out.println("Book Not Found");
                return;
            }
            if(!rs.getBoolean("available")){
                System.out.println("Book is already issued.");
                return;    
            }



            LocalDate issueDate = LocalDate.now();
            LocalDate returnDate = issueDate.plusDays(15);

            String insertIssue = """
                insert into issued_books(book_id,user_id,issue_date,return_date,status)
                values (?,?,?,?,?)
                """;
            PreparedStatement pst2 = conn.prepareStatement(insertIssue);
            pst2.setInt(1,bookId);
            pst2.setInt(2,userId);
            pst2.setDate(3,java.sql.Date.valueOf(issueDate));
            pst2.setDate(4,java.sql.Date.valueOf(returnDate));
            pst2.setString(5,"ISSUED");
            pst2.executeUpdate();



            String updateBook = "Update books set available = false, issued_to = ? where book_id = ?";
            PreparedStatement pst3 = conn.prepareStatement(updateBook);
            pst3.setInt(1,userId);
            pst3.setInt(2,bookId);
            pst3.executeUpdate();
            
            System.out.println("Book Issued Successfully!!!!");
            System.out.println("Issue Date: "+issueDate);
            System.out.println("Return Date: "+ returnDate);
        }catch( SQLException e){
            e.printStackTrace();
        }        
    }

    public void returnBook(int bookId) {

        try {

            String findIssue = """
                SELECT * FROM issued_books
                WHERE book_id = ? AND status = 'ISSUED'
                """;

            PreparedStatement ps = conn.prepareStatement(findIssue);
            ps.setInt(1, bookId);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("This book is not currently issued.");
                return;
            }

            int issueId = rs.getInt("issue_id");

            LocalDate dueDate = rs.getDate("return_date").toLocalDate();
            LocalDate today = LocalDate.now();

            long lateDays = java.time.temporal.ChronoUnit.DAYS.between(dueDate, today);

            if (lateDays < 0) {
                lateDays = 0;
            }

            long fine = lateDays * 5;

            String updateIssue = """
                UPDATE issued_books
                SET actual_return_date = ?,
                    status = 'RETURNED'
                    WHERE issue_id = ?
                """;

            PreparedStatement ps2 = conn.prepareStatement(updateIssue);

            ps2.setDate(1, java.sql.Date.valueOf(today));
            ps2.setInt(2, issueId);

            ps2.executeUpdate();

            String updateBook = """
                    UPDATE books
                    SET available = true,
                        issued_to = NULL
                    WHERE book_id = ?
                    """;

            PreparedStatement ps3 = conn.prepareStatement(updateBook);
            ps3.setInt(1, bookId);

            ps3.executeUpdate();

            System.out.println("\n========== Return Receipt ==========");
            System.out.println("Book Returned Successfully!");
            System.out.println("Return Date : " + today);
            System.out.println("Late Days   : " + lateDays);
            System.out.println("Fine        : Rs." + fine);
            System.out.println("====================================");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
