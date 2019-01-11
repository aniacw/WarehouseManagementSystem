package main.data;

import main.factory.Sessions;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CSVLoader {
//    public static void loadTable(String filename, String tablename, Session session) throws FileNotFoundException {
//        File file =new File(filename);
//        FileInputStream stream = new FileInputStream(file);
//        Scanner scanner = new Scanner(stream);
//        StringBuilder builder=new StringBuilder((int)(file.length()*2));
//        String header = scanner.nextLine();
//        builder.append("INSERT INTO ");
//        builder.append(tablename);
//        builder.append("(");
//        builder.append(header);
//        builder.append(") VALUES ");
//        if (scanner.hasNext()) {
//            builder.append("(");
//            builder.append(scanner.nextLine());
//            builder.append(")");
//            while (scanner.hasNext()) {
//                builder.append(", (");
//                builder.append(scanner.nextLine());
//                builder.append(")");
//            }
//        }
//        session.beginTransaction();
//        Query query = session.createQuery(builder.toString());
//        query.executeUpdate();
//        session.getTransaction().commit();
//    }

    public static void loadTable(String filename, String tablename, Session session) throws FileNotFoundException {
        File file =new File(filename);
        FileInputStream stream = new FileInputStream(file);
        Scanner scanner = new Scanner(stream);
        String header = scanner.nextLine();
        String front = "INSERT INTO " + tablename + "(" + header + ") (";
        session.beginTransaction();
        while (scanner.hasNext()) {
            session.createQuery(front + scanner.nextLine() + ")").executeUpdate();
        }
        session.getTransaction().commit();
    }

    public static void loadProductsTable(String filename, Session session) throws FileNotFoundException {
        File file =new File(filename);
        FileInputStream stream = new FileInputStream(file);
        Scanner scanner = new Scanner(stream);
        scanner.nextLine();
        session.beginTransaction();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] fields=line.split(",");
            Product p =new Product(
                    Integer.parseInt(fields[0]),
                    fields[1],
                    fields[2],
                    Double.parseDouble(fields[3]),
                    Integer.parseInt(fields[4]));
            session.save(p);
        }
        session.getTransaction().commit();
    }

    public static void loadProductsTable(String filename) throws FileNotFoundException {
        Session session= Sessions.getSessionFactory().openSession();
        loadProductsTable(filename, session);
        session.close();
    }

    public static void loadTable(String filename, String tablename) throws FileNotFoundException {
        Session session= Sessions.getSessionFactory().openSession();
        loadTable(filename, tablename, session);
        session.close();
    }
}
