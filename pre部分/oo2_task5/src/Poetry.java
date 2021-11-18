public class Poetry extends OtherA
{
    private String author;

    public Poetry(String name,double price,long number,long age,String author)
    {
        super(name,price,number,age);
        this.author = author;
    }

    public String getAuthor()
    {
        return author;
    }

    public void getMessage()
    {
        System.out.printf("%s %f %d %d ",getName(),getPrice(),getNumber(),getAge());
        System.out.println(author);
    }

    @Override
    public boolean equalsOutName(Book book) {
        if (book.getClass().equals(this.getClass()))
        {
            if (Math.abs(this.getPrice() - book.getPrice()) < 1e-5)
            {
                if (((Poetry) book).getAge() == this.getAge())
                {
                    return ((Poetry) book).getAuthor().equals(this.author);
                }
            }
        }
        return false;
    }

    @Override
    public Book clone(Book book) {
        String name = book.getName();
        return new Poetry(name,book.getPrice(),book.getNumber(),((Poetry)book).getAge(),((Poetry)book).getAuthor());
    }
}
