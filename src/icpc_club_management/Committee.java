package icpc_club_management;

import static icpc_club_management.ICPC_Club_Management.mainScene;
import static icpc_club_management.ICPC_Club_Management.window;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.security.crypto.bcrypt.BCrypt;
import static org.springframework.security.crypto.bcrypt.BCrypt.gensalt;

public class Committee extends Helper{
    private databaseConnection dataBase=new databaseConnection();
    displayError errorMsg=new displayError();
    private checkData checkData=new checkData();
    private static String idC;
    private static String access="Member";
    Stage profileWindow=new Stage();
    Stage passWindow=new Stage();
    Rectangle2D visualBounds=Screen.getPrimary().getVisualBounds();
    
    public Scene committePage(String i){
    idC=i;
    Scene adminScene;
    Pane pane=new Pane();
    TabPane searchTab=new TabPane();
    searchTab.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    searchTab.setPrefWidth(700);
    Tab member=new Tab("Members");
    Tab profile=new Tab("Profile");
    Tab event=new Tab("Events");
    member.setContent(memberTab());
    profile.setContent(profileTab(idC));
    event.setContent(eventTab());
    
    searchTab.getTabs().addAll(member,profile,event);
     
    pane.setLayoutX(0);
    pane.setLayoutY(0);
    pane.getChildren().addAll(searchTab);
    adminScene=new Scene(pane,700,650);
    adminScene.getStylesheets().addAll("Light.css");
return adminScene;}

private VBox memberTab(){
    //Nodes
    final CheckBox allBox=new CheckBox("All");
    final TextField fname=new TextField();
    final TextField lname=new TextField();
    final TextField id=new TextField();
    final CheckBox sexBox=new CheckBox();
    final ComboBox<String> sex=new ComboBox();
    final CheckBox yearBox=new CheckBox();
    final ComboBox<String> yearCombo=new ComboBox<>();
    final Button searchBtn=new Button("Search");
    final Button clearBtn=new Button("Clear");
    final Button backBtn =new Button("Logout");
    final Button briefBtn=new Button("Brief");
    
    //set prompt text
    id.setPromptText("Id Number");
    fname.setPromptText("First Name");
    lname.setPromptText("Last Name");
    sex.setPromptText("Sex");
    yearCombo.setPromptText("Academic Year");
      
   
    sex.setDisable(true);
    yearCombo.setDisable(true);
    //Creating table
    TableView<addToTable> table=new TableView();
    TableColumn<addToTable,String> iCol=new TableColumn("Id");
    TableColumn<addToTable,String> fCol=new TableColumn("First Name");
    TableColumn<addToTable,String> lCol=new TableColumn("Last Name");
    TableColumn<addToTable,String> sCol=new TableColumn("Sex");
    TableColumn<addToTable,String> yCol=new TableColumn("Year");
    
    iCol.setCellValueFactory(new PropertyValueFactory("id"));
    iCol.setMinWidth(100);
    fCol.setCellValueFactory(new PropertyValueFactory("fname"));
    fCol.setMinWidth(100);
    lCol.setCellValueFactory(new PropertyValueFactory("lname"));
    lCol.setMinWidth(100);
    sCol.setCellValueFactory(new PropertyValueFactory("sex"));
    sCol.setMinWidth(100);
    yCol.setCellValueFactory(new PropertyValueFactory("year"));
    yCol.setMinWidth(100);
    
    table.setMaxWidth(500);
    table.setPrefHeight(430);
    table.setMaxHeight(430);
    table.getColumns().addAll(iCol,fCol,lCol,sCol,yCol);
     
    //Adding values to comboxs
    sex.getItems().addAll("Female","Male");
    yearCombo.getItems().addAll("1","2","3","4","5");
    //Installing tooltip for a button
    briefBtn.setTooltip(new Tooltip("Show additional information\nabout selected user"));
    //Fetching data from database
    try {
        Statement stmt= dataBase.getConnection().createStatement();
        ResultSet rs;
        rs=stmt.executeQuery("select * from members order by fname,lname");
        clearTable(table);
        while(rs.next()){
            String i=rs.getString(1);
            String fn=rs.getString(2);
            String ln=rs.getString(3);
            String s=rs.getString(5);
            String y=rs.getString(7);
            int a=rs.getInt(10);
            if(a==1){access="Committe";}
            else if(a==2){continue;}
            else{access="Member";}
            table.getItems().addAll(addToMTable(i,fn,ln,s,y,access));
            }
            table.setPlaceholder(new Label("No data found"));
            rs.close();
     } catch (SQLException ex) {
            errorMsg.errorMessage("Error","Database connection failed.Try again");
     }
    //Setting nodes to action   
    allBox.selectedProperty().addListener(e->{
        if(allBox.isSelected()){
            id.setDisable(true);    id.clear();
            fname.setDisable(true); fname.clear();
            lname.setDisable(true); lname.clear();
            sex.setDisable(true);   
            sexBox.setDisable(true);
            sexBox.setSelected(false);
            yearBox.setDisable(true);
            yearCombo.setDisable(true);
        }
        if(!allBox.isSelected()){
            id.setDisable(false);
            fname.setDisable(false);
            lname.setDisable(false);
            sexBox.setDisable(false);
            yearBox.setDisable(false);
        }
    });
    id.focusedProperty().addListener((oV,oldV,newV)->{
        allBox.setSelected(false);
        allBox.setDisable(true);
        fname.setDisable(true);
        lname.setDisable(true);
        sex.setDisable(true);
        sexBox.setDisable(true);
        sexBox.setSelected(false);
        yearBox.setDisable(true);
        yearBox.setSelected(false);
        yearCombo.setDisable(true);
        if(!newV){
            allBox.setDisable(false);
            fname.setDisable(false);
            lname.setDisable(false);
            sexBox.setDisable(false);
            yearBox.setDisable(false);
        }
    });
    sexBox.selectedProperty().addListener(e->{
        if(sexBox.isSelected()){
            sex.setDisable(false);
        }
        if(!sexBox.isSelected()){
            sex.setDisable(true);
        }
    });
    yearBox.selectedProperty().addListener(e->{
        if(yearBox.isSelected()){
            yearCombo.setDisable(false);
        }
        if(!yearBox.isSelected()){
            yearCombo.setDisable(true);
        }
    });
    searchBtn.setOnAction(e->{
       if(allBox.isSelected()){
            try {
                Statement stmt= dataBase.getConnection().createStatement();
                ResultSet rs;
                rs=stmt.executeQuery("select * from members order by fname,lname");
                if(!rs.next()){
                    clearTable(table);
                    table.setPlaceholder(new Label("No match found"));
                }
                while(rs.next()){
                    String i=rs.getString(1);
                    String fn=rs.getString(2);
                    String ln=rs.getString(3);
                    String s=rs.getString(5);
                    String y=rs.getString(7);
                    int a=rs.getInt(10);
                    if(a==1){access="Committe";}
                    else if(a==2){continue;}
                    else{access="Member";}
                    table.getItems().addAll(addToMTable(i,fn,ln,s,y,access));
                }
            rs.close();
            stmt.close();
            dataBase.closeConnection();
           
     } catch (SQLException ex) {
            errorMsg.errorMessage("Error","Database connection failed.Try again");
     }
    }
    else if(!id.isDisabled()&&!id.getText().equals("")){
            try {
                Statement stmt= dataBase.getConnection().createStatement();
                ResultSet rs;
                rs=stmt.executeQuery("select * from members where id='"+id.getText()+"' order by fname,lname;");
                if(!rs.next()){
                    clearTable(table);
                    table.setPlaceholder(new Label("No match found"));
                }
                while(rs.next()){
                    String i=rs.getString(1);
                    String fn=rs.getString(2);
                    String ln=rs.getString(3);
                    String s=rs.getString(5);
                    String y=rs.getString(7);
                    int a=rs.getInt(10);
                    if(a==1){access="Committe";}
                    else if(a==2){break;}
                    else{access="Member";
                    break;}
                    table.getItems().addAll(addToMTable(i,fn,ln,s,y,access));
                }
            rs.close();
            stmt.close();
            dataBase.closeConnection();
     } catch (SQLException ex) {
            errorMsg.errorMessage("Error","Database connection failed.Try again");
     }
    }
        else if(!fname.isDisabled()&&!fname.getText().equals("")){
            try {
                Statement stmt= dataBase.getConnection().createStatement();
                ResultSet rs;
                rs=stmt.executeQuery("select * from members where fname='"+fname.getText()+"' order by fname,lname;");
                if(!rs.next()){
                    clearTable(table);
                    table.setPlaceholder(new Label("No match found"));
                }
                while(rs.next()){
                    String i=rs.getString(1);
                    String fn=rs.getString(2);
                    String ln=rs.getString(3);
                    String s=rs.getString(5);
                    String y=rs.getString(7);
                    int a=rs.getInt(10);
                    if(a==1){access="Committe";}
                    else if(a==2){continue;}
                    else{access="Member";}
                    table.getItems().addAll(addToMTable(i,fn,ln,s,y,access));
                }
            rs.close();
            stmt.close();
            dataBase.closeConnection();
           
     } catch (SQLException ex) {
            errorMsg.errorMessage("Error","Database connection failed.Try again");
     }
     }
    else if(!lname.isDisabled()&&!lname.getText().equals("")){
            try {
                Statement stmt= dataBase.getConnection().createStatement();
                ResultSet rs;
                rs=stmt.executeQuery("select * from members where lname='"+lname.getText()+"' order by fname,lname;");
                clearTable(table);
                while(rs.next()){
                    String i=rs.getString(1);
                    String fn=rs.getString(2);
                    String ln=rs.getString(3);
                    String s=rs.getString(5);
                    String y=rs.getString(7);
                    int a=rs.getInt(10);
                    if(a==1){access="Committe";}
                    else if(a==2){continue;}
                    else{access="Member";}
                    table.getItems().addAll(addToMTable(i,fn,ln,s,y,access));
                }
                rs.close();
                stmt.close();
                dataBase.closeConnection();
     } catch (SQLException ex) {
            errorMsg.errorMessage("Error","Database connection failed.Try again");
     }
     }
    else if(sexBox.isSelected()&&!yearBox.isSelected()&&!sex.getSelectionModel().isEmpty()){
            try {
                Statement stmt= dataBase.getConnection().createStatement();
                ResultSet rs;
                rs=stmt.executeQuery("select * from members where sex='"+getSex(sex.getValue())+"' order by fname,lname;");
                clearTable(table);
                while(rs.next()){
                    String i=rs.getString(1);
                    String fn=rs.getString(2);
                    String ln=rs.getString(3);
                    String s=rs.getString(5);
                    String y=rs.getString(7);
                    int a=rs.getInt(10);
                    if(a==1){access="Committe";}
                    else if(a==2){continue;}
                    else{access="Member";}
                    table.getItems().addAll(addToMTable(i,fn,ln,s,y,access));
                    }
            rs.close();
            stmt.close();
            dataBase.closeConnection();
     } catch (SQLException ex) {
            errorMsg.errorMessage("Error","Database connection failed.Try again");
     }
    }
    else if(!sexBox.isSelected()&&yearBox.isSelected()&!yearCombo.getSelectionModel().isEmpty()){
            try {
                Statement stmt= dataBase.getConnection().createStatement();
                ResultSet rs;
                rs=stmt.executeQuery("select * from members where year="+Integer.parseInt(yearCombo.getValue())+" order by fname,lname;");
                clearTable(table);
                while(rs.next()){
                    String i=rs.getString(1);
                    String fn=rs.getString(2);
                    String ln=rs.getString(3);
                    String s=rs.getString(5);
                    String y=rs.getString(7);
                    int a=rs.getInt(10);
                    if(a==1){access="Committe";}
                    else if(a==2){continue;}
                    else{access="Member";}
                    table.getItems().addAll(addToMTable(i,fn,ln,s,y,access));
                }
                rs.close();
                stmt.close();
                dataBase.closeConnection();
     } catch (SQLException ex) {
            errorMsg.errorMessage("Error","Database connection failed.Try again");
     }
    }
    else if(sexBox.isSelected()&&yearBox.isSelected()&&!sex.getSelectionModel().isEmpty()&&!yearCombo.getSelectionModel().isEmpty()){
            try {
                Statement stmt= dataBase.getConnection().createStatement();
                ResultSet rs;
                rs=stmt.executeQuery("select * from members where sex='"+getSex(sex.getValue())+"' and year="+Integer.parseInt(yearCombo.getValue())+" order by fname,lname;");
                if(!rs.next()){
                    clearTable(table);
                    table.setPlaceholder(new Label("No match found"));
                }
                while(rs.next()){
                    String i=rs.getString(1);
                    String fn=rs.getString(2);
                    String ln=rs.getString(3);
                    String s=rs.getString(5);
                    String y=rs.getString(7);
                    int a=rs.getInt(10);
                    if(a==1){access="Committe";}
                    else if(a==2){continue;}
                    else{access="Member";}
                    table.getItems().addAll(addToMTable(i,fn,ln,s,y,access));
                }
                rs.close();
                stmt.close();
                dataBase.closeConnection();
           
     } catch (SQLException ex) {
            errorMsg.errorMessage("Error","Database connection failed.Try again");
     }
    }
    else{
         errorMsg.errorMessage("Warning","You haven't entered any data.Try again.");}
         });
    clearBtn.setOnAction(e->{
            id.clear();
            fname.clear();
            lname.clear();
            sexBox.setSelected(false);
            yearBox.setSelected(false);
            
    });
    briefBtn.setOnAction(e->{
        ObservableList items=table.getItems();
        if(!items.isEmpty()){
            TablePosition pos=table.getSelectionModel().getSelectedCells().get(0);
            int index=pos.getRow();
            String ID=(String)table.getColumns().get(0).getCellObservableValue(index).getValue();
            briefInfo(ID);
        }
    });
    backBtn.setOnAction(e->{
         window.setScene(mainScene);
     });
    //Styling Nodes
    searchBtn.setPrefWidth(80);
    clearBtn.setPrefWidth(80);
    backBtn.setPrefWidth(80);
    briefBtn.setPrefWidth(100);
    //Styling containers
    VBox mainBox=new VBox();
    HBox hBox1=new HBox();
    hBox1.setSpacing(8);
    hBox1.setPadding(new Insets(10,10,10,10));
    HBox hBox2=new HBox();
    hBox2.setSpacing(8);
    hBox2.setPadding(new Insets(10,10,10,10));
    HBox hBox3=new HBox();
    hBox3.setSpacing(8);
    hBox3.setPadding(new Insets(10,10,10,10));
    hBox3.setAlignment(Pos.CENTER_RIGHT);
    hBox3.setStyle("-fx-border-width:1; -fx-border-style:solid; -fx-border-color:silver;");
    VBox vBox1=new VBox();
    vBox1.setAlignment(Pos.CENTER);
    vBox1.setSpacing(10);
    vBox1.setPrefWidth(200);
    vBox1.setStyle("-fx-border-width:1; -fx-border-style:solid; -fx-border-color:silver;");
    HBox hBox4=new HBox();
   

    hBox1.getChildren().addAll(allBox,id,fname,lname);
    hBox2.getChildren().addAll(sexBox,sex,yearBox,yearCombo);
    hBox3.getChildren().addAll(searchBtn,clearBtn,backBtn);
    vBox1.getChildren().addAll(briefBtn);
    hBox4.getChildren().addAll(table,vBox1);
    
    
    mainBox.getChildren().addAll(hBox1,hBox2,hBox3,hBox4);
return mainBox;}


private addToTable addToMTable(String id,String fname,String lname,String sex,String year,String access){
    addToTable at=new addToTable();
    at.setId(id);
    at.setFname(fname);
    at.setLname(lname);
    at.setSex(sex);
    at.setYear(year);
    at.setAccess(access);
    
return at;}
}
