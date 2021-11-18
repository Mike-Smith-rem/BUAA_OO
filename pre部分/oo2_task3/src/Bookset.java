import java.util.ArrayList;
import java.util.Arrays;

public class Bookset {
//    *
//    public static final int bookOther = 0;
//    public static final int bookOtherA = 1;
//    public static final int bookNovel = 2;
//    public static final int bookPoetry = 3;
//    public static final int bookOtherS = 4;
//    public static final int bookMath = 5;
//    public static final int bookComputer = 6;
//     *

    private ArrayList<Book> bookArrayList = new ArrayList<>(2000);

    @SuppressWarnings("checkstyle:MethodParamPad")
    public void addBook(Book book)
    {
        bookArrayList.add(book);
    }

    public long remove(String name)
    {
        long tempNumber = 0;
        for (int i = 0;i < bookArrayList.size();i++)
        {
            Book item = bookArrayList.get(i);
            tempNumber += item.getNumber();
            if (item.getName().compareTo(name) == 0)
            {
                tempNumber -= item.getNumber();
                bookArrayList.remove(item);
                i--;
            }
        }
        return tempNumber;
    }

    public int typeOfBook()
    {
        int number = 0;
        int[] flag = new int[7];
        Arrays.fill(flag, 1);
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
        return number;
    }

    public long sumOfBook()
    {
        long tempNumber = 0;
        for (Book item : bookArrayList) {
            tempNumber += item.getNumber();
        }
        return tempNumber;
    }

    public void message(String name)
    {
        int index = 0;
        for (int i = 0;i < bookArrayList.size();i++)
        {
            Book book = bookArrayList.get(i);
            if (book.getName().equals(name))
            {
                index = i;
                break;
            }
        }
        Book book = bookArrayList.get(index);
        book.getMessage();
    }

}
