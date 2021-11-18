import java.util.Scanner;

public class Bookset {
    @SuppressWarnings("checkstyle:FallThrough")
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.next();
        double price = scanner.nextDouble();
        int number = scanner.nextInt();
        int time = scanner.nextInt();
        Book book = new Book(name,price,number);
        for (int i = 0;i < time;i++) {
            int j = scanner.nextInt();
            switch (j) {
                case 1:
                    System.out.println(book.getName());
                    break;
                case 2:
                    System.out.println(book.getPrice());
                    break;
                case 3:
                    System.out.println(book.getNumber());
                    break;
                case 4:
                    name = scanner.next();
                    break;
                case 5:
                    price = scanner.nextDouble();
                    break;
                case 6:
                    number = scanner.nextInt();
                    break;
                case 7:
                    System.out.println(book.sum());
                    break;
                default:
                    break;
            }
            book = new Book(name,price,number);
        }
    }

    static class Book
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
}

