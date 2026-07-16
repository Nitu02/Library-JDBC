package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import model.Book;
import util.DBConnection;

import java.util.ArrayList;

public class BookDAO {

    private Connection conn;

    public BookDAO(){
        conn =DBConnection.getConnection();
    }

    

    public void addBook(Book book){
        String sql = "Insert into books(book_id,title,author,available) values(?,?,?,?) ";
        try{
            
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
        
        try{
            String checksql ="select * from issued_books where book_id = ?";
            
            PreparedStatement  pst = conn.prepareStatement(checksql);
            pst.setInt(1,book_id);
            
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                System.out.println("Cannot delete this book. It has issue history.");
                return;
            }

            String deletesql = "Delete from books where book_id = ?";
            PreparedStatement pst1 = conn.prepareStatement(deletesql);
            pst1.setInt(1,book_id);
            int rows = pst1.executeUpdate();
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
    public List<Book> searchBookByAuthor(String author){
        List<Book> books= new ArrayList<>();
        String sql = "select * from books where author like ?";
        try{
            
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1,"%" +author+"%");
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                books.add(new Book(rs.getInt("book_id"), rs.getString("title"), rs.getString("author"), rs.getBoolean("available")));
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return books;

    }
    public void updateBook(int bookId, int choice, String value1, String value2){
        try{
            Connection conn = DBConnection.getConnection();
            String checkSql = "select available from books where book_id = ?";

            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, bookId);

            ResultSet rs = checkStmt.executeQuery();
            if(!rs.next()){
                System.out.println("Book Not Found.");
                return;
            }
            if(!rs.getBoolean("available")){
                System.out.println("Book is currently issued.");
                System.out.println("Return the book before updating.");
                return;
            }
            String sql = "";
            if(choice == 1){
                sql = "Update books set title = ? where book_id = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, value1);
                pst.setInt(2, bookId);

                pst.executeUpdate();
                System.out.println("Book Authr Updated Successfully!!!");

            }
            else if(choice == 2){
                sql = "update books set author = ? where book_id =?";

                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, value1);
                pst.setInt(2, bookId);

                pst.executeUpdate();

                System.out.println("Book Author Updated Successfully!!!!!!!!!");
            }
            else if(choice == 3){
                sql = "update books set title = ?, author = ? where book_id = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, value1);
                pst.setString(2, value2);
                pst.setInt(3, bookId);

                pst.executeUpdate();
                System.out.println("Book Details Updated Successfully!");
            }
            else{
                System.out.println("Invalid Choice");
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
