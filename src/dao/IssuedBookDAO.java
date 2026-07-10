package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Statement;

import util.DBConnection;
public class IssuedBookDAO {
    Connection conn;
    public IssuedBookDAO(){
        conn=DBConnection.getConnection();
    }
    public void issueBook(int bookId,int userId){
        try{
            conn.setAutoCommit(false);

            //Check if Users exist
            String  checkUser = "select * from users where user_id = ?";
            PreparedStatement psUser = conn.prepareStatement(checkUser);
            psUser.setInt(1,userId);
            ResultSet userRs = psUser.executeQuery();
            if(!userRs.next()){
                System.out.println("User not Found");
                conn.rollback();
                return;
            }

            // Check if Book Exist
            String checkBook = "select available from books where book_id = ?";
            PreparedStatement pst1 = conn.prepareStatement(checkBook);
            pst1.setInt(1,bookId);
            ResultSet rs = pst1.executeQuery();
            if(!rs.next()){
                System.out.println("Book Not Found");
                conn.rollback();
                return;
            }
            if(!rs.getBoolean("available")){
                System.out.println("Book is already issued.");
                conn.rollback();
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

            conn.commit();

        }catch( SQLException e){
            try{
                conn.rollback();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
            
            e.printStackTrace();
        }
        finally{
            try{
                conn.setAutoCommit(true);
            }catch(SQLException e){
                e.printStackTrace();
            }
        }       
    }

    public void returnBook(int bookId) {

        try {
            conn.setAutoCommit(false);

            String findIssue = """
                SELECT * FROM issued_books
                WHERE book_id = ? AND status = 'ISSUED'
                """;

            PreparedStatement ps = conn.prepareStatement(findIssue);
            ps.setInt(1, bookId);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("This book is not currently issued.");
                conn.rollback();
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
            conn.commit();

        } catch (SQLException e) {
            try{
                conn.rollback();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }finally{
            try{
                conn.setAutoCommit(true);
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    public void viewCurrentlyIssuedBooks(){
        String sql = "Select ib.issue_id,b.book_id,b.title, b.author, u.name, ib.issue_date, ib.return_date, ib.status from issued_books ib INNER JOIN books b ON ib.book_id = b.book_id INNER JOIN users u ON ib.user_id = u.user_id where ib.status = 'ISSUED'";
        try{
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            boolean found = false;
            
            while(rs.next()){
                found = true;
                LocalDate dueDate = rs.getDate("return_date").toLocalDate();
                LocalDate today = LocalDate.now();

                long daysLeft = java.time.temporal.ChronoUnit.DAYS.between(today, dueDate);

                System.out.println("\n======================================");
                System.out.println("Issue ID    : " + rs.getInt("issue_id"));
                System.out.println("Book ID     : " + rs.getInt("book_id"));
                System.out.println("Title       : " + rs.getString("title"));
                System.out.println("Author      : " + rs.getString("author"));
                System.out.println("Issued To   : " + rs.getString("name"));
                LocalDate issueDate = rs.getDate("issue_date").toLocalDate();
                System.out.println("Issue Date : " + issueDate);
                System.out.println("Due Date    : " + rs.getDate("return_date"));
                if (daysLeft >= 0) {
                    System.out.println("Days Left  : " + daysLeft);
                } else {
                    System.out.println("Overdue By : " + (-daysLeft) + " day(s)");
                }
                System.out.println("Status      : " + rs.getString("status"));
                System.out.println("======================================");

            }
            if(!found){
                System.out.println("No issued books found.");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public void viewIssuedBooksHistory(){
        String sql = "Select ib.issue_id,b.book_id,b.title, b.author, u.name, ib.issue_date, ib.return_date, ib.status from issued_books ib INNER JOIN books b ON ib.book_id = b.book_id INNER JOIN users u ON ib.user_id = u.user_id order by ib.issue_id";
        try{
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            boolean found = false;
            while(rs.next()){
                found = true;
                System.out.println("\n======================================");
                System.out.println("Issue ID    : " + rs.getInt("issue_id"));
                System.out.println("Book ID     : " + rs.getInt("book_id"));
                System.out.println("Title       : " + rs.getString("title"));
                System.out.println("Author      : " + rs.getString("author"));
                System.out.println("Issued To   : " + rs.getString("name"));
                System.out.println("Issue Date  : " + rs.getDate("issue_date"));
                System.out.println("Due Date    : " + rs.getDate("return_date"));
                System.out.println("Status      : " + rs.getString("status"));
                System.out.println("======================================");

            }
            if(!found){
                System.out.println("No issued books found.");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void viewOverDueBooks(){
        String sql = """
                select 
                    ib.issue_id, b.book_id, b.title, b.author,u.name,ib.issue_date,ib.return_date
                from issued_books ib 
                INNER JOIN books b
                    ON ib.book_id = b.book_id
                INNER JOIN users u 
                    ON ib.user_id = u.user_id
                WHERE ib.status = 'ISSUED'
                """;
        try{
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            boolean found = false;
            while(rs.next()){
                LocalDate dueDate = rs.getDate("return_date").toLocalDate();
                LocalDate today = LocalDate.now();
                long lateDays = java.time.temporal.ChronoUnit.DAYS.between(dueDate,today);
                if(lateDays > 0){
                    found = true;
                    System.out.println("\n====================================");
                    System.out.println("Issue ID    : " + rs.getInt("issue_id"));
                    System.out.println("Book ID     : " + rs.getInt("book_id"));
                    System.out.println("Title       : " + rs.getString("title"));
                    System.out.println("Author      : " + rs.getString("author"));
                    System.out.println("Borrower    : " + rs.getString("name"));
                    System.out.println("Issue Date  : " + rs.getDate("issue_date"));
                    System.out.println("Due Date    : " + dueDate);
                    System.out.println("Late Days   : " + lateDays);
                    System.out.println("Fine        : Rs." + (lateDays * 5));
                    System.out.println("====================================");
                }
            }
            if(!found){
                System.out.println("No overdue books found.");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }     
    }

    public void topReaders(){
        String sql ="""
                select
                    u.user_id,u.name,count(ib.issue_id) as total_books
                from users u
                INNER JOIN issued_books ib
                    on u.user_id = ib.user_id
                group by u.user_id, u.name
                order by total_books desc
                """;
        try{
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            boolean found = false;
            System.out.println("\n============== TOP READERS ===============");
            while(rs.next()){
                found = true;
                System.out.println(
                    "User ID : " + rs.getInt("user_id") +
                    " | Name : " + rs.getString("name") +
                    " | Borrowed : " + rs.getInt("total_books")
                );
            }
            if(!found){
                System.out.println("No records found");
            }
            System.out.println("==============================================");

        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public void mostIssuedBooks(){
        String sql = """
                select 
                    b.book_id,b.title,b.author,count(ib.issue_id) as issue_count
                from books b
                INNER JOIN issued_books ib
                    ON b.book_id = ib.book_id
                group by b.book_id,b.title,b.author
                order by issue_count desc
                """;
        try{
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            boolean found = false;
            System.out.println("\n===================   MOST ISSUED BOOKS   ====================");
            while(rs.next()){
                found = true;
                System.out.println(
                    "Book ID : " + rs.getInt("book_id") +
                    " | Title : " + rs.getString("title") +
                    " | Author : " + rs.getString("author") +
                    " | Times Issued : " + rs.getInt("issue_count")
                );
            }
            if(!found){
                System.out.println("No records found.");
            }
            System.out.println("====================================================================");

        }catch(SQLException e){
            e.printStackTrace();
        }

    }
    public void libraryDashBoard(){
        try{        
            Statement stmt = conn.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("Select count(*) from users");
            rs.next();
            int totalUsers = rs.getInt(1);

            rs = stmt.executeQuery("SELECT COUNT(*) FROM books");
            rs.next();
            int totalBooks = rs.getInt(1);

            rs = stmt.executeQuery("SELECT COUNT(*) FROM books WHERE available = true");
            rs.next();
            int availableBooks = rs.getInt(1);

            
            rs = stmt.executeQuery("SELECT COUNT(*) FROM issued_books WHERE status = 'ISSUED'");
            rs.next();
            int issuedBooks = rs.getInt(1);

            
            rs = stmt.executeQuery("""
                    SELECT COUNT(*)
                    FROM issued_books
                    WHERE status = 'ISSUED'
                    AND return_date < CURDATE()
                    """);
            rs.next();
            int overdueBooks = rs.getInt(1);

            
            rs = stmt.executeQuery("""
                    SELECT u.name, COUNT(*) AS total
                    FROM users u
                    JOIN issued_books ib
                    ON u.user_id = ib.user_id
                    GROUP BY u.user_id, u.name
                    ORDER BY total DESC
                    LIMIT 1
                    """);

            String topReader = "None";
            int booksBorrowed = 0;

            if (rs.next()) {
                topReader = rs.getString("name");
                booksBorrowed = rs.getInt("total");
            }
            rs = stmt.executeQuery("""
                    SELECT b.title, COUNT(*) AS total
                    FROM books b
                    JOIN issued_books ib
                    ON b.book_id = ib.book_id
                    GROUP BY b.book_id, b.title
                    ORDER BY total DESC
                    LIMIT 1
                    """);

            String topBook = "None";
            int issueCount = 0;

            if (rs.next()) {
                topBook = rs.getString("title");
                issueCount = rs.getInt("total");
            }
            System.out.println("\n============== LIBRARY DASHBOARD ==============");
            System.out.println("Total Users           : " + totalUsers);
            System.out.println("Total Books           : " + totalBooks);
            System.out.println("Available Books       : " + availableBooks);
            System.out.println("Currently Issued      : " + issuedBooks);
            System.out.println("Overdue Books         : " + overdueBooks);
            System.out.println("-----------------------------------------------");
            System.out.println("Top Reader            : " + topReader + " (" + booksBorrowed + " books)");
            System.out.println("Most Issued Book      : " + topBook + " (" + issueCount + " times)");
            System.out.println("===============================================");

        }catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
