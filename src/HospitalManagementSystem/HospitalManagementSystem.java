package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url="jdbc:mysql://localhost:3306/hospital";
    private static final String username="root";
    private static final String password="Aakarsh000";

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        Scanner scn=new Scanner(System.in);
        try {
            Connection connection= DriverManager.getConnection(url,username,password);
            Patient patient=new Patient(connection,scn);
            Doctor doctor=new Doctor(connection);

            while (true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. ADD PATIENTS");
                System.out.println("2. VIEW PATIENTS");
                System.out.println("3. VIEW DOCTORS");
                System.out.println("4. BOOK APPOINTMENT");
                System.out.println("5. EXIT");
                System.out.println("ENTER YOUR CHOICE");
                int choice=scn.nextInt();

                switch (choice){
                    case 1:
                        //ADD PATIENT
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        //VIEW PATIENT
                        patient.viewPatient();
                        System.out.println();break;
                    case 3:
                        //VIEW DOCTORS
                        doctor.viewDoctor();
                        System.out.println();
                        break;
                    case 4:
                        //BOOK APPOINTMENT
                        bookAppointment(patient,doctor,connection,scn);
                        System.out.println();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("ENTER VALID CHOICE !");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public  static void bookAppointment(Patient patient,Doctor doctor,Connection connection,Scanner scn){
        System.out.print("ENTER PATIENT ID: ");
        int patientId=scn.nextInt();
        System.out.print("ENTER DOCTOR ID: ");
        int doctorId=scn.nextInt();
        System.out.print("ENTER APPOINTMENT DATE (YYYY-MM-DD)");
        String appointmentDate=scn.next();
        if (patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
            if(checkDoctorAvailability(doctorId, appointmentDate, connection)){
                String appointmentQuery="INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES(?,?,?)";
                try{
                    PreparedStatement preparedStatement=connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);
                    int rowsAffected=preparedStatement.executeUpdate();
                    if (rowsAffected>0){
                        System.out.println("Appointment booked");
                    }else{
                        System.out.println("Failed to book appointment");
                    }
                }catch (SQLException e){
                    e.printStackTrace();;
                }
            }else{
                System.out.println("Doctor not available on this Date !");
            }
        }
        else{
            System.out.println("Either Doctor or Patient doesn't exists !!");
        }
    }

    public static boolean checkDoctorAvailability(int doctorId, String appointmentDate,Connection connection ){
        String query="SELECT COUNT(*) FROM appointments WHERE doctor_id= ? AND appointment_date= ?";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);
            ResultSet resultSet=preparedStatement.executeQuery();
            if (resultSet.next()){
                int count= resultSet.getInt(1);
                if (count==0){
                    return true;
                }else{
                    return false;
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return  false;
    }
}
