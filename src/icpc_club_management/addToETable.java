package icpc_club_management;

public class addToETable {
    private String title;
    private String date;
    private String place;
    private String time;
    
    addToETable(){
        this.title="";
        this.date="";
        this.place="";
        this.time="";
    }
    addToETable(String title,String date,String place,String time){
        this.title=title;
        this.date=date;
        this.place=place;
        this.time=time;
     }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

  
    
}
