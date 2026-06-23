import java.util.Scanner;
public class Main {

    public static void main(String[] args) {

        UserDAO userDAO = new UserDAO();
        BookDAO bookDAO = new BookDAO();
        Scanner sc = new Scanner(System.in);

        while(true){
            System.out.println("\n================ Library Management System ===============");
            System.out.println("\n--------------User Management-------------------");
            System.out.println("1. Add User");
            System.out.println("2. View Users");
            System.out.println("3. Search User");
            System.out.println("4. Delete User");
            System.out.println("\n ------------------Book Management-----------------");
            System.out.println("5. Add Book");
            System.out.println("6. View Book");
            System.out.println("7. Search Book");
            System.out.println("8. Delete Book");
            System.out.println("9. Issue Book");
            System.out.println("10. Return Book");
            System.out.println("\n --------------------Exit-------------");
            System.out.println("11. Exit");

            System.out.println("Enter Choice: ");
            int choice = sc.nextInt();

            if(choice == 11){
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
                case 6:{
                    bookDAO.viewBooks();
                    break;
                }
                case 7:{
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
                case 8:{
                    System.out.println("Enter Book Id:");
                    int deleteBookId= sc.nextInt();
                    bookDAO.deleteBookById(deleteBookId);
                    break;
                }
                case 9:{
                    System.out.println("Enter Book Id:");
                    int issueBookId = sc.nextInt();
                    System.out.println("Enter User Id: ");
                    int issueUserId = sc.nextInt();
                    bookDAO.issueBook(issueBookId, issueUserId);
                    break;
                }
                case 10:{
                    System.out.println("Enter the Book Id:");
                    int returnBookId = sc.nextInt();
                    bookDAO.returnBook(returnBookId);
                    break;
                }
                case 11:{
                    System.out.println("ThankYou!!!");
                    sc.close();
                    return;
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