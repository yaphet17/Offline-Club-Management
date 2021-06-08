
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
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.security.crypto.bcrypt.BCrypt;
import static org.springframework.security.crypto.bcrypt.BCrypt.gensalt;

public class Helper {
    databaseConnection dataBase=new databaseConnection();
    displayError errorMsg=new displayError();
    checkData checkData=new checkData();
    Stage profileWindow=new Stage();
    private static String id;
    Stage passWindow=new Stage();
    final Rectangle2D visualBounds=Screen.getPrimary().getVisualBounds();
    
    //A Tab that provide brief information about member
VBox profileTab(String id){
    this.id=id;
    //Containers
    VBox vBox=new VBox();
    VBox vBox1=new VBox();
    HBox hBox=new HBox();
    HBox hBox1=new HBox();
    HBox hBox2=new HBox();
    HBox hBox3=new HBox();
    HBox hBox4=new HBox();
    HBox hBox5=new HBox();
    HBox hBox6=new HBox();
    HBox hBox7=new HBox();
    HBox hBox8=new HBox();
   
    //Nodes
    final Label profileL=new Label("Profile Information");
    final TextField browseField=new TextField(); 
    final ComboBox<String> yearCombo=new ComboBox<>();
    final TextField emailField=new TextField(); 
    final TextField phoneField=new TextField();
    final ComboBox<String> progCombo=new ComboBox<>();
    final TextField gitField=new TextField();
    final CheckBox updateCheck=new CheckBox("Update");
    final Hyperlink passLink=new Hyperlink("Change Password?");
    final Button browseBtn=new Button("Browse");
    final Button addBtn=new Button("Add");
    final Button clearBtn=new Button("Clear");
    final Button exitBtn=new Button("Logout");
    final Image image=new Image("placeHolder.png");
    ImageView imageView=new ImageView(image);
    //File choosers
    FileChooser fileChooser=new FileChooser();
    Desktop desktop=Desktop.getDesktop();
    FileChooser.ExtensionFilter jpgFilter= new FileChooser.ExtensionFilter("JPEG","JPG","*.jpeg","*.jpg");
    FileChooser.ExtensionFilter pngFilter= new FileChooser.ExtensionFilter("PNG","*.png");
    fileChooser.getExtensionFilters().addAll(jpgFilter,pngFilter);
    fileChooser.setTitle("Choose Image");
    
    //set prompt text
    browseField.setPromptText("Image path here");
    yearCombo.setPromptText("Academic Year");
    emailField.setPromptText("Email Address");
    phoneField.setPromptText("Phone Number");
    progCombo.setPromptText("Major Programming Language");
    gitField.setPromptText("GitHub Link");
    
    //Set contents to comboxs
    yearCombo.getItems().addAll("1","2","3","4","5");
    progCombo.getItems().addAll("Java","C++","C","Python","C#","Ruby","JavaScript","PHP","Lisp");
     //Fetching data from database
    try {
        Statement stmt= dataBase.getConnection().createStatement();
        ResultSet rs;
        rs=stmt.executeQuery("select * from members where id='"+id+"';");
        if(rs.next()){
            String y=rs.getString(7);
            String e=rs.getString(8);
            String p=rs.getString(9);
            yearCombo.setValue(y);
            emailField.setText(e);
            phoneField.setText(p);
        }
        rs=stmt.executeQuery("select * from  memberaddtional where id='"+id+"';");
        if(rs.next()){
            String ip=rs.getString(2);
            String pl=rs.getString(3);
            String g=rs.getString(4);
            String path=rs.getString(2).replace("\\","\\\\");
                    Image image2;
                    try {
                        image2 = new Image(new FileInputStream(path));
                        imageView.setImage(image2);
                    }catch (Exception ex) {
                    
                       errorMsg.errorMessage("File","Image file not found in "+path);
                       
                    }
            browseField.setText(path);
            progCombo.setValue(pl);
            gitField.setText(g);
            updateCheck.setSelected(true);
            updateCheck.setDisable(true);
            yearCombo.setDisable(false);
            emailField.setDisable(false);
            phoneField.setDisable(false);
        }
        
        stmt.close();
        rs.close();
        dataBase.closeConnection();
     } catch (SQLException ex) {
            errorMsg.errorMessage("Error","Database connection failed.Try again");
     }
    //setting nodes on action
    updateCheck.setSelected(true);
    addBtn.setOnAction(e->{
        if(updateCheck.isSelected()){
            if(!browseField.getText().equals("")&&yearCombo.getValue()!=null&&!emailField.getText().equals("")
            &&!phoneField.getText().equals("")&&progCombo.getValue()!=null&&!gitField.getText().equals("")){
                if(checkData.checkEmail(emailField.getText())){
                if(checkData.checkNumber(phoneField.getText())){
                try {
                   Statement st = dataBase.getConnection().createStatement();
                   st.execute("delete from  memberaddtional where id='"+id+"';");
                   st.execute("insert into  memberaddtional values('"+id+"','"+copyImage(browseField.getText(),emailField.getText())+"','"+progCombo.getValue()+"','"+
                   gitField.getText()+"');");
                   st.execute("update members set year="+yearCombo.getValue()+" where id='"+id+"';");
                   st.execute("update members set email='"+emailField.getText()+"' where id='"+id+"';");
                   st.execute("update members set phone="+phoneField.getText()+" where id='"+id+"';");
                   st.close();
                   dataBase.closeConnection();
                   errorMsg.errorMessage("Notification","Data sucessfully updated");

                } catch (SQLException ex) {
                     errorMsg.errorMessage("Error","Database connection failed.Try again");
                }
            }else{ errorMsg.errorMessage("Warning","Invalid phone number");}
            }else{ errorMsg.errorMessage("Warning","Invalid email address");}
            }else{
                errorMsg.errorMessage("Warning","Required fields empty.");}
        }
        else{
             if(!browseField.getText().equals("")&&progCombo.getValue()!=null&&!gitField.getText().equals("")){
                try {
                   Statement st = dataBase.getConnection().createStatement();
                   st.execute("insert into  memberaddtional values('"+copyImage(browseField.getText(),emailField.getText())+"','"+progCombo.getValue()+"','"+
                   gitField.getText()+"');");
                   st.close();
                   dataBase.closeConnection();
                   errorMsg.errorMessage("Notification","Data sucessfully added");
                } catch (SQLException ex) {
                     errorMsg.errorMessage("Error","Database connection failed.Try again");
                }
            }else{
                errorMsg.errorMessage("Warning","Required fields empty.");}
        }
    });
    browseBtn.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent e) {
             try {
                File file=fileChooser.showOpenDialog(profileWindow);
                FileInputStream input=new FileInputStream(file.getAbsolutePath());
                Image image=new Image(input);
                imageView.setCache(true);
                imageView.setImage(image);
                imageView.setFitWidth(150);
                imageView.setFitHeight(150);
                String path=file.getAbsolutePath();
                browseField.setText(path.replace("\\","\\\\"));
            } catch (Exception ex) {
                errorMsg.errorMessage("Error","Can't find the image in the specified path");
             }}
     });
    updateCheck.selectedProperty().addListener(e->{
        if(!updateCheck.isSelected()){
            yearCombo.setDisable(true);
            emailField.setDisable(true);
            phoneField.setDisable(true);
        }
        if(updateCheck.isSelected()){
            yearCombo.setDisable(false);
            emailField.setDisable(false);
            phoneField.setDisable(false);
            
        }
    });
    passLink.setOnAction(e->{
        passPage();
        passWindow.setX((visualBounds.getMaxX()/2)-(window.getWidth()/2));
        passWindow.setY(100);
    });
    exitBtn.setOnAction(e->{
       window.setScene(mainScene);
    });
    
    //Styling nodes
    addBtn.setPrefWidth(80);
    clearBtn.setPrefWidth(80);
    exitBtn.setPrefWidth(80);
    
    profileL.setFont(new Font("Arial", 30));
    browseField.setPrefWidth(200);
    browseBtn.setPrefWidth(100);
    yearCombo.setPrefWidth(300);
    emailField.setPrefWidth(300);
    phoneField.setPrefWidth(300);
    progCombo.setPrefWidth(300);
    gitField.setPrefWidth(300);
    imageView.setFitWidth(150);
    imageView.setFitHeight(150);
    
     //Styling Controls
    vBox.prefWidthProperty().bind(window.widthProperty());
    vBox.setAlignment(Pos.CENTER);
    vBox.setSpacing(10);
    hBox.setAlignment(Pos.CENTER_LEFT);
    hBox.setPadding(new Insets(0,0,20,30));
    vBox1.setAlignment(Pos.CENTER);
    vBox1.setSpacing(5);
    vBox1.setPadding(new Insets(0,0,20,0));
    hBox1.setAlignment(Pos.CENTER);
    hBox2.setAlignment(Pos.CENTER);
    hBox3.setAlignment(Pos.CENTER);
    hBox4.setAlignment(Pos.CENTER);
    hBox5.setAlignment(Pos.CENTER);
    hBox6.setAlignment(Pos.CENTER);
    hBox7.setAlignment(Pos.CENTER);
    hBox.setSpacing(10);
    hBox8.setAlignment(Pos.CENTER_RIGHT);
    hBox8.setPadding(new Insets(0,20,20,0));
    hBox8.setSpacing(10);
    
    
    //Adding nodes to containers
    hBox.getChildren().add(profileL);
    hBox1.getChildren().addAll(browseField,browseBtn);
    vBox1.getChildren().addAll(imageView,hBox1);
    hBox2.getChildren().addAll(yearCombo);
    hBox3.getChildren().addAll(emailField);
    hBox4.getChildren().addAll(phoneField);
    hBox5.getChildren().addAll(progCombo);
    hBox6.getChildren().addAll(gitField);
    hBox7.getChildren().addAll(updateCheck,passLink);
    hBox8.getChildren().addAll(addBtn,clearBtn,exitBtn);
    vBox.getChildren().addAll(hBox,vBox1,hBox2,hBox3,hBox4,hBox5,hBox6,hBox7,hBox8);
    
    return vBox;
}
VBox eventTab(){
    //Containers
    VBox vBox=new VBox();
    GridPane grid=new GridPane();
    HBox hBox1=new HBox();
    HBox hBox2=new HBox();
    HBox hBox3=new HBox();
    HBox hBox4=new HBox();
    HBox hBox5=new HBox();
    HBox hBox6=new HBox();
    
    
    //Nodes
    Label titleL=new Label("Title:");
    Label descL=new Label("Description:");
    Label dateL=new Label("Date:");
    Label placeL=new Label("Place:");
    Label timeL=new Label("Time:");
    TextField titleField=new TextField();
    TextArea descField=new TextArea();
    DatePicker dateField=new DatePicker();
    TextField placeField=new TextField();
    TextField timeField=new TextField();
    Button addBtn=new Button("Add");
    Button showBtn=new Button("Show Description");
    Button clearBtn=new Button("Clear");
    Button deleteBtn=new Button("Delete");
    Button backBtn=new Button("Logout");
    
    //set prompt text
    titleField.setPromptText("Event title here");
    dateField.setPromptText("Event date here");
    placeField.setPromptText("Event place here");
    timeField.setPromptText("Event time here(HH:MM)");
    //Disabling datepickers editor
     dateField.setEditable(false);
     //Creating table
    TableView<addToETable> table=new TableView();
    TableColumn<addToETable,String> tCol=new TableColumn("Title");
    TableColumn<addToETable,String> dCol=new TableColumn("Date");
    TableColumn<addToETable,String> pCol=new TableColumn("Place");
    TableColumn<addToETable,String> tmCol=new TableColumn("Time");
    
    tCol.setCellValueFactory(new PropertyValueFactory("title"));
    tCol.setMinWidth(170);
    dCol.setCellValueFactory(new PropertyValueFactory("date"));
    dCol.setMinWidth(170);
    pCol.setCellValueFactory(new PropertyValueFactory("place"));
    pCol.setMinWidth(170);
    tmCol.setCellValueFactory(new PropertyValueFactory("time"));
    tmCol.setMinWidth(170);
    
    
    table.prefWidthProperty().bind(vBox.prefWidthProperty());
    table.setPrefHeight(250);
    table.setMaxHeight(250);
    table.getColumns().addAll(tCol,dCol,pCol,tmCol);
    table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    //Installing tooltips for buttons
    addBtn.setTooltip(new Tooltip("Add event"));
    showBtn.setTooltip(new Tooltip("Show description\nabout selected event"));
    clearBtn.setTooltip(new Tooltip("Clear all fields"));
    deleteBtn.setTooltip(new Tooltip("Delete selected event "));
    backBtn.setTooltip(new Tooltip("Logout"));
    
    //Fetching data from database
     try {
        Statement stmt= dataBase.getConnection().createStatement();
        ResultSet rs;
        rs=stmt.executeQuery("select * from event order by date");
        while(rs.next()){
            String titleS=rs.getString(1);
            String dateS=rs.getString(3);
            String placeS=rs.getString(4);
            String timeS=rs.getString(5);
            table.getItems().addAll(addToETable(titleS,dateS,placeS,timeS));
            }
            rs.close();
     } catch (SQLException ex) {
            errorMsg.errorMessage("Error","Database connection failed.Try again");
     }
    
    //Setting nodes to action
    addBtn.setOnAction(e->{
        if(!titleField.getText().equals("")&&!descField.getText().equals("")&&dateField.getValue()!=null&&
           !placeField.getText().equals("")&&!timeField.getText().equals("")){
        if(checkData.checkTime(timeField.getText())){
            try {
                Statement st=dataBase.getConnection().createStatement();
                st.execute("insert into event values('"+titleField.getText()+"','"+descField.getText()+"','"+dateField.getValue()+"','"
                +placeField.getText()+"','"+timeField.getText()+":01');");
                table.getItems().addAll(addToETable(titleField.getText(),dateField.getValue().toString(),placeField.getText(),timeField.getText()));
                errorMsg.errorMessage("Notification","Event sucessfully added");
            } catch (SQLException ex) {
                Logger.getLogger(Administrator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            errorMsg.errorMessage("Warning","Invalid time formatPlease enter valid time.");
        }
        }
        else{
            errorMsg.errorMessage("Warning","Required field empty.Please fill all fields.");
        }
    });
    showBtn.setOnAction(e->{
        ObservableList items=table.getItems();
        if(!items.isEmpty()){
            TablePosition pos=table.getSelectionModel().getSelectedCells().get(0);
            int index=pos.getRow();
            String TITLE=(String)table.getColumns().get(0).getCellObservableValue(index).getValue();
            pos=table.getSelectionModel().getSelectedCells().get(0);
            index=pos.getRow();
            String DATE=(String)table.getColumns().get(1).getCellObservableValue(index).getValue();
            showDescription(TITLE,DATE);
        }
    });
    clearBtn.setOnAction(e->{
        titleField.clear();
        descField.clear();
        dateField.setValue(LocalDate.now());
        dateField.getEditor().clear();
        placeField.clear();
        timeField.clear();
    });
    deleteBtn.setOnAction(e->{
         ObservableList items=table.getItems();
        if(!items.isEmpty()){
            TablePosition pos=table.getSelectionModel().getSelectedCells().get(0);
            int index=pos.getRow();
            String TITLE=(String)table.getColumns().get(0).getCellObservableValue(index).getValue();
            String DATE=(String)table.getColumns().get(1).getCellObservableValue(index).getValue();
            if(deleteRequest("Are you sure do you want to delete selected event?")){
                try {
                    Statement st=dataBase.getConnection().createStatement();
                    st.execute("delete from event where title='"+TITLE+"' and date='"+DATE+"';");
                    table.getItems().remove(table.getSelectionModel().getSelectedItem());
                    errorMsg.errorMessage("Notification","Successfully deleted");
                }
                catch (SQLException ex) {
                    Logger.getLogger(Administrator.class.getName()).log(Level.SEVERE, null, ex);
             }
            }
        }

    });
    backBtn.setOnAction(e->{
        window.setScene(mainScene);
    });
    
    //Styling nodes
    addBtn.setPrefWidth(80);
    clearBtn.setPrefWidth(80);
    backBtn.setPrefWidth(80);
    titleL.setPrefWidth(80);
    descL.setPrefWidth(80);
    dateL.setPrefWidth(80);
    placeL.setPrefWidth(80);
    timeL.setPrefWidth(80);
    titleField.setPrefWidth(300);
    descField.setPrefWidth(300);
    descField.setMaxWidth(300);
    descField.setPrefHeight(100);
    dateField.setPrefWidth(300);
    placeField.setPrefWidth(300);
    timeField.setPrefWidth(300);
    
    //Styling containers
    vBox.setSpacing(10);
    hBox1.setAlignment(Pos.CENTER_LEFT);
    hBox1.setPadding(new Insets(0,0,0,10));
    hBox1.setSpacing(10);
    hBox2.setAlignment(Pos.CENTER_LEFT);
    hBox2.setPadding(new Insets(0,0,0,10));
    hBox2.setSpacing(10);
    hBox3.setAlignment(Pos.CENTER_LEFT);
    hBox3.setPadding(new Insets(0,0,0,10));
    hBox3.setSpacing(10);
    hBox4.setAlignment(Pos.CENTER_LEFT);
    hBox4.setPadding(new Insets(0,0,0,10));
    hBox4.setSpacing(10);
    hBox5.setAlignment(Pos.CENTER_LEFT);
    hBox5.setPadding(new Insets(0,0,0,10));
    hBox5.setSpacing(10);
    hBox6.setAlignment(Pos.BOTTOM_RIGHT);
    hBox6.setSpacing(10);
    hBox6.setPadding(new Insets(50,20,5,0));
    
    //Adding nodes to containers
    hBox1.getChildren().addAll(titleL,titleField);
    hBox2.getChildren().addAll(descL,descField);
    hBox3.getChildren().addAll(dateL,dateField);
    hBox4.getChildren().addAll(placeL,placeField);
    hBox5.getChildren().addAll(timeL,timeField);
    hBox6.getChildren().addAll(addBtn,showBtn,clearBtn,backBtn);
    vBox.getChildren().addAll(table,hBox1,hBox2,hBox3,hBox4,hBox5,hBox6);
    
return vBox;}
void passPage(){
    passWindow.setResizable(false);
    passWindow.initModality(Modality.APPLICATION_MODAL);
    Scene passScene;
    //Containers
    VBox vBox=new VBox();
    HBox hBox1=new HBox();
    HBox hBox2=new HBox();
    HBox hBox3=new HBox();
    HBox hBox4=new HBox();
    HBox hBox5=new HBox();
    //Nodes
    Label changeL=new Label("Change Password");
    PasswordField oldField=new PasswordField();
    PasswordField newField=new PasswordField();
    PasswordField confirmField=new PasswordField();
    Button updateBtn=new Button("Update");
    Button exitBtn=new Button("Exit");
    //Setting update button as a default
    updateBtn.setDefaultButton(true);
    
    //set prompt text for fields
    oldField.setPromptText("Old password");
    newField.setPromptText("New Password");
    confirmField.setPromptText("Confirm password");
    
     //setting button on action
     updateBtn.setOnAction(e->{
         if(!oldField.getText().equals("")&&!newField.getText().equals("")&&!confirmField.getText().equals("")){
             try {
                 Statement st=dataBase.getConnection().createStatement();
                 ResultSet rs=st.executeQuery("select password from members where id='"+id+"';");
                 if(rs.next()){
                     if(BCrypt.checkpw(oldField.getText(),rs.getString(1))){
                        if(newField.getText().equals(confirmField.getText())){
                            st.execute("update members set password='"+BCrypt.hashpw(newField.getText(),gensalt(10))+"' where id='"+id+"';");
                            errorMsg.errorMessage("Notification","Sucessfully updated.");
                            passWindow.close();
                         }else{
                            errorMsg.errorMessage("Warning","The password you entered don't match.");
                         }
                     }else{
                          errorMsg.errorMessage("Warning","Invalid password.");
                     }
                 }
                 st.close();
                 rs.close();
                 dataBase.closeConnection();
             }catch (SQLException ex) {
                 errorMsg.errorMessage("Error","Database connection failed.Try again");
             }
         }else{
             errorMsg.errorMessage("Warning","Required fields emepty");
         }
         
     });
    exitBtn.setOnAction(e->{
        passWindow.close();
    });
    //Styling Nodes
    changeL.setFont(new Font("Arial", 25));
    oldField.setPrefWidth(300);
    newField.setPrefWidth(300);
    confirmField.setPrefWidth(300);
    updateBtn.setPrefWidth(80);
    exitBtn.setPrefWidth(80);
    //Styling contrainers
    vBox.setSpacing(10);
    hBox1.setAlignment(Pos.CENTER_LEFT);
    hBox1.setPadding(new Insets(10,0,0,30));
    hBox2.setAlignment(Pos.CENTER);
    hBox3.setAlignment(Pos.CENTER);
    hBox4.setAlignment(Pos.CENTER);
    hBox5.setAlignment(Pos.CENTER_RIGHT);
    hBox5.setAlignment(Pos.CENTER_RIGHT);
    hBox5.setPadding(new Insets(15,20,0,0));
    hBox5.setSpacing(10);
    //Adding nodes to containers
    hBox1.getChildren().addAll(changeL);
    hBox2.getChildren().addAll(oldField);
    hBox3.getChildren().addAll(newField);
    hBox4.getChildren().addAll(confirmField);
    hBox5.getChildren().addAll(updateBtn,exitBtn);
    vBox.getChildren().addAll(hBox1,hBox2,hBox3,hBox4,hBox5);
    
    passScene=new Scene(vBox,450,220);
    passScene.setOnKeyPressed(e->{
        if(e.getCode()==KeyCode.ESCAPE){
            exitBtn.fire();
        }
    });
    passScene.getStylesheets().addAll("Light.css");
    passWindow.setScene(passScene);
    passWindow.showAndWait();
}
void briefInfo(String ID){
    Stage window=new Stage();
    window.setTitle("Member Information");
    window.initModality(Modality.APPLICATION_MODAL);
    window.setResizable(false);
    window.setOnCloseRequest(e->{
        dataBase.closeConnection();
    });
    Scene briefScene;
    Pane pane=new Pane();
    
    Label idL=new Label("Member Id:");
    Label fnameL=new Label("First Name:");
    Label lnameL=new Label("Last Name:");
    Label sexL=new Label("Sex:");
    Label departL=new Label("Department:");
    Label yearL=new Label("Academic Year:");
    Label emailL=new Label("Email Address:");
    Label phoneL=new Label("Phone Number:");
    Label memberL=new Label("Membership Level:");
    Label dateL=new Label("Membership Date:");
    Label progL=new Label("Programming Language");
    Label gitL=new Label("Github Link:");
    
    Label idDisp=new Label();
    idDisp.setFont(new Font("Arial",18));
    Label fnameDisp=new Label();
    fnameDisp.setFont(new Font("Arial",18));
    Label  lnameDisp=new Label();
    lnameDisp.setFont(new Font("Arial",18));
    Label  sexDisp=new Label();
    sexDisp.setFont(new Font("Arial",18));
    Label  departDisp=new Label();
    departDisp.setFont(new Font("Arial",18));
    Label  yearDisp=new Label();
    yearDisp.setFont(new Font("Arial",18));
    Label  emailDisp=new Label();
    emailDisp.setFont(new Font("Arial",18));
    Label  phoneDisp=new Label();
    phoneDisp.setFont(new Font("Arial",18));
    Label  memberDisp=new Label();
    memberDisp.setFont(new Font("Arial",18));
    Label  dateDisp=new Label();
    dateDisp.setFont(new Font("Arial",18));
    Label progDisp=new Label();
    progDisp.setFont(new Font("Arial",18));
    Label gitDisp=new Label();
    gitDisp.setFont(new Font("Arial",18));
   
    
    ImageView imageView=new ImageView();
    Image image=new Image("placeHolder.png");
    imageView.setImage(image);
    imageView.setFitWidth(280);
    imageView.setFitHeight(280);
    
    //set the position of Image
    imageView.setLayoutX(200);
    imageView.setLayoutY(30);
    try {
        Statement st=dataBase.getConnection().createStatement();
        ResultSet rs=st.executeQuery("select * from members where id='"+ID+"'");
        while(rs.next()){
            String i=rs.getString(1);
            idDisp.setText(i);
            String fname=rs.getString(2);
            fnameDisp.setText(fname);
            String lname=rs.getString(3);
            lnameDisp.setText(lname);
            String sx=rs.getString(5);
            sexDisp.setText(sx);
            String dt=rs.getString(6);
            departDisp.setText(dt);
            int y=rs.getInt(7);
            yearDisp.setText(Integer.toString(y));
            String e=rs.getString(8);
            emailDisp.setText(e);
            int p=rs.getInt(9);
            phoneDisp.setText(Integer.toString(p));
            int ac=rs.getInt(10);
            if(ac==1){ memberDisp.setText("Committe");}
            else if(ac==2){ memberDisp.setText("Administrator");}
            else{ memberDisp.setText("Member");;}
            String d=rs.getString(11);
            dateDisp.setText(d);
            break;
        }
        
        rs=st.executeQuery("select * from memberaddtional where id='"+ID+"'");
        if(rs.next()){
            String path=rs.getString(2).replace("\\","\\\\");
            Image image2;
            try {
                image2 = new Image(new FileInputStream(path));
                imageView.setImage(image2);
            }catch (Exception ex) {
               errorMsg.errorMessage("Warning","Image file not found in"+path);
            }
            String pg=rs.getString(3);
            progDisp.setText(pg);
            String g=rs.getString(4);
            gitDisp.setText(g);
        }
        
        rs.close();
        st.close();
        dataBase.closeConnection();
        }catch (SQLException ex) {
            errorMsg.errorMessage("Error","Database connection failed.Try again");
        }
         
    GridPane grid=new GridPane();
    grid.setPadding(new Insets(20));
    grid.setVgap(15);
    grid.setHgap(10);
    //Colunm one
    grid.setConstraints(idL,0,1);
    grid.setConstraints(idDisp,1,1);
    grid.setConstraints(fnameL,0,2);
    grid.setConstraints(fnameDisp,1,2);
    grid.setConstraints(lnameL,0,3);
    grid.setConstraints(lnameDisp,1,3);
    grid.setConstraints(sexL,0,4);
    grid.setConstraints(sexDisp,1,4);
    grid.setConstraints(departL,0,5);
    grid.setConstraints(departDisp,1,5);
    grid.setConstraints(yearL,0,6);
    grid.setConstraints(yearDisp,1,6);
    grid.setConstraints(emailL,0,7);
    grid.setConstraints(emailDisp,1,7);
    //Column Two
    grid.setConstraints(phoneL,12,1);
    grid.setConstraints(phoneDisp,13,1);
    grid.setConstraints(memberL,12,2);
    grid.setConstraints(memberDisp,13,2);
    grid.setConstraints(dateL,12,3);
    grid.setConstraints(dateDisp,13,3);
    grid.setConstraints(progL,12,4);
    grid.setConstraints(progDisp,13,4);
    grid.setConstraints(gitL,12,5);
    grid.setConstraints(gitDisp,13,5);
    
    
    grid.getChildren().addAll(idL,idDisp,fnameL,fnameDisp,lnameDisp,lnameL,sexL,sexDisp,departL,
                              departDisp,yearL,yearDisp,emailL,emailDisp,phoneL,phoneDisp,memberL,memberDisp,dateL,dateDisp,progL,progDisp,gitL,gitDisp);
    
    //set the position of layouts
   grid.setLayoutX(0);
   grid.setLayoutY(330);
   pane.getChildren().addAll(imageView,grid);
   briefScene=new Scene(pane,700,650);
   briefScene.setOnKeyPressed(e->{
       if(e.getCode()==KeyCode.ESCAPE){
           window.close();
       }
   });
   briefScene.getStylesheets().addAll("Light.css");
   window.setScene(briefScene);
   window.showAndWait();
}
addToETable addToETable(String title,String date,String place,String time){
    addToETable at=new addToETable();
    at.setTitle(title);
    at.setDate(date);
    at.setPlace(place);
    at.setTime(time);
    
return at;}
void showDescription(String TITLE,String DATE){
    Stage showWindow=new Stage();
    showWindow.setResizable(false);
    showWindow.initModality(Modality.APPLICATION_MODAL);
    Scene showScene;
    //Containers
    VBox vBox=new VBox();
    HBox hBox1=new HBox();
    HBox hBox2=new HBox();
    //Nodes
    TextArea descField=new TextArea();
    Button exitBtn=new Button("Exit");
    //Fteching data from databse
    try {
        Statement st=dataBase.getConnection().createStatement();
        ResultSet rs=st.executeQuery("select description from event where title='"+TITLE+"' and date='"+DATE+"';");
        if(rs.next()){
            descField.setText(rs.getString(1));
        }
    }catch (SQLException ex) {
         errorMsg.errorMessage("Error","Database connection failed.Try again");
    }
    
    descField.setEditable(false);
    exitBtn.setOnAction(e->{
        showWindow.close();
    });
    //Styling containers
    hBox2.setAlignment(Pos.CENTER);
    hBox2.setPadding(new Insets(10,0,10,0));
    //Adding nodes to containers
    hBox1.getChildren().add(descField);
    hBox2.getChildren().add(exitBtn);
    vBox.getChildren().addAll(hBox1,hBox2);
    
    showScene=new Scene(vBox,300,200);
    showScene.getStylesheets().addAll("Light.css");
    showWindow.setScene(showScene);
    showWindow.showAndWait();
}
String copyImage(String path,String name){
    String workingDir=System.getProperty("user.dir")+"\\data";
    
    Path source=Paths.get(path);
    Path targetDir=Paths.get(workingDir);
        try {
            Files.createDirectories(targetDir);
        } catch (IOException ex) {
            errorMsg.errorMessage("Error","Can't create the necessary directory");
        }
    Path target=targetDir.resolve(source.getFileName());
    String format="";
    if(target.toString().endsWith(".JPEG")||target.toString().endsWith(".jpeg")){
        format=".jpeg";
    }
    if(target.toString().endsWith(".JPG")||target.toString().endsWith(".jpg")){
        format=".jpg";
    }
    if(target.endsWith(".PNG")||target.endsWith(".png")){
        format=".png";
    }
    try{
        Files.copy(source,target.resolveSibling(name+format),StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            errorMsg.errorMessage("Warning","Some error ocurred while trying to copy "+name+format+" to "+targetDir);
        }
    String storagePath=targetDir+"\\"+name+format;
    return storagePath.replace("\\","\\\\");
}
String getSex(String sex){
    if(sex.equals("Male")){
        return "M";
    }
    else{
        return "F";
    }
}
void clearTable(TableView table){
   for(int i=0;i<table.getItems().size();i++){
       table.getItems().clear();
   }
} 
static boolean bool=true;
boolean deleteRequest(String msg){
    Stage win=new Stage();
    win.initModality(Modality.APPLICATION_MODAL);
    win.setTitle("Confirm deletion");
    win.setOnCloseRequest(e->{
        bool=false;
    });
    Scene scene;
    //COntainers
    VBox vBox=new VBox();
    HBox hBox1=new HBox();
    HBox hBox2=new HBox();
    //Nodes
    final Label label=new Label(msg);
    final Button yesBtn=new Button("Yes");
    final Button noBtn=new Button("No");
    Image image=new Image("warning.png");
    ImageView imageView=new ImageView(image);
    //Setting noBtn as a default button
    yesBtn.setDefaultButton(true);
    yesBtn.setOnAction(e->{
        bool=true;
        win.close();
    });
    noBtn.setOnAction(e->{
        bool=false;
        win.close();
    });
    //Styling Nodes
    label.setFont(Font.font("Arial", 13));
    label.setGraphic(imageView);
    imageView.setFitWidth(30);
    imageView.setFitHeight(30);
    yesBtn.setPrefWidth(80);
    noBtn.setPrefWidth(80);
    //Styling containers
    hBox1.setAlignment(Pos.CENTER);
    hBox2.setAlignment(Pos.CENTER);
    hBox2.setSpacing(10);
    vBox.setAlignment(Pos.CENTER);
    vBox.setSpacing(10);
    //Adding nodes to container
    hBox1.getChildren().add(label);
    hBox2.getChildren().addAll(yesBtn,noBtn);
    vBox.getChildren().addAll(hBox1,hBox2);
        
    scene=new Scene(vBox,550,100);
    scene.setOnKeyPressed(e->{
        if(e.getCode()==KeyCode.ENTER){
            noBtn.fire();
        }
    });
    scene.getStylesheets().add("Light.css");
    win.setScene(scene);
    win.showAndWait();
    return bool;}
}



