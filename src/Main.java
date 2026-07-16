import java.util.Scanner;

import dao.BookDAO;
import dao.IssuedBookDAO;
import dao.UserDAO;
import model.Book;
import model.User;

import java.util.List;
public class Main {

    public static void main(String[] args) {

        UserDAO userDAO = new UserDAO();
        BookDAO bookDAO = new BookDAO();
        IssuedBookDAO issuedBookDAO = new IssuedBookDAO();
        System.out.println("Database connected Successfully!!!!");
        Scanner sc = new Scanner(System.in);

        while(true){
            System.out.println("\n================ Library Management System ===============");
            System.out.println("\n--------------User Management-------------------");
            System.out.println("1. Add User");
            System.out.println("2. View Users");
            System.out.println("3. Search User By ID");
            System.out.println("4. Delete User");
            System.out.println("5. Total Users");
            System.out.println("6. Search User By Name");
            System.out.println("\n ------------------Book Management-----------------");
            System.out.println("7. Add Book");
            System.out.println("8. View Book");
            System.out.println("9. Search Book By ID");
            System.out.println("10. Update Book");
            System.out.println("11. Delete Book");
            System.out.println("12. Issue Book");
            System.out.println("13. Return Book");
            System.out.println("14. Total Books");
            System.out.println("15. Search Book By Title");
            System.out.println("16. Search Book By Author");
            System.out.println("\n---------------------------Report-------------------");
            System.out.println("17. View Available Book");
            System.out.println("18. View Currently Issued Book");
            System.out.println("19. View Issue History");
            System.out.println("20. View OverDue Books");
            System.out.println("21. TOP Readers");
            System.out.println("22. MOST ISSUED Books");
            System.out.println("23. Library Dashboard");
            System.out.println("\n --------------------Exit-------------");
            System.out.println("24. Exit");

            System.out.println("Enter Choice: ");
            int choice = sc.nextInt();

            if(choice == 24){
                break;
            }

            switch(choice){
                case 1:{
                    System.out.println("Enter User Id:");
                    int userId= sc.nextInt();
                    sc.nextLine();

                    System.out.println("Enter name: ");
                    String name = sc.nextLine();
                    userDAO.addUser(new User(userId,name));
                    break;
                }
                case 2:{
                    userDAO.viewUser();
                    break;
                }
                case 3:{
                    System.out.println("Enter User Id: ");
                    int searchId =sc.nextInt();

                    User user = userDAO.searchUserById(searchId);
                    if(user != null){
                        System.out.println(user);
                    }
                    else{
                        System.out.println("User Not Found");
                    }
                    break;
                }
                case 4:{
                    System.out.println("Enter User ID:");
                    int deleteId = sc.nextInt();
                    userDAO.deleteUser(deleteId);
                    break;
                }
                case 5:{
                    userDAO.totalUsers();
                    break;
                }
                case 6:{
                    sc.nextLine();
                    System.out.println("Enter the name of User ");
                    String name = sc.nextLine();
                    User user = userDAO.searchUserByName(name);
                    if(user != null){
                        System.out.println(user);
                    }
                    else{
                        System.out.println("User not Found");
                    }
                    break;
                }
                case 7:{
                    System.out.println("Enter Book Id: ");
                    int bookId = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Enter Title:");
                    String title = sc.nextLine();
                    System.out.println("Enter Author: ");
                    String author = sc.nextLine();

                    Book book = new Book(bookId, title, author,true);
                    bookDAO.addBook(book);
                    break;
                }
                case 8:{
                    bookDAO.viewBooks();
                    break;
                }
                case 9:{
                    System.out.println("Enter Book Id: ");
                    int searchBookId = sc.nextInt();
                    Book foundBook = bookDAO.searchBookById(searchBookId);
                    if(foundBook != null){
                        System.out.println(foundBook);
                    }
                    else{
                        System.out.println("Book Not Found");
                    }
                    break;
                }
                case 10:{
                    System.out.print("Enter Book Id:");
                    int bookId=sc.nextInt();
                    sc.nextLine();

                    System.out.println("\n What do you want to update?");
                    System.out.println("1. Title");
                    System.out.println("2. Author");
                    System.out.println("3. Both");
                    System.out.print("Enter Choice: ");

                    int updateChoice = sc.nextInt();
                    sc.nextLine();

                    String title = "";
                    String author = "";

                    if(updateChoice == 1){
                        System.out.print("Enter New Title: ");
                        title = sc.nextLine();
                    }
                    else if(updateChoice == 2){
                        System.out.println("Enter new Author: ");
                        author = sc.nextLine();
                    }
                    else if (updateChoice == 3){
                        System.out.print("Enter New Title: ");
                        title = sc.nextLine();
                        System.out.println("Enter New Author: ");
                        author = sc.nextLine();
                    }else{
                        System.out.println("Invalid Choice.");
                        break;
                    }

                    bookDAO.updateBook(bookId, updateChoice, title, author);
                    break;
                }
                case 11:{
                    System.out.println("Enter Book Id:");
                    int deleteBookId= sc.nextInt();
                    bookDAO.deleteBookById(deleteBookId);
                    break;
                }
                case 12:{
                    System.out.println("Enter Book Id:");
                    int issueBookId = sc.nextInt();
                    System.out.println("Enter User Id: ");
                    int issueUserId = sc.nextInt();
                    issuedBookDAO.issueBook(issueBookId, issueUserId);
                    break;
                }
                case 13:{
                    System.out.println("Enter the Book Id:");
                    int returnBookId = sc.nextInt();
                    issuedBookDAO.returnBook(returnBookId);
                    break;
                }
                case 14:{
                    bookDAO.totalBooks();
                    break;
                }
                case 15:{
                    sc.nextLine();
                    System.out.println("Enter the title of Book: ");
                    String title = sc.nextLine();
                    Book book = bookDAO.searchBookByTitle(title);
                    if(book != null){
                        System.out.println(book);
                    }
                    else{
                        System.out.println("Book not Found");
                    }
                    break;
                }
                case 16:{
                    sc.nextLine();
                    System.out.println("Enter the author of Book: ");
                    String author = sc.nextLine();
                    List<Book> books = bookDAO.searchBookByAuthor(author);
                    if(books.isEmpty()){
                        System.out.println("Book not Found");
                    }else{
                        for(Book book :books){
                            System.out.println(book);
                        }
                    }
                    break;

                }
                case 17:{
                    bookDAO.viewAvailableBooks();
                    break;
                }
                case 18:{
                    issuedBookDAO.viewCurrentlyIssuedBooks();
                    break;
                }
                case 19:{
                    issuedBookDAO.viewIssuedBooksHistory();
                    break;

                }
                case 20:{
                    issuedBookDAO.viewOverDueBooks();
                    break;
                }
                case 21:{
                    issuedBookDAO.topReaders();
                    break;
                }
                case 22:{
                    issuedBookDAO.mostIssuedBooks();
                    break;
                }
                case 23:{
                    issuedBookDAO.libraryDashBoard();
                    break;

                }
                
                default:{
                    System.out.println("Invalid Choice");
                    break;
                }
            }
        }
        sc.close();
    }
}