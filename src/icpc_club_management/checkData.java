package icpc_club_management;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class checkData {
    
final String nameP="^[a-zA-Z]*$";
final String numberP="[0-9]+";
final String emailP= "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
final String timeP="(1[012]|[1-9]):[0-5][0-9](\\s)?(?i)";
Pattern pattern;
Matcher matcher;
protected boolean checkName(String name){
    pattern = Pattern.compile(nameP);
    matcher = pattern.matcher(name);
    return matcher.matches();
}
protected boolean checkNumber(String number){
    pattern = Pattern.compile(numberP);
    matcher = pattern.matcher(number);
    boolean bool=matcher.matches()&&number.startsWith("09")&&number.length()==10;
    return bool;
}
protected boolean checkEmail(String email){
    pattern = Pattern.compile(emailP);
    matcher = pattern.matcher(email);
    return matcher.matches();
}
protected boolean checkTime(String time){
    pattern = Pattern.compile(timeP);
    matcher = pattern.matcher(time);
    return matcher.matches();
}
    
}
