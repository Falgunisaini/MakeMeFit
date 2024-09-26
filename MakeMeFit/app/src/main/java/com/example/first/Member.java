package com.example.first;
import java.util.HashMap;
import java.util.Map;
public class Member {
    private String username;
    private String email;
    private String gender;
    private float weight;
    private float height;
    private int age;
    private int stepcounter;
    private int stepgoal;
    private float bmi;
    private float  calories;

    public HashMap<String, Object> getsleep() {
        return sleep;
    }
    public void setSleep(HashMap<String, Object> sleep) { this.sleep = sleep; }
    private HashMap<String,Object> sleep;

    public HashMap<String, Object> getwater() {
        return water;
    }
    public void setWater(HashMap<String, Object> sleep) { this.water = water; }
    private HashMap<String,Object> water;

    public HashMap<String, Object> getcurrcal() {
        return currcal;
    }
    public void setcurrcal(HashMap<String, Object> cal) {
        this.currcal = cal;
    }
    private HashMap<String,Object> currcal;

    public void setCalories() {

        int stepsinmile;
        if(height<=153)
            stepsinmile = 2556;
        else if(height<165)
            stepsinmile=2360;
        else if(height<172.72)
            stepsinmile=2256;
        else if(height<182.88)
            stepsinmile=2130;
        else
            stepsinmile=2045;

        float val;
        val= Float.parseFloat(Double.toString((0.57 * weight * 2.205)/stepsinmile));
        this.calories= val;
    }

    public float getCalories() {
        return calories;
    }

    private static int id=0;
    protected static String currmail;
    protected static String currpass;
    protected static String currid;
    //private static int water;
    //private static int sleep;
    private Map<String, String> timestamp;
    public HashMap<String, Object> getsteps() {
        return steps;
    }
    public void setsteps(HashMap<String, Object> steps) {
        this.steps = steps;
    }
    private HashMap<String,Object> steps;
    public void setTimestamp(Map<String, String> timeStamp) {this.timestamp=
            timestamp;}
    public Map<String, String> getTimestamp() {return timestamp;}
    public Member(){}
    public static int getId() {
        return id;
    }
    /*
    public static int getWater() {
        return water;
    }

   public static void setWater(int water) {
        Member.water = WaterActivity.wcount;
    }
    public static int getSleep() {
       return sleep;
   }
   public static void setSleep(int sleep) {
       Member.sleep = SleepActivity.scount;
   }
    */

    public static void setId(int id) {
        Member.id = id;
    }
    public String inttoString(int id) {
        return Integer.toString(Member.id);
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }


    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;

    }
    public float getWeight() {
        return weight;
    }
    public void setWeight(String weight) {
        this.weight = Float.parseFloat(weight);
    }
    public float getHeight() {
        return height;
    }
    public void setHeight(String height) {
        this.height = Integer.parseInt(height);
    }
    public int getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = Integer.parseInt(age);
    }
    public float getstepcounter() {
        return stepcounter;
    }
    public int getStepgoal() {
        return stepgoal;
    }
    public float getBmi() {
        return bmi;
    }
    public void setBmi() {
        bmi = weight / ((height / 100) * (height / 100));
        bmi= (float) (Math.round(bmi*100.0)/100.0);
    }
    public void setStepgoal() {
        if (age <= 4 && age <= 6) {
            if (18.5 < bmi && bmi < 25)
                stepcounter = 12000;
            else if (bmi < 18.5)
                stepcounter = 1000;
            else

            stepcounter = 14000;
        }
        else {
            if (6 < age && age<=11 && gender=="Female")
            { if (18.5 < bmi && bmi < 25)
                stepcounter = 11000;
            else if (bmi < 18.5)
                stepcounter = 9000;
            else
                stepcounter = 12000; }
            else if (6 < age && age<=11 && gender=="Male")
            { if (18.5 < bmi && bmi < 25)
                stepcounter = 14000;
            else if (bmi < 18.5)
                stepcounter = 12000;
            else
                stepcounter = 16000; }
            else if(age>11 && age<=19){
                if (18.5 < bmi && bmi < 25)
                    stepcounter = 12000;
                else if (bmi < 18.5)
                    stepcounter = 10000;
                else
                    stepcounter = 15000;
            }
            else if(age>19 && age<45)
            { if (18.5 < bmi && bmi < 25)
                stepcounter = 10000;
            else if (bmi < 18.5)
                stepcounter = 8000;
            else
                stepcounter = 13000;
            }
            else
            {
                if (18.5 < bmi && bmi < 25)
                    stepcounter = 9000;
                else if (bmi < 18.5)
                    stepcounter = 7000;
                else
                    stepcounter = 10000;
            }
        }
        this.stepgoal = stepcounter;
    }
}


      /*  Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

    //    HashMap<String,Object>  calmap = new HashMap<>();
        float calburnt;
      //  calburnt= Float.parseFloat(String.valueOf(((0.57 * weight * 2.205)/stepsinmile)* Integer.parseInt(steps.get(formattedDate).toString())));
       // calmap.put(formattedDate,calburnt);
       // setCalories(calmap);
*/