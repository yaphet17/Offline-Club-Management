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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
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


public class Member extends Helper{
    private static String idM;
    private databaseConnection dataBase=new databaseConnection();
    private displayError errorMsg=new displayError();
    private checkData checkData=new checkData();
    Stage profileWindow=new Stage();
    Stage eventWindow=new Stage();
    Stage  passWindow=new Stage();
    Rectangle2D visualBounds=Screen.getPrimary().getVisualBounds();

public Scene MemberPage(String i){
    Scene memberScene;
    idM=i;
    Pane pane=new Pane();
    TabPane mTab=new TabPane();
    mTab.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    mTab.setPrefWidth(700);
    Tab profile=new Tab("Profile");
    Tab event=new Tab("Event");
    
    //Containers
    BorderPane borderPane=new BorderPane();
    VBox vBox=new VBox();
    HBox hBox1=new HBox();
    HBox hBox2=new HBox();
    
    profile.setContent(profileTab(idM));
    event.setContent(eventPage());
    mTab.getTabs().addAll(profile,event);
     
    pane.setLayoutX(0);
    pane.setLayoutY(0);
    pane.getChildren().addAll(mTab);
    memberScene=new Scene(pane,700,600);
    memberScene.getStylesheets().addAll("Light.css");
    
   return memberScene;}

private VBox eventPage(){
   //Containers
    VBox vBox=new VBox();
    HBox hBox=new HBox();
   //Nodes
    final Button showBtn=new Button("Show description");
    final Button exitBtn=new Button("Logout");
    
    //Installing tooltip to a button
    showBtn.setTooltip(new Tooltip("Show detail information\nabout selected event"));
    
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
    
    //setting button on action
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
    exitBtn.setOnAction(e->{
        window.setScene(mainScene);
    });
    //Styling nodes
    showBtn.setPrefWidth(80);
    exitBtn.setPrefWidth(80);
    //Stylig conatianers
    hBox.setAlignment(Pos.CENTER_RIGHT);
    hBox.setPadding(new Insets(20,10,0,0));
    hBox.setSpacing(10);
    //Adding nodes to conatinesr
    hBox.getChildren().addAll(showBtn,exitBtn);
    vBox.getChildren().addAll(table,hBox);
    
    return vBox; 
}


}