package icpc_club_management;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class CloseRequest {
    static boolean bool=false;
    boolean closeWindow(){
        Stage win=new Stage();
        win.initModality(Modality.APPLICATION_MODAL);
        win.setTitle("Close window");
        win.setResizable(false);
        win.setOnCloseRequest(e->{
            bool=false;
        });
        Scene scene;
        //COntainers
        VBox vBox=new VBox();
        HBox hBox1=new HBox();
        HBox hBox2=new HBox();
        //Nodes
        final Label label=new Label("Are you sure do you want to close the window?");
        final Button yesBtn=new Button("Yes");
        final Button noBtn=new Button("No");
        Image image=new Image("warning.png");
        ImageView imageView=new ImageView(image);
        //Setting noBtn as a default button
        noBtn.setDefaultButton(true);
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
        
        scene=new Scene(vBox,400,100);
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
