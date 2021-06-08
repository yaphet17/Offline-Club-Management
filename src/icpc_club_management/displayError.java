package icpc_club_management;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class displayError {

public static void errorMessage(String title,String content){
    Stage errorWindow=new Stage();
    errorWindow.setTitle(title);
    errorWindow.initModality(Modality.APPLICATION_MODAL);
    Scene errorScene;
    //Conatiners
    VBox vBox=new VBox();
    HBox hBox1=new HBox();
    HBox hBox2=new HBox();
    //Nodes
    final Label errorL=new Label(content);
    final Button okBtn=new Button("Ok");
    Image image=new Image("warning.png");
    Image image2=new Image("check.png");
    ImageView imageView;
    //Setting okBtn as a default button
    okBtn.setDefaultButton(true);
    
    if(title.equalsIgnoreCase("Notification")){
        imageView=new ImageView(image2);}
    else{
        imageView=new ImageView(image);
    }
    //setting nodes to action
    okBtn.setOnAction(e->{
        errorWindow.close();
    });
    errorL.setGraphic(imageView);
    imageView.setFitWidth(30);
    imageView.setFitHeight(30);
    //Styling Nodes
    errorL.setFont(Font.font("Arial", 15));
    //Styling containers
    vBox.setAlignment(Pos.CENTER);
    vBox.setSpacing(10);
    hBox1.setAlignment(Pos.CENTER);
    hBox2.setAlignment(Pos.CENTER);
    //Adding nodes containers
    hBox1.getChildren().add(errorL);
    hBox2.getChildren().add(okBtn);
    vBox.getChildren().addAll(hBox1,hBox2);
    
    errorScene=new Scene(vBox,400,100);
    errorScene.getStylesheets().addAll("Light.css");
    errorWindow.setScene(errorScene);
    errorWindow.showAndWait();
}
}
