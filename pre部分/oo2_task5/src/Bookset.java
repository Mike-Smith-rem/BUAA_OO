import java.util.ArrayList;
import java.util.Arrays;

public class Bookset {
    private ArrayList<Book> bookArrayList = new ArrayList<>();

    public ArrayList<Book> getBookArrayList()
    {
        return bookArrayList;
    }


    public void addBook(Book book)
    {
        try {
            for (Book book1 : bookArrayList)
            {
                if (book1.getName().equals(book.getName()))
                {
                    throw new ExsitException(book.getName());
                }
            }
            bookArrayList.add(book);
        } catch (ExsitException e)
        {
            System.out.println(e.getReason());
        }
    }

    public void remove(String name)
    {
        long tempNumber = 0;
        int flag = 0;
        try {
            for (int i = 0; i < bookArrayList.size(); i++) {
                Book item = bookArrayList.get(i);
                tempNumber += item.getNumber();
                if (item.getName().compareTo(name) == 0) {
                    tempNumber -= item.getNumber();
                    bookArrayList.remove(item);
                    i--;
                    flag = 1;
                }
            }
            if (flag == 0) {
                throw new NullNameException();
            }
            else {
                System.out.println(tempNumber);
            }
        } catch (NullNameException e)
        {
            System.out.println(e.getReason());
        }
    }

    public void typeOfBook() {
        long number = 0;
        int[] flag = new int[7];
        Arrays.fill(flag, 1);
        try {
            if (bookArrayList.size() == 0) {
                throw new NullBooksetException();
            } else {
                for (Book item : bookArrayList) {
                    if (item.getClass() == Novel.class && flag[0] == 1) {
                        flag[0] = 0;
                        number++;
                    } else if (item.getClass() == Poetry.class && flag[1] == 1) {
                        flag[1] = 0;
                        number++;
                    } else if (item.getClass() == Maths.class && flag[2] == 1) {
                        flag[2] = 0;
                        number++;
                    } else if (item.getClass() == Computer.class && flag[3] == 1) {
                        flag[3] = 0;
                        number++;
                    } else if (item.getClass() == OtherS.class && flag[4] == 1) {
                        flag[4] = 0;
                        number++;
                    } else if (item.getClass() == OtherA.class && flag[5] == 1) {
                        flag[5] = 0;
                        number++;
                    } else if (item.getClass() == Other.class && flag[6] == 1) {
                        flag[6] = 0;
                        number++;
                    }
                }
                System.out.println(number);
            }
        } catch (NullBooksetException e)
        {
            System.out.println(e.getReason());
        }
    }

    public void sumOfBook()
    {
        long tempNumber = 0;
        try {
            if (bookArrayList.size() == 0)
            {
                throw new NullBooksetException();
            }
            else {
                for (Book item : bookArrayList) {
                    tempNumber += item.getNumber();
                }
                System.out.println(tempNumber);
            }
        } catch (NullBooksetException e)
        {
            System.out.println(e.getReason());
        }
    }

    public void message(String name)
    {
        int index = 0;
        int flag = 0;
        try {
            if (bookArrayList.size() == 0) {
                throw new NullBooksetException();
            } else {
                for (int i = 0; i < bookArrayList.size(); i++) {
                    Book book = bookArrayList.get(i);
                    if (book.getName().equals(name)) {
                        index = i;
                        flag = 1;
                        break;
                    }
                }
                if (flag == 1) {
                    Book book = bookArrayList.get(index);
                    book.getMessage();
                } else {
                    throw new UnExistException(name);
                }
            }
        } catch (NullBooksetException e)
        {
            System.out.println(e.getReason());
        } catch (UnExistException e)
        {
            System.out.println(e.getReason());
        }
    }

    public void addBooks(Book item1,Book item2)
    {
        long number = item1.getNumber() + item2.getNumber();
        if (item1.getClass().equals(Other.class))
        {
            Other item = new Other(item1.getName(),item1.getPrice(),number);
            bookArrayList.add(item);
        }
        else if (item1.getClass().equals(OtherA.class))
        {
            OtherA item = new OtherA(item1.getName(),item1.getPrice(),number,((OtherA) item1).getAge());
            bookArrayList.add(item);
        }
        else if (item1.getClass().equals(OtherS.class))
        {
            OtherS item = new OtherS(item1.getName(),item1.getPrice(),number,((OtherS) item1).getYear());
            bookArrayList.add(item);
        }
        else if (item1.getClass().equals(Maths.class))
        {
            long grade = ((Maths)item1).getGrade();
            Maths item = new Maths(item1.getName(),item1.getPrice(),number,((Maths) item1).getYear(),grade);
            bookArrayList.add(item);
        }
        else if (item1.getClass().equals(Computer.class))
        {
            String str = ((Computer) item1).getMajor();
            Computer item = new Computer(item1.getName(),item1.getPrice(),number,((Computer) item1).getYear(),str);
            bookArrayList.add(item);
        }
        else if (item1.getClass().equals(Novel.class))
        {
            boolean str = ((Novel) item1).isFinish();
            Novel item = new Novel(item1.getName(),item1.getPrice(),number, ((Novel) item1).getAge(),str);
            bookArrayList.add(item);
        }
        else if (item1.getClass().equals(Poetry.class))
        {
            String str = ((Poetry) item1).getAuthor();
            Poetry item = new Poetry(item1.getName(),item1.getPrice(),number,((Poetry) item1).getAge(),str);
            bookArrayList.add(item);
        }
    }
}
