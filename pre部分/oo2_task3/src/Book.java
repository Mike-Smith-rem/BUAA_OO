public class Book {
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

    @SuppressWarnings("checkstyle:WhitespaceAround")
    public void getMessage()
    {
        System.out.printf("%s %f %d%n", name, price, number);
    }
}
