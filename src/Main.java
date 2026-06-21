public class Main {

    public static void main(String[] args) {

        UserDAO userDAO = new UserDAO();
        User user = new User(1,"Aarti");

         userDAO.addUser(user);
        userDAO.viewUser();
        User user1 = userDAO.searchUserById(1);
        if(user1!= null){
            System.out.println(user1);
        }
        else{
            System.out.println("User not Found");
        }
        userDAO.deleteUser(1);

        DBConnection.getConnection();

    }
}