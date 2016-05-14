import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class test {
    public static void main(String[] args) {
        DateFormat dateFormatReturnDate = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(calendar.DATE, 5);
        String returnDate = dateFormatReturnDate.format(calendar.getTime());
        System.out.println(returnDate);
    }
}
