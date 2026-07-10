package model;
public class Book {

    private int book_id;
    private String title;
    private String author;
    private boolean available;
    private User issued_to;
    
    public Book(int book_id, String title, String author, boolean available){
        this.book_id=book_id;
        this.title=title;
        this.author=author;
        this.available=available;
        this.issued_to=null;
    }
    public int getBookId(){
        return book_id;
    }
    public String getBooktitle(){
        return title;
    }
    public String getBookAuthor(){
         return author;
    }
    public boolean isAvailable(){
        return available;
    }
    public User getIssuedTo(){
        return issued_to;
    }
    public void setIssuedTo(User user){
        this.issued_to = user; 
    }

    @Override
    public String toString(){
        return "BookId: "+ book_id + ", Title: " + title + ", Author: " + author + ",Available: " + available;
    }

}
