package mbh.prjt.ba7thver001;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class GetTime {

    private Long timestamp;
    private String timeKey;
    /*private Calendar calendar;
    private boolean late;
    private long elapsedDays;
    private long elapsedHours;
    private long elapsedMinutes;*/

    public GetTime(){}

    public interface TimeCallback {
        void timecallback(Long value,String key);
    }

    /*public void getServerTime() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference adminRef = database.getReference();
        DatabaseReference timeRef= adminRef.child("timestamp").push();

        timeRef.setValue(ServerValue.TIMESTAMP);

        timeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                timeKey=snapshot.getKey();
                timestamp= (Long) snapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/

    public void deleteTimestamp(String timeKey,DatabaseReference myRef){
        myRef.child("timestamp").child(timeKey).removeValue();
    }

    public void getServerTimeWithCall(TimeCallback TimeCallback){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        DatabaseReference timeRef= myRef.child("timestamp").push();

        timeRef.setValue(ServerValue.TIMESTAMP);

        timeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                timeKey=snapshot.getKey();
                timestamp = (Long) snapshot.getValue();

                TimeCallback.timecallback(timestamp, timeKey);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public Timestamp convertFromStringToTimestamp(String str) {
        Date date;
        Timestamp time=null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.FRANCE);

        try {
            date = sdf.parse(str);
            time = new java.sql.Timestamp(date.getTime());

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return time;
    }

    public String convertFromTimeToString(Long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(timestamp);

        return (cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH) + 1) + "/"
                + cal.get(Calendar.DAY_OF_MONTH) + " - " + cal.get(Calendar.HOUR_OF_DAY) + ":"
                + cal.get(Calendar.MINUTE));
    }

    /*public void calculateDifrence(String endDay) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference adminRef = database.getReference();
        Calendar calendar=convertFromStringToCalendar(endDay);
        Long endTimeinmili=calendar.getTimeInMillis();

        getServerTimeWithCall(new TimeCallback() {
            @Override
            public void timecallback(Calendar value,String key) {
                if(calendar.compareTo(value)>0) {
                    late=false;
                    calculation(endTimeinmili,value.getTimeInMillis());
                    adminRef.child("timestamp").child(key).removeValue();
                }
                else{
                    late=true;
                    elapsedDays=0L;
                    elapsedHours=0L;
                    elapsedMinutes=0L;
                }
            }
        });
    }




    private void calculation(Long end,Long current) {

        Long diffTimeInMili = 0L;

        diffTimeInMili = end - current;

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        elapsedDays = diffTimeInMili / daysInMilli;
        diffTimeInMili = diffTimeInMili % daysInMilli;
        elapsedHours = diffTimeInMili / hoursInMilli;
        diffTimeInMili = diffTimeInMili % hoursInMilli;
        elapsedMinutes = diffTimeInMili / minutesInMilli;
        diffTimeInMili = diffTimeInMili % minutesInMilli;
    }*/

    public Long getTimestamp() {
        return timestamp;
    }

   /* public Calendar getCalendar() {
        return calendar;
    }

    public boolean isLate() {
        return late;
    }

    public long getElapsedDays() {
        return elapsedDays;
    }

    public long getElapsedHours() {
        return elapsedHours;
    }

    public long getElapsedMinutes() {
        return elapsedMinutes;
    }*/

    public String getTimeKey() {
        return timeKey;
    }
}
