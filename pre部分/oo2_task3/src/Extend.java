import java.util.ArrayList;
import java.util.Scanner;

public class Extend {
    public static void main(String[] args)
    {
        ArrayList<Bookset> booksetArrayList = new ArrayList<>(2000);
        Scanner scanner = new Scanner(System.in);
        int bookSetNumber = scanner.nextInt();
        for (int i = 0;i < bookSetNumber;i++) {
            Bookset bookset = new Bookset();
            booksetArrayList.add(bookset);
        }
        int operationTime = scanner.nextInt();
        for (int i = 0;i < operationTime;i++) {
            int type = scanner.nextInt();
            int attribute = scanner.nextInt();
            String name = null;
            double price = 0;
            int number = 0;
            int age = 0;
            boolean finish = false;
            String author = null;
            int year = 0;
            int grade = 0;
            String major = null;
            Bookset bookset = booksetArrayList.get(attribute - 1);
            String types;
            switch (type) {
                case 1:
                    name = scanner.next();
                    bookset.message(name);
                    break;
                case 2:
                    System.out.println(bookset.typeOfBook());
                    break;
                case 3:
                    System.out.println(bookset.sumOfBook());
                    break;
                case 4:
                    types = scanner.next();
                    name = scanner.next();
                    price = scanner.nextDouble();
                    number = scanner.nextInt();
                    switch (types) {
                        case "Other":
                            Other other = new Other(name,price,number);
                            bookset.addBook(other);
                            break;
                        case "OtherA":
                            age = scanner.nextInt();
                            OtherA otherA = new OtherA(name,price,number,age);
                            bookset.addBook(otherA);
                            break;
                        case "Novel":
                            age = scanner.nextInt();
                            finish = scanner.nextBoolean();
                            Novel novel = new Novel(name,price,number,age,finish);
                            bookset.addBook(novel);
                            break;
                        case "Poetry":
                            age = scanner.nextInt();
                            author = scanner.next();
                            Poetry poetry = new Poetry(name,price,number,age,author);
                            bookset.addBook(poetry);
                            break;
                        case "OtherS":
                            year = scanner.nextInt();
                            OtherS otherS = new OtherS(name,price,number,year);
                            bookset.addBook(otherS);
                            break;
                        case "Math":
                            year = scanner.nextInt();
                            grade = scanner.nextInt();
                            Maths maths = new Maths(name,price,number,year,grade);
                            bookset.addBook(maths);
                            break;
                        case "Computer":
                            year = scanner.nextInt();
                            major = scanner.next();
                            Computer computer = new Computer(name,price,number,year,major);
                            bookset.addBook(computer);
                            break;
                        default:
                            break;
                    }
                    break;
                case 5:
                    name = scanner.next();
                    System.out.println(bookset.remove(name));
                    break;
                default:
                    break;
            }
        }
    }

}


