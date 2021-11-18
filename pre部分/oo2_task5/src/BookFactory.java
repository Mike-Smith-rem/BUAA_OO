import java.util.ArrayList;
import java.util.Scanner;

public class BookFactory {
    private static final ArrayList<Bookset> booksetArrayList = new ArrayList<>(2000);

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        int bookSetNumber = scanner.nextInt();
        for (int i = 0;i < bookSetNumber;i++)
        {
            Bookset bookset = new Bookset();
            booksetArrayList.add(bookset);
        }
        int operationTime = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0;i < operationTime;i++)
        {
            String string = scanner.nextLine();
            String[] strings = string.split(" ");
            int type = Integer.parseInt(strings[0]);
            int attribute = Integer.parseInt(strings[1]);
            Bookset bookset = booksetArrayList.get(attribute - 1);
            switch (type) {
                case 1:
                    bookset.message(strings[2]);
                    break;
                case 2:
                    bookset.typeOfBook();
                    break;
                case 3:
                    bookset.sumOfBook();
                    break;
                case 4:
                    String types = strings[2];
                    String name = strings[3];
                    double price = Double.parseDouble(strings[4]);
                    long number = Long.parseLong(strings[5]);
                    switch (types) {
                        case "Other":
                            Other other = new Other(name, price, number);
                            bookset.addBook(other);
                            break;
                        case "OtherA":
                            long age = Long.parseLong(strings[6]);
                            OtherA otherA = new OtherA(name, price, number, age);
                            bookset.addBook(otherA);
                            break;
                        case "Novel":
                            age = Long.parseLong(strings[6]);
                            boolean finish = Boolean.parseBoolean(strings[7]);
                            Novel novel = new Novel(name, price, number, age, finish);
                            bookset.addBook(novel);
                            break;
                        case "Poetry":
                            age = Long.parseLong(strings[6]);
                            String author = strings[7];
                            Poetry poetry = new Poetry(name, price, number, age, author);
                            bookset.addBook(poetry);
                            break;
                        case "OtherS":
                            long year = Long.parseLong(strings[6]);
                            OtherS otherS = new OtherS(name, price, number, year);
                            bookset.addBook(otherS);
                            break;
                        case "Math":
                            year = Long.parseLong(strings[6]);
                            long grade = Long.parseLong(strings[7]);
                            Maths maths = new Maths(name, price, number, year, grade);
                            bookset.addBook(maths);
                            break;
                        case "Computer":
                            year = Long.parseLong(strings[6]);
                            String major = strings[7];
                            Computer computer = new Computer(name, price, number, year, major);
                            bookset.addBook(computer);
                            break;
                        default:
                            break;
                    }
                    break;
                case 5:
                    bookset.remove(strings[2]);
                    break;
                case 6:
                    int var2 = Integer.parseInt(strings[2]);
                    Compares(attribute,var2);
                    break;
                default:
                    break;
            }
        }
    }

    public static  void Compares(int var1, int var2)
    {
        ArrayList<Book> bookset1 = booksetArrayList.get(var1 - 1).getBookArrayList();
        ArrayList<Book> bookset2 = booksetArrayList.get(var2 - 1).getBookArrayList();
        Bookset newBookSet = new Bookset();
        for (Book book : bookset1)
        {
            Book newBook = book.clone(book);
            newBookSet.getBookArrayList().add(book);
        }
        if (newBookSet.getBookArrayList().size() != 0) {
            for (Book book : bookset2) {
                int flag = 0;
                for (Book book1 : bookset1) {
                    if (book1.equalsOutName(book) && book1.getName().equals(book.getName())) {
                        newBookSet.getBookArrayList().remove(book1);
                        newBookSet.addBooks(book1, book);
                        flag = 1;
                        break;
                    } else if (!book1.equalsOutName(book) && book1.getName().equals(book.getName())) {
                        System.out.println("Oh, no. We fail!");
                        return;
                    }
                }
                if (flag == 0)
                {
                    newBookSet.getBookArrayList().add(book);
                }
            }
        }
        else
        {
            for (Book book : bookset2) {
                newBookSet.getBookArrayList().add(book);
            }
        }
        booksetArrayList.add(newBookSet);
        System.out.println(booksetArrayList.size());
    }
}
