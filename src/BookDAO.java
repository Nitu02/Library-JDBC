import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class BookDAO {

    public void addBook(Book book){
        String sql = "Insert into books(book_id,title,author,available) values(?,?,?,?) ";
        try{
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setInt(1,book.getBookId());
            pst.setString(2,book.getBooktitle());
            pst.setString(3,book.getBookAuthor());
            pst.setBoolean(4,book.isAvailable());

            pst.executeUpdate();
            System.out.println("Book added Successfully!!!!");

        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }
    public void viewBooks(){
        String sql = "SELECT b.book_id,b.title,b.author,b.available,u.name FROM books b LEFT JOIN users u ON b.issued_to = u.user_id";
        try{
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                String issuedTo = rs.getString("name");
                System.out.println(
                    "Book ID: "+ rs.getInt("book_id")+
                    ", Book Title: " + rs.getString("title") +
                    ", Book Author: " + rs.getString("author") +
                    ", Book Availability: " + rs.getBoolean("available") +
                    ", Issued To: " + (issuedTo == null ? "None" : issuedTo)
                );
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public Book searchBookById(int book_id){
        String sql ="select * from books where book_id = ?";
        try{
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1,book_id);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                return new Book(
                    rs.getInt("book_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getBoolean("available")
                );
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public void deleteBookById(int book_id){
        String sql = "Delete from books where book_id =?";
        try{
            Connection conn =DBConnection.getConnection();
            PreparedStatement  pst = conn.prepareStatement(sql);
            pst.setInt(1,book_id);
            int rows =pst.executeUpdate();
            if(rows>0){
                System.out.println("Book deleted Successfully!!!!!");
            }
            else{
                System.out.println("Book not Found");
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void issueBook(int bookId, int userId){
        String sql = "Update books set available = false, issued_to= ? where book_id=?";
        try{
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1,userId);
            pst.setInt(2, bookId);

            int rows = pst.executeUpdate();
            if(rows>0){
                System.out.println("Book Issued Succesfull!!!!");
            }
            else{
                System.out.println("Book not Found");
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void returnBook(int bookId){
        String sql = "Update books set available = true, issued_to = NULL where book_id = ?";
        try{
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setInt(1,bookId);
            int rows =pst.executeUpdate();
            if(rows>0){
                System.out.println("Book Returned SuccessFully!!!");
            }else{
                System.out.println("Book not Found");
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void totalBooks(){
        String sql = "select count(*) from books";
        try{
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                System.out.println("Total Books: " + rs.getInt(1));
            }
            else{
                System.out.println("Books not Found");
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
    }
    public void viewAvailableBooks(){
        String sql = "Select * from books where available = true";
        try{
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                System.out.println(
                    "Book ID: "+ rs.getInt("book_id") + 
                    ", Title: " + rs.getString("title")+
                    ", Author: " + rs.getString("author")
                );
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void viewIssuedBooks(){
        String sql = "Select * from books where available = false";
        try{
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                System.out.println(
                    "Book ID: "+ rs.getInt("book_id") + 
                    ", Title: " + rs.getString("title")+
                    ", Author: " + rs.getString("author")
                );
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public Book searchBookByTitle(String title){
        String sql = "select * from books where title = ?";
        try{
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1,title);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                return new Book(rs.getInt("book_id"), rs.getString("title"), rs.getString("author"), rs.getBoolean("available"));
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

}
