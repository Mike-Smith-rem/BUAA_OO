import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

public class Container {
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        int bookSetNumber = scanner.nextInt();
        ArrayList<BookSet> bookSetArrayList = new ArrayList<>(2000);
        for (int i = 0;i < bookSetNumber;i++)
        {
            BookSet bookSet = new BookSet();
            bookSetArrayList.add(bookSet);
        }
        int operationTime = scanner.nextInt();
        for (int i = 0;i < operationTime;i++)
        {
            int type = scanner.nextInt();
            int attribute = scanner.nextInt();
            String name;
            double price;
            int number;
            BookSet bookSet = bookSetArrayList.get(attribute - 1);;
            switch (type)
            {
                case 1:
                    System.out.println(bookSet.maxPrice());
                    break;
                case 2:
                    System.out.println(bookSet.totalPrice());
                    break;
                case 3:
                    name = scanner.next();
                    price = scanner.nextDouble();
                    number = scanner.nextInt();
                    Book book = new Book(name,price,number);
                    bookSet.addBook(book);
                    break;
                case 4:
                    name = scanner.next();
                    System.out.println(bookSet.remove(name));
                    break;
                default:
                    break;
            }
        }
    }

}

class BookSet
{
    private int index;
    private ArrayList<Book> bookArrayList = new ArrayList<>(2000);

    public double maxPrice()
    {
        double max = 0;
        for (Book item : bookArrayList)
        {
            if (item.getPrice() > max)
            {
                max = item.getPrice();
            }
        }
        return max;
    }

    public BigDecimal totalPrice()
    {
        BigDecimal sum = new BigDecimal(Double.toString(0));
        for (Book item :bookArrayList)
        {
            BigDecimal price = new BigDecimal(Double.toString(item.getPrice()));
            BigDecimal number = new BigDecimal(Integer.toString(item.getNumber()));
            BigDecimal price1 = price.multiply(number);
            sum = sum.add(price1);
        }
        return sum;
    }

    public void addBook(Book book)
    {
        bookArrayList.add(book);
    }

    public int remove(String name)
    {
        int tempNumber = 0;
        for (int i = 0;i < bookArrayList.size();i++)
        {
            Book item = bookArrayList.get(i);
            tempNumber += item.getNumber();
            //System.out.println(item.getName().compareTo(name));
            if (item.getName().compareTo(name) == 0)
            {
                tempNumber -= item.getNumber();
                bookArrayList.remove(item);
                i--;
            }
        }
        return tempNumber;
    }
}

class Book
{
    private String name;
    private double price;
    private int number;

    public Book(String name,double price,int number)
    {
        this.name = name;
        this.price = price;
        this.number = number;
    }

    public String getName()
    {
        return name;
    }

    public double getPrice()
    {
        return price;
    }

    public int getNumber()
    {
        return number;
    }

    public double sum()
    {
        return number * price;
    }
}