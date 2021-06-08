
package icpc_club_management;


public class addToTable {
    private String id;
    private String fname;
    private String lname;
    private String sex;
    private String year;
    private String access;
    
    addToTable(){
        this.id="";
        this.fname="";
        this.lname="";
        this.sex="";
        this.year="";
        this.access="";
    }
    addToTable(String id,String fname,String lname){
        this.id=id;
        this.fname=fname;
        this.lname=lname;
    }
    addToTable(String id,String fname,String lname,String sex,String year,String access){
        this.id=id;
        this.fname=fname;
        this.lname=lname;
        this.sex=sex;
        this.year=year;
        this.access=access;
     }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }
}

    

