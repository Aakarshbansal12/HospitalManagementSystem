package HospitalManagementSystem;

import com.mysql.cj.xdevapi.PreparableStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;

    private Scanner scn;

    public Patient(Connection connection, Scanner scn){
        this.connection=connection;
        this.scn=scn;

    }
    public void addPatient(){
        System.out.print("Enter Patient Name");
        String name=scn.next();
        System.out.print("Enter Patient Age");
        int age=scn.nextInt();
        System.out.print("Enter Patient Gender");
        String gender=scn.next();

        try {
            String query="INSERT INTO patients(name,age,gender) VALUES(?,?,?)";
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,age);
            preparedStatement.setString(3,gender);
            int affectRows=preparedStatement.executeUpdate();
            if (affectRows>0){
                System.out.println("Patient Added Successfully");
            }else{
                System.out.println("Failed to Add Patient");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void viewPatient(){
        String query="select * from patients";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            ResultSet resultSet=preparedStatement.executeQuery();
            System.out.println("Patients ");
            System.out.println("+------------+--------------------+------------+----------------+");
            System.out.println("| Patient Id | Name               | Age        | Gender         |");
            System.out.println("+------------+--------------------+------------+----------------+");

            while(resultSet.next()){
               int id =resultSet.getInt("id");
               String name=resultSet.getString("name");
               int age=resultSet.getInt("age");
               String gender=resultSet.getString("gender");

                System.out.printf("|%-12s|%-20s|%-12s|%-16s|\n",id,name,age,gender);
                System.out.println("+------------+--------------------+------------+----------------+");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int id){
        String query="select * from patients where id=?";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }else {
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
