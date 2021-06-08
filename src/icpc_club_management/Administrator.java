package icpc_club_management;

import static icpc_club_management.ICPC_Club_Management.mainScene;
import static icpc_club_management.ICPC_Club_Management.signScene;
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
import javafx.scene.input.KeyCode;
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


public class Administrator extends Helper{
private static String idA;   
private databaseConnection dataBase=new databaseConnection();
private checkData checkData=new checkData();
private displayError errorMsg=new displayError();
private static String access="Member";
    
public Scene AdminPage(String i){
    idA=i;
    Scene adminScene;
    Pane pane=new Pane();
    TabPane searchTab=new TabPane();
    searchTab.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    searchTab.setPrefWidth(700);
    Tab member=new Tab("Members");
    Tab committee=new Tab("Executive Committees");
    Tab profile=new Tab("Profile");
    Tab event=new Tab("Events");
    
    member.setContent(memberTab());
    committee.setContent(committeeTab());
    profile.setContent(profileTab(idA));
    event.setContent(eventTab());
    
    searchTab.getTabs().addAll(member,committee,profile,event);
     
    pane.setLayoutX(0);
    pane.setLayoutY(0);
    pane.getChildren().addAll(searchTab);
    adminScene=new Scene(pane,700,650);
    String keyCode="";
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
    final Button refreshBtn=new Button("Refresh");
    final Button clearBtn=new Button("Clear");
    final Button backBtn =new Button("Logout");
    final Button briefBtn=new Button("Brief");
    final Button deleteBtn=new Button("Delete");
    final Button addBtn=new Button("Promote to Committee");
    
    //set prompt text
    id.setPromptText("Id Number");
    fname.setPromptText("First Name");
    lname.setPromptText("Last Name");
    sex.setPromptText("Sex");
    yearCombo.setPromptText("Academic Year");
    
      
    //Styling Nodes
    searchBtn.setPrefWidth(80);
    refreshBtn.setPrefWidth(80);
    clearBtn.setPrefWidth(80);
    backBtn.setPrefWidth(80);
    briefBtn.setPrefWidth(100);
    deleteBtn.setPrefWidth(100);
    addBtn.setPrefWidth(150);
    sex.setDisable(true);
    yearCombo.setDisable(true);
    //Creating table
    TableView<addToTable> table=new TableView();
    TableColumn<addToTable,String> iCol=new TableColumn("Id");
    TableColumn<addToTable,String> fCol=new TableColumn("First Name");
    TableColumn<addToTable,String> lCol=new TableColumn("Last Name");
    TableColumn<addToTable,String> sCol=new TableColumn("Sex");
    TableColumn<addToTable,String> yCol=new TableColumn("Year");
    TableColumn<addToTable,String> aCol=new TableColumn("Membership");
    
    iCol.setCellValueFactory(new PropertyValueFactory("id"));
    iCol.setMinWidth(88);
    fCol.setCellValueFactory(new PropertyValueFactory("fname"));
    fCol.setMinWidth(85);
    lCol.setCellValueFactory(new PropertyValueFactory("lname"));
    lCol.setMinWidth(85);
    sCol.setCellValueFactory(new PropertyValueFactory("sex"));
    sCol.setMinWidth(85);
    yCol.setCellValueFactory(new PropertyValueFactory("year"));
    yCol.setMinWidth(85);
    aCol.setCellValueFactory(new PropertyValueFactory("access"));
    aCol.setMinWidth(85);
    
    table.setMaxWidth(500);
    table.setPrefHeight(430);
    table.setMaxHeight(430);
    table.getColumns().addAll(iCol,fCol,lCol,sCol,yCol,aCol);
    table.setPlaceholder(new Label("No data found"));
    table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
     
    //Adding values to comboxs
    sex.getItems().addAll("Female","Male");
    yearCombo.getItems().addAll("1","2","3","4","5");
    //Installing tooltip for a button
    refreshBtn.setTooltip(new Tooltip("Refresh changes"));
    searchBtn.setTooltip(new Tooltip("Search members"));
    clearBtn.setTooltip(new Tooltip("Clear all fields"));
    backBtn.setTooltip(new Tooltip("Logout"));
    briefBtn.setTooltip(new Tooltip("Show additional information\nabout selected member"));
    deleteBtn.setTooltip(new Tooltip("Delete selected member"));
    addBtn.setTooltip(new Tooltip("Promote selected member\nto executive committee"));
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
            else if(a==2){access="Adminstrator";}
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
    fname.focusedProperty().addListener((oV,oldV,newV)->{
        allBox.setSelected(false);
        allBox.setDisable(true);
        id.setDisable(true);
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
    lname.focusedProperty().addListener((oV,oldV,newV)->{
        allBox.setSelected(false);
        allBox.setDisable(true);
        id.setDisable(true);
        fname.setDisable(true);
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
            allBox.setSelected(false);
            allBox.setDisable(true);
            id.setDisable(true);
            fname.setDisable(true);
            lname.setDisable(true);
            
        }
        if(!sexBox.isSelected()){
            sex.setDisable(true);
            allBox.setDisable(false);
            id.setDisable(false);
            fname.setDisable(false);
            lname.setDisable(false);
            sexBox.setDisable(false);
        }
    });
    yearBox.selectedProperty().addListener(e->{
        if(yearBox.isSelected()){
            yearCombo.setDisable(false);
            allBox.setSelected(false);
            allBox.setDisable(true);
            id.setDisable(true);
            fname.setDisable(true);
            lname.setDisable(true);
        }
        if(!yearBox.isSelected()){
            yearCombo.setDisable(true);
            allBox.setDisable(false);
            id.setDisable(false);
            fname.setDisable(false);
            lname.setDisable(false);
            sexBox.setDisable(false);
        }
    });
    
    searchBtn.setOnAction(e->{
        if(allBox.isSelected()){
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
            else if(a==2){access="Adminstrator";}
            else{access="Member";}
            table.getItems().addAll(addToMTable(i,fn,ln,s,y,access));
            }
            table.setPlaceholder(new Label("No data found"));
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
                clearTable(table);
                if(rs.next()){
                    String i=rs.getString(1);
                    String fn=rs.getString(2);
                    String ln=rs.getString(3);
                    String s=rs.getString(5);
                    String y=rs.getString(7);
                    int a=rs.getInt(10);
                    if(a==1){access="Committe";}
                    else if(a==2){access="Adminstrator";}
                    else{access="Member";}
                    table.getItems().addAll(addToMTable(i,fn,ln,s,y,access));
                }
                else{ 
                    clearTable(table);
                    table.setPlaceholder(new Label("No match found"));
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
                clearTable(table);
                while(rs.next()){
                    String i=rs.getString(1);
                    String fn=rs.getString(2);
                    String ln=rs.getString(3);
                    String s=rs.getString(5);
                    String y=rs.getString(7);
                    int a=rs.getInt(10);
                    if(a==1){access="Committe";}
                    else if(a==2){access="Adminstrator";}
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
                    else if(a==2){access="Adminstrator";}
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
                    else if(a==2){access="Adminstrator";}
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
    else if(!sexBox.isSelected()&&yearBox.isSelected()&&!yearCombo.getSelectionModel().isEmpty()){
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
                    else if(a==2){access="Adminstrator";}
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
                clearTable(table);
                if(rs.next()){
                    String i=rs.getString(1);
                    String fn=rs.getString(2);
                    String ln=rs.getString(3);
                    String s=rs.getString(5);
                    String y=rs.getString(7);
                    int a=rs.getInt(10);
                    if(a==1){access="Committe";}
                    else if(a==2){access="Adminstrator";}
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
    addBtn.setOnAction(e->{
        ObservableList items=table.getItems();
        if(!items.isEmpty()){
            TablePosition pos=table.getSelectionModel().getSelectedCells().get(0);
            int index=pos.getRow();
            String ID=(String)table.getColumns().get(0).getCellObservableValue(index).getValue();
            try {
                Statement st=dataBase.getConnection().createStatement();
                ResultSet rs=st.executeQuery("select accesslevel from members where id='"+ID+"';");
                if(rs.next()){
                    if(rs.getInt(1)==2){
                        errorMsg.errorMessage("Warning","Adminstrator can't be a committee");
                    }
                    else if(rs.getInt(1)==1){
                        errorMsg.errorMessage("Warning","The member is already a committee");
                    }
                    else{
                    addDescription(ID);
                    }
                }
                rs.close();
                st.close();
                dataBase.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(Administrator.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    });
    refreshBtn.setOnAction(e->{
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
            else if(a==2){access="Adminstrator";}
            else{access="Member";}
            table.getItems().addAll(addToMTable(i,fn,ln,s,y,access));
            }
            table.setPlaceholder(new Label("No data found"));
            rs.close();
     } catch (SQLException ex) {
            errorMsg.errorMessage("Error","Database connection failed.Try again");
             }
    });
    deleteBtn.setOnAction(e->{
        ObservableList items=table.getItems();
        if(!items.isEmpty()){
            TablePosition pos=table.getSelectionModel().getSelectedCells().get(0);
            int index=pos.getRow();
            String ID=(String)table.getColumns().get(0).getCellObservableValue(index).getValue();
            String FNAME=(String)table.getColumns().get(1).getCellObservableValue(index).getValue();
            String LNAME=(String)table.getColumns().get(2).getCellObservableValue(index).getValue();
            if(deleteRequest("Are you sure do you want to delete \""+FNAME+" "+LNAME+"\" from member?")){
             try {
                 Statement st=dataBase.getConnection().createStatement();
                 ResultSet rs=st.executeQuery("select accesslevel from members where id='"+ID+"';");
                 if(rs.next()){
                     if(rs.getInt(1)!=2){
                       st.execute("delete from members where id='"+ID+"';");
                       table.getItems().remove(table.getSelectionModel().getSelectedItem());
                       errorMsg.errorMessage("Notification","Successfully deleted");}
                     else{
                         errorMsg.errorMessage("Warning","Adminstrator can't be deleted");
                     }
                 }
             } catch (SQLException ex) {
                 Logger.getLogger(Administrator.class.getName()).log(Level.SEVERE, null, ex);
             }
            }
        }
    });
    backBtn.setOnAction(e->{
        window.setScene(mainScene);
     });
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
    //Adding nodes to containers
    hBox1.getChildren().addAll(allBox,id,fname,lname);
    hBox2.getChildren().addAll(sexBox,sex,yearBox,yearCombo);
    hBox3.getChildren().addAll(refreshBtn,searchBtn,clearBtn,backBtn);
    vBox1.getChildren().addAll(briefBtn,deleteBtn,addBtn);
    hBox4.getChildren().addAll(table,vBox1);
    mainBox.getChildren().addAll(hBox1,hBox2,hBox3,hBox4);
return mainBox;}

private VBox committeeTab(){
    //Containers
    VBox vBox=new VBox();
    HBox hBox=new HBox();
    //Nodes
    final Button deleteBtn=new Button("Delete");
    final Button refreshBtn=new Button("Refresh");
    final Button backBtn=new Button("Logout");
    
    
    //Creating table
    TableView<addToTable> table=new TableView();
    TableColumn<addToTable,String> iCol=new TableColumn("Id");
    TableColumn<addToTable,String> fCol=new TableColumn("First Name");
    TableColumn<addToTable,String> lCol=new TableColumn("Last Name");
    TableColumn<addToTable,String> sCol=new TableColumn("Sex");
    TableColumn<addToTable,String> yCol=new TableColumn("Year");
    iCol.setCellValueFactory(new PropertyValueFactory("id"));
    iCol.setMinWidth(140);
    fCol.setCellValueFactory(new PropertyValueFactory("fname"));
    fCol.setMinWidth(140);
    lCol.setCellValueFactory(new PropertyValueFactory("lname"));
    lCol.setMinWidth(140);
    sCol.setCellValueFactory(new PropertyValueFactory("sex"));
    sCol.setMinWidth(140);
    yCol.setCellValueFactory(new PropertyValueFactory("year"));
    yCol.setMinWidth(140);
    table.prefWidthProperty().bind(vBox.widthProperty());
    table.setPrefHeight(500);
    table.setMaxHeight(500);
    table.getColumns().addAll(iCol,fCol,lCol,sCol,yCol);
    
    //Installing tooltips to buttons
    refreshBtn.setTooltip(new Tooltip("Refresh"));
    deleteBtn.setTooltip(new Tooltip("Delete selected\nexecutive committee"));
    backBtn.setTooltip(new Tooltip("Logout"));
    
    try {
        Statement stmt= dataBase.getConnection().createStatement();
        ResultSet rs;
        rs=stmt.executeQuery("select * from members where accesslevel="+1+" order by fname,lname");
        clearTable(table);
        while(rs.next()){
            String i=rs.getString(1);
            String fn=rs.getString(2);
            String ln=rs.getString(3);
            String s=rs.getString(5);
            String y=rs.getString(7);
            table.getItems().addAll(addToCTable(i,fn,ln,s,y));
            }
            table.setPlaceholder(new Label("No data found"));
            rs.close();
     } catch (SQLException ex) {
            errorMsg.errorMessage("Error","Database connection failed.Try again");
     }
     deleteBtn.setOnAction(e->{
        ObservableList items=table.getItems();
        if(!items.isEmpty()){
            TablePosition pos=table.getSelectionModel().getSelectedCells().get(0);
            int index=pos.getRow();
            String ID=(String)table.getColumns().get(0).getCellObservableValue(index).getValue();
            String FNAME=(String)table.getColumns().get(1).getCellObservableValue(index).getValue();
            String LNAME=(String)table.getColumns().get(2).getCellObservableValue(index).getValue();
            if(deleteRequest("Are you sure do you want to delete "+FNAME+" "+LNAME+" from Executive committee?")){
            try {
                Statement st=dataBase.getConnection().createStatement();
                st.execute("delete from executive_committees where id='"+ID+"';");
                st.execute("update members set accesslevel="+0+" where id='"+ID+"';");
                table.getItems().remove(table.getSelectionModel().getSelectedItem());
                errorMsg.errorMessage("Notification","Successfully deleted");
             } catch (SQLException ex) {
                 Logger.getLogger(Administrator.class.getName()).log(Level.SEVERE, null, ex);
             }
            }
        }
    });
     refreshBtn.setOnAction(e->{
         try {
        Statement stmt= dataBase.getConnection().createStatement();
        ResultSet rs;
        rs=stmt.executeQuery("select * from members where accesslevel="+1+" order by fname,lname");
        clearTable(table);
        while(rs.next()){
            String i=rs.getString(1);
            String fn=rs.getString(2);
            String ln=rs.getString(3);
            String s=rs.getString(5);
            String y=rs.getString(7);
            table.getItems().addAll(addToCTable(i,fn,ln,s,y));
            }
            table.setPlaceholder(new Label("No data found"));
            rs.close();
     } catch (SQLException ex) {
            errorMsg.errorMessage("Error","Database connection failed.Try again");
     }
     });
     backBtn.setOnAction(e->{
         window.setScene(mainScene);
     });
    //Styling nodes
    refreshBtn.setPrefWidth(80);
    deleteBtn.setPrefWidth(80);
    backBtn.setPrefWidth(80);
    //Styling containers
    hBox.setAlignment(Pos.CENTER_RIGHT);
    hBox.setSpacing(10);
    hBox.setPadding(new Insets(50,10,0,0));
    //Adding nodes to containers 
    hBox.getChildren().addAll(refreshBtn,deleteBtn,backBtn);
    vBox.getChildren().addAll(table,hBox);
    
return vBox;}

private addToTable addToMTable(String id,String fname,String lname,String sex,String year,String access){
    addToTable at=new addToTable();
    at.setId(id);
    at.setFname(fname);
    at.setLname(lname);
    at.setSex(sex);
    at.setYear(year);
    at.setAccess(access);
    
return at;}
private addToTable addToCTable(String id,String fname,String lname,String sex,String year){
    addToTable at=new addToTable();
    at.setId(id);
    at.setFname(fname);
    at.setLname(lname);
    at.setSex(sex);
    at.setYear(year);
return at;}
private void addDescription(String ID){
    Stage win=new Stage();
    win.initModality(Modality.APPLICATION_MODAL);
    win.setTitle("Committee Title");
    Scene scene;
    //Containers
    VBox vBox=new VBox();
    HBox hBox0=new HBox();
    HBox hBox1=new HBox();
    HBox hBox2=new HBox();
    HBox hBox3=new HBox();
    
    //Nodes
    final Label notifyL=new Label();
    final Label tL=new Label("Title");
    final TextField titleField=new TextField();
    final Button addBtn=new Button("Add");
    //Set prompt text to the node
    titleField.setPromptText("Title");
    addBtn.setOnAction(e->{
        if(!titleField.getText().equals("")){
            notifyL.setText("");
            try {
                Statement st=dataBase.getConnection().createStatement();
                st.execute("update members set accesslevel="+1+" where id='"+ID+"';");
                st.execute("insert into executive_committees values('"+ID+"','"+titleField.getText()+"','"+LocalDate.now()+"');");
                win.close();
                errorMsg.errorMessage("Notification","Successfully promoted");
            }
            catch (SQLException ex) {
                Logger.getLogger(Administrator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            notifyL.setText("Required field empty.");
        }
    });
    //Styling nodes
    tL.setFont(Font.font("Arial", 15));
    addBtn.setPrefWidth(80);
    notifyL.setTextFill(Color.web("#F0360F"));
    titleField.setPrefWidth(200);
    //Styling containers
    vBox.setAlignment(Pos.CENTER);
    hBox0.setAlignment(Pos.CENTER);
    hBox1.setAlignment(Pos.CENTER);
    hBox2.setAlignment(Pos.CENTER);
    hBox3.setAlignment(Pos.CENTER);
    vBox.setSpacing(10);
    //Adding nodes to containers
    hBox1.getChildren().add(notifyL);
    hBox0.getChildren().add(tL);
    hBox2.getChildren().add(titleField);
    hBox3.getChildren().add(addBtn);
    vBox.getChildren().addAll(hBox1,hBox0,hBox2,hBox3);
    
    scene=new Scene(vBox,300,150);
    scene.getStylesheets().addAll("Light.css");
    win.setScene(scene);
    win.showAndWait();
}   

}
