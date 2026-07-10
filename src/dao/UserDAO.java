package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.User;
import util.DBConnection;

public class UserDAO {
    private Connection conn;

    public UserDAO(){
        conn = DBConnection.getConnection();
    }

    public void addUser(User user){
        String sql = "INSERT INTO users(user_id,name) VALUES (?,?)";
        try{
            
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setInt(1,user.getUserId());
            pst.setString(2,user.getName());

            pst.executeUpdate();
            System.out.println("User Added Successfully!!!");
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void viewUser(){
        String sql = " select * from users";
        try{
            
            PreparedStatement pst = conn.prepareStatement(sql);

            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                System.out.println(
                    "User ID: " + rs.getInt("user_id") +
                    ", Name: " + rs.getString("name")
                );
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public User searchUserById(int userId){
        String sql = "Select * from users where user_id =?";
        try{
            
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1,userId);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                return new User(
                    rs.getInt("user_id"),
                    rs.getString("name"));
            }
            

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public void deleteUser(int userId){
        
        try{

            String checksql = "select * from issued_books where user_id = ?";            
            PreparedStatement pst = conn.prepareStatement(checksql);
            pst.setInt(1,userId);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                System.out.println("Cannot delete this user. The user has issue history.");
                return;
            }
            String sql = "DELETE from users where user_id =?";
            PreparedStatement deletePst = conn.prepareStatement(sql);
            deletePst.setInt(1,userId);

            int rows=deletePst.executeUpdate();
            if(rows >0 ){
                System.out.println("User Deleted Successfully!!");
            }else{
                System.out.println("User not Found");
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void totalUsers(){
        String sql = "select count(*) from users";
        try{
            
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                System.out.println("Total Users: " + rs.getInt(1));
            }
            else{
                System.out.println("Users not Found");
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public User searchUserByName(String name){
        String sql = "select * from users where name =  ?";
        try{
            
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1,name);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                return new User(rs.getInt("user_id"),rs.getString("name"));
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
}
