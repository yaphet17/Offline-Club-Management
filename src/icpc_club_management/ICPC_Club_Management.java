package icpc_club_management;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.text.Font;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.security.crypto.bcrypt.BCrypt;
import static org.springframework.security.crypto.bcrypt.BCrypt.gensalt;


public class ICPC_Club_Management extends Application {
    public static Stage window;//main window
    private databaseConnection dataBase=new databaseConnection();
    private checkData checkData=new checkData();
    private CloseRequest closeReq=new CloseRequest();
    static Scene mainScene;
    static Scene signScene;
    final Rectangle2D visualBounds=Screen.getPrimary().getVisualBounds();
    Administrator ad=new Administrator();
    Member mb=new Member();
    Committee cm=new Committee();
    displayError errorMsg=new displayError();
    private static String sx="";
    private static int accesslevel=0;
    
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        window=primaryStage;
        window.setTitle("ICPC Club Management");
        window.setResizable(false);
        window.setOnCloseRequest(e->{
            e.consume();
            if(closeReq.closeWindow()){
                window.close();
            }
        });
        //containers
        BorderPane borderPane=new BorderPane();
        ScrollPane scroll=new ScrollPane();
        VBox vBox=new VBox();
        VBox vBox1=new VBox();
        HBox hBox0=new HBox();
        HBox hBox1=new HBox();
        HBox hBox2=new HBox();
        HBox hBox3=new HBox();
        //Nodes
        final Label welcome=new Label("Welcome To ICPC Club");
        final Button signBtn=new Button("Signup");
        final Button logBtn=new Button("Login");
        final TextArea textArea=new TextArea();
        final Hyperlink emailLink=new Hyperlink("Email");
        final Hyperlink fbLink=new Hyperlink("FaceBook");
        final Hyperlink tgLink=new Hyperlink("Telegram");
        Desktop desktop=Desktop.getDesktop();
        Image image=new Image("icpc_image.png");
        ImageView imageView=new ImageView(image);
        Image emailImage=new Image("gmail.png");
        ImageView emailView=new ImageView(emailImage);
        Image tgImage=new Image("telegram.png");
        ImageView tgView=new ImageView(tgImage);
        Image fbImage=new Image("facebook.png");
        ImageView fbView=new ImageView(fbImage);
        
        
        TextFlow textFlow=new TextFlow();
        Text text1=new Text("The ICPC traces its roots to 1970 when the first competition was hosted by "
                + "pioneers of the Alpha Chapter of the UPE Computer Science Honor Society. The initiative"
                + " spread quickly within the United States and Canada as an innovative program to raise increase"
                + " ambition, problem-solving aptitude, and opportunities of the strongest students in the field of computing.\n" +
                "Over time, the contest evolved into a multi-tier competition with the first championship round conducted in 1977. "
                + "Since then, the contest has expanded into a worldwide collaborative of universities hosting regional competitions "
                + "that advance teams to the annual global championship round, the ICPC World Finals.\n" +"\n" +"The International "
                + "Collegiate Programming Contest (ICPC) is the premier global programming competition conducted by and for the world’s "
                + "universities. The ICPC is affiliated with the ICPC Foundation and is headquartered at Baylor University.The contest "
                + "fosters creativity, teamwork, and innovation in building new software programs, and enables students to test their "
                + "ability to perform under pressure. The contest has raised aspirations and performance of generations of the world’s "
                + "problem solvers in the computing sciences and engineering.\n" +"\n" +"The ICPC features several levels of competition:\n" +
                "-    Local Contests\n" +
                "-   Regional Contests\n" +
                "-   Regional Championships\n" +
                "-   The World Finals");
        //Installing tootip to button
        signBtn.setTooltip(new Tooltip("Create account"));
        logBtn.setTooltip(new Tooltip("Login to account"));
        signBtn.setOnAction(e->{
            window.setScene(signUp());
            window.setX((visualBounds.getMaxX()/2)-(window.getWidth()/2));
            window.setY(100);
        });
        logBtn.setOnAction(e->{
            window.setScene(logIn());
            window.setX((visualBounds.getMaxX()/2)-(window.getWidth()/2));
            window.setY(100);
        });
        tgLink.setOnAction(e->{
             try {
                desktop.browse(new URI("https://t.me/CSEC_ASTU"));
            } catch (URISyntaxException ex) {
                Logger.getLogger(ICPC_Club_Management.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ICPC_Club_Management.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        fbLink.setOnAction(e->{
            try {
                desktop.browse(new URI("https://ift.tt/22PEWePp"));
            } catch (URISyntaxException ex) {
                Logger.getLogger(ICPC_Club_Management.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ICPC_Club_Management.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        emailLink.setOnAction(e->{
            try {
                desktop.browse(new URI("mailto:astu.csec@gmail.com"));
            } catch (URISyntaxException ex) {
                Logger.getLogger(ICPC_Club_Management.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ICPC_Club_Management.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        //Styling nodes
        welcome.setFont(Font.font("Roboto", 30));
        welcome.setTextFill(Color.web("#FFFFFF"));
        emailView.setFitWidth(15);
        emailView.setFitHeight(15);
        tgView.setFitWidth(15);
        tgView.setFitHeight(15);
        fbView.setFitHeight(15);
        fbView.setFitWidth(15);
        logBtn.setPrefWidth(80);
        signBtn.setPrefWidth(80);
    
        text1.setFont(Font.font("Arial", 18));
        emailLink.setGraphic(emailView);
        tgLink.setGraphic(tgView);
        fbLink.setGraphic(fbView);
       
        
        //Styling Controls
        vBox.setStyle("-fx-background-color:linear-gradient(to right, #f7b733, #fc4a1a)");
        vBox1.setStyle("-fx-background-color:#fdfff5");
        borderPane.prefWidthProperty().bind(window.widthProperty());
        scroll.maxWidthProperty().bind(borderPane.widthProperty());
        textArea.prefWidthProperty().bind(scroll.widthProperty());
        textArea.prefHeightProperty().bind(scroll.heightProperty());
        vBox.prefWidthProperty().bind(borderPane.widthProperty());
        vBox1.prefWidthProperty().bind(borderPane.widthProperty());
        vBox1.setPadding(new Insets(0,0,0,20));
        hBox0.setSpacing(20);
        hBox0.setAlignment(Pos.CENTER);
        hBox1.prefWidthProperty().bind(vBox.widthProperty());
        hBox2.prefWidthProperty().bind(vBox.widthProperty());
        hBox3.prefWidthProperty().bind(borderPane.widthProperty());
        hBox1.setAlignment(Pos.CENTER);
        hBox1.setPadding(new Insets(20,0,0,0));
        hBox2.setAlignment(Pos.CENTER_RIGHT);
        hBox2.setSpacing(10);
        hBox2.setPadding(new Insets(0,20,20,0));
        hBox3.setAlignment(Pos.CENTER_RIGHT);
        hBox2.setSpacing(10);
        hBox3.setPadding(new Insets(0,20,20,0));
        //Adding nodes to containers
        hBox0.getChildren().add(imageView);
        textFlow.getChildren().addAll(text1);
        vBox1.getChildren().addAll(hBox0,textFlow);
        scroll.setContent(vBox1);
        hBox1.getChildren().addAll(welcome);
        hBox2.getChildren().addAll(signBtn,logBtn);
        hBox3.getChildren().addAll(emailLink,tgLink,fbLink);
        vBox.getChildren().addAll(hBox1,hBox2);
        borderPane.setTop(vBox);
        borderPane.setCenter(scroll);
        borderPane.setBottom(hBox3);
        
        mainScene=new Scene(borderPane,700,660);
        mainScene.getStylesheets().addAll("Light.css");
        window.setScene(mainScene);
        window.show(); 
        
    }
protected Scene signUp(){
    
    //containers
    VBox vBox=new VBox();
    HBox hBox=new HBox();
    HBox hBox0=new HBox();
    HBox hBox1=new HBox();
    HBox hBox2=new HBox();
    HBox hBox3=new HBox();
    HBox hBox4=new HBox();
    HBox hBox5=new HBox();
    HBox hBox6=new HBox();
    HBox hBox7=new HBox();
    HBox hBox8=new HBox();
    HBox hBox9=new HBox();
    HBox hBox10=new HBox();
    HBox hBox11=new HBox();
    
    //Nodes
    final Label signL=new Label("Sign Up");
    final Label notifyL=new Label();
    final TextField fnameField=new TextField();           
    final TextField lnameField=new TextField();           
    final TextField idField=new TextField();             
    final PasswordField passField=new PasswordField();    
    final PasswordField confirmField=new PasswordField(); 
    final RadioButton maleRadio=new RadioButton("Male");
    final RadioButton femaleRadio=new RadioButton("Female");
    final ToggleGroup tg=new ToggleGroup();
    final ComboBox<String> departCombo=new ComboBox<>();
    final ComboBox<String> yearCombo=new ComboBox<>();
    final TextField emailField=new TextField();  
    final TextField phoneField=new TextField();
    final Button signBtn=new Button("SignUp");
    final Button backBtn=new Button("Back");
    Image image=new Image("signup.png");
    ImageView imageView=new ImageView(image);
    
    
    //set prompt text
    fnameField.setPromptText("First Name");
    lnameField.setPromptText("Last Name");
    idField.setPromptText("Id Number");
    passField.setPromptText("Password");
    confirmField.setPromptText("Confirm Password");
    emailField.setPromptText("Email Address");
    phoneField.setPromptText("Phone Number(09--------)");
    departCombo.setPromptText("Department");
    yearCombo.setPromptText("Academic Year");
    
    //Makign signup button a default button
    //to fire it whenever an enter is pressed in keyboard
    signBtn.setDefaultButton(true);
    
    //Adding radio buttons to toggle group
    maleRadio.setToggleGroup(tg);
    femaleRadio.setToggleGroup(tg);
    
    //Set contents to comboxs
    departCombo.getItems().addAll("CSE","ECE","EPCE");
    yearCombo.getItems().addAll("1","2","3","4","5");
    
    //Checking accesslevel from database
     try {
        Statement st=dataBase.getConnection().createStatement();
        ResultSet rs=st.executeQuery("select * from members;");
        if(rs.next()){
            accesslevel=0;}
        else{
            accesslevel=2;}
        rs.close();
        st.close();
    }
    catch (SQLException ex) {
        }
    //Installing tootip to button
    signBtn.setTooltip(new Tooltip("Signup(Enter)"));
    backBtn.setTooltip(new Tooltip("Back to main page(Esc)"));
    //Setting buttons to action
    maleRadio.setOnAction(e->sx="M");
    femaleRadio.setOnAction(e->sx="F");
    signBtn.setOnAction(e->{   
    if(!fnameField.getText().equals("")&&!lnameField.getText().equals("")&&!idField.getText().equals("")&&
        !passField.getText().equals("")&&!confirmField.getText().equals("")&&(maleRadio.isSelected()||femaleRadio.
        isSelected())&&departCombo.getValue()!= null&&yearCombo.getValue()!= null){
        if(checkData.checkName(fnameField.getText())){
        if(checkData.checkName(lnameField.getText())){
        if(passField.getText().equals(confirmField.getText())){
        if(checkData.checkEmail(emailField.getText())){
        if(checkData.checkNumber(phoneField.getText())){
        try {
            notifyL.setText("");
            Statement st=dataBase.getConnection().createStatement();
            String query="insert into members values('"+idField.getText().trim()+"','"+fnameField.getText().
                          trim()+"','"+lnameField.getText().trim()+"','"+BCrypt.hashpw(passField.getText(),
                          gensalt(10))+"','"+sx+"','"+departCombo.getValue()+"','"+yearCombo.getValue()+"',"
                          + "'"+emailField.getText()+"',"+phoneField.getText()+","+accesslevel+",'"+LocalDate.now()+"');";
            st.execute(query);
            st.close();
            if(accesslevel==2){
                 window.setScene(ad.AdminPage(idField.getText()));
            }
            else if(accesslevel==1){
                 window.setScene(cm.committePage(idField.getText()));
            }
            else{
                window.setScene(mb.MemberPage(idField.getText()));
            }
            window.setX((visualBounds.getMaxX()/2)-(window.getWidth()/2));
            window.setY(50);
            
        } catch (SQLException ex) {
            errorMsg.errorMessage("Error","Database connection failed.Try again");
        }
        }else{notifyL.setText("Invalid Phone Number");}
        }else{notifyL.setText("Invalid Email Address");}
        }else{notifyL.setText("The password you entered don't match.");}
        }else{notifyL.setText("Invalid Last Name");}
        }else{notifyL.setText("Invalid First Name");}
    }
    else{
        notifyL.setText("Required field empty.Please fill all fields");
    }
    });
    backBtn.setOnAction(e->{
        window.setScene(mainScene);
        window.setX((visualBounds.getMaxX()/2)-(window.getWidth()/2));
        window.setY(0);
    });
    //Setting fields to check validation upon typing
    fnameField.setOnKeyReleased(e->{
         if(!checkData.checkName(fnameField.getText())){notifyL.setText("Invalid First Name");}
         if(e.getCode()!=KeyCode.ENTER&&checkData.checkName(fnameField.getText())){notifyL.setText("");}
     });
    lnameField.setOnKeyReleased(e->{
         if(!checkData.checkName(lnameField.getText())){notifyL.setText("Invalid Last Name");}
         if(e.getCode()!=KeyCode.ENTER&&checkData.checkName(lnameField.getText())){notifyL.setText("");}
     });
    passField.setOnKeyReleased(e->{
         if(!passField.getText().equals(confirmField.getText())){notifyL.setText("The password you entered don't match.");}
         if(e.getCode()!=KeyCode.ENTER&&passField.getText().equals(confirmField.getText())){notifyL.setText("");}
     });
    confirmField.setOnKeyReleased(e->{
         if(!confirmField.getText().equals(passField.getText())){notifyL.setText("The password you entered don't match.");}
         if(e.getCode()!=KeyCode.ENTER&&confirmField.getText().equals(passField.getText())){notifyL.setText("");}
     });
    emailField.setOnKeyReleased(e->{
         if(!checkData.checkEmail(fnameField.getText())){notifyL.setText("Invalid Email Address");}
         if((e.getCode()!=KeyCode.ENTER&&checkData.checkEmail(emailField.getText()))||emailField.getText().equals("")){notifyL.setText("");}
     });
    phoneField.setOnKeyReleased(e->{
         if(!checkData.checkNumber(fnameField.getText())){notifyL.setText("Invalid Phone Number");}
         if((e.getCode()!=KeyCode.ENTER&&checkData.checkNumber(phoneField.getText()))||phoneField.getText().equals("")){notifyL.setText("");}
     });
    signL.setGraphic(imageView);
    
    //Styling nodes
    signL.setFont(new Font("Roboto", 30));
    signL.setTextFill(Color.web("#62BBE3"));
    imageView.setFitWidth(20);
    imageView.setFitHeight(20);
    notifyL.setTextFill(Color.web("#F0360F"));
    fnameField.setPrefWidth(300);
    lnameField.setPrefWidth(300);
    idField.setPrefWidth(300);
    passField.setPrefWidth(300);
    confirmField.setPrefWidth(300);
    departCombo.setPrefWidth(145);
    yearCombo.setPrefWidth(145);
    emailField.setPrefWidth(300);
    phoneField.setPrefWidth(300);
    signBtn.setPrefWidth(300);
    backBtn.setPrefWidth(80);
    //Styling Controls
    vBox.prefWidthProperty().bind(window.widthProperty());
    vBox.prefHeightProperty().bind(window.heightProperty());
    vBox.setAlignment(Pos.CENTER);
    vBox.setSpacing(10);
    hBox.setAlignment(Pos.CENTER_LEFT);
    hBox0.setAlignment(Pos.CENTER);
    hBox.setPadding(new Insets(15,0,0,50));
    hBox1.setAlignment(Pos.CENTER);
    hBox2.setAlignment(Pos.CENTER);
    hBox3.setAlignment(Pos.CENTER);
    hBox4.setAlignment(Pos.CENTER);
    hBox5.setAlignment(Pos.CENTER);
    hBox6.setAlignment(Pos.CENTER);
    hBox6.setSpacing(10);
    hBox7.setAlignment(Pos.CENTER);
    hBox7.setSpacing(10);
    hBox8.setAlignment(Pos.CENTER);
    hBox9.setAlignment(Pos.CENTER);
    hBox10.setAlignment(Pos.CENTER);
    hBox10.setPadding(new Insets(10,0,0,0));
    hBox11.setAlignment(Pos.CENTER_RIGHT);
    hBox11.setPadding(new Insets(10,20,5,0));
    
    
    //Adding nodes to containers
    hBox.getChildren().add(signL);
    hBox0.getChildren().add(notifyL);
    hBox1.getChildren().addAll(fnameField);
    hBox2.getChildren().addAll(lnameField);
    hBox3.getChildren().addAll(idField);
    hBox4.getChildren().addAll(passField);
    hBox5.getChildren().addAll(confirmField);
    hBox6.getChildren().addAll(maleRadio,femaleRadio);
    hBox7.getChildren().addAll(departCombo,yearCombo);
    hBox8.getChildren().addAll(emailField);
    hBox9.getChildren().addAll(phoneField);
    hBox10.getChildren().addAll(signBtn);
    hBox11.getChildren().addAll(backBtn);
    vBox.getChildren().addAll(hBox,hBox0,hBox1,hBox2,hBox3,hBox4,hBox5,hBox6,hBox7,hBox8,hBox9,hBox10,hBox11);
    
    signScene=new Scene(vBox,450,510);
    //Setting scene to listen a keyboard input
    signScene.setOnKeyPressed(e->{
            if (e.getCode() == KeyCode.ESCAPE) {
                backBtn.fire();
            }
    });
    signScene.getStylesheets().addAll("Light.css");
      
return signScene;
}
protected Scene logIn(){
    Scene logScene;
    //Container
    VBox vBox=new VBox();
    HBox hBox=new HBox();
    HBox hBox1=new HBox();
    HBox hBox2=new HBox();
    HBox hBox3=new HBox();
    HBox hBox4=new HBox();
    
    //Nodes
    final Label logL=new Label("Login");
    final Label notifyL=new Label();
    final TextField ieField=new TextField();
    final PasswordField passField=new PasswordField();
    final Button logBtn=new Button("Login");
    final Button backBtn=new Button("Back");
    Image image=new Image("login.png");
    ImageView imageView=new ImageView(image);
    
    //set prompt text
    ieField.setPromptText("Email or Id Number");
    passField.setPromptText("Password");
    
    //Makign signup button a default button
    //to fire it whene an enter is pressed in keyboard
    logBtn.setDefaultButton(true);
    //Installing tootip to button
    logBtn.setTooltip(new Tooltip("Signup(Enter)"));
    backBtn.setTooltip(new Tooltip("Back to main page(Esc)"));
    //Settingnodes to action
    logBtn.setOnAction(e->{
         if(!ieField.getText().equals("")&&!passField.getText().equals(""))
         {
        try {
            Statement st=dataBase.getConnection().createStatement();
            ResultSet rs=st.executeQuery("select * from members;");
            while(rs.next())
            {
                String ieS=rs.getString(1);
                String passS=rs.getString(4);
                String emailS=rs.getString(8);
                String accessS=rs.getString(10);
                if((ieS.equalsIgnoreCase(ieField.getText().trim())||emailS.equalsIgnoreCase(ieField.getText()))&&BCrypt.checkpw(passField.getText(),passS))
                {
                    if(Integer.parseInt(accessS)==2){
                        window.setScene(ad.AdminPage(ieField.getText()));}
                    else if(Integer.parseInt(accessS)==1){
                        window.setScene(cm.committePage(ieField.getText()));
                    }
                   else{
                        window.setScene(mb.MemberPage(ieField.getText()));
                    }
                     window.setX((visualBounds.getMaxX()/2)-(window.getWidth()/2));
                     window.setY(50);
                    break;
                }
                else{
                    if(!ieS.equalsIgnoreCase(ieField.getText().trim())||emailS.equalsIgnoreCase(ieField.getText())){ 
                        notifyL.setText("Invalid id or email");
                    }
                    else{
                        notifyL.setText("Invalid password");
                    }  
                }
            }
            st.close();
            rs.close();
        }catch (SQLException ex) {
              errorMsg.errorMessage("Error","Database connection failed.Try again");
        }
    }
    else {
             if(ieField.getText().equals("")){
                 notifyL.setText("Id or email required");
             }
             else{
                 notifyL.setText("Password required");
             }
    }
    });
    backBtn.setOnAction(e->{
        window.setScene(mainScene);
        window.setX((visualBounds.getMaxX()/2)-(window.getWidth()/2));
        window.setY(0);
    });
    logL.setGraphic(imageView);
    
    //Styling nodes
    logL.setFont(new Font("Arial", 25));
    logL.setTextFill(Color.web("#62BBE3"));
    imageView.setFitWidth(20);
    imageView.setFitHeight(20);
    notifyL.setTextFill(Color.web("#F0360F"));
    notifyL.setPrefHeight(50);
    ieField.setPrefWidth(300);
    passField.setPrefWidth(300);
    logBtn.setPrefWidth(300);
    backBtn.setPrefWidth(80);
    
    
    //Styling containers
    vBox.prefWidthProperty().bind(window.widthProperty());
    vBox.prefHeightProperty().bind(window.heightProperty());
    vBox.setAlignment(Pos.CENTER);
    vBox.setSpacing(10);
    hBox.setAlignment(Pos.CENTER_LEFT);
    hBox.setPadding(new Insets(10,0,0,50));
    hBox1.setAlignment(Pos.CENTER);
    hBox1.setPrefHeight(50);
    hBox2.setAlignment(Pos.CENTER);
    hBox3.setAlignment(Pos.CENTER);
    hBox4.setAlignment(Pos.CENTER_RIGHT);
    hBox4.setPadding(new Insets(10,20,5,0)); 
    //Adding nodes to containers
    hBox.getChildren().add(logL);
    hBox1.getChildren().add(notifyL);
    hBox2.getChildren().add(ieField);
    hBox3.getChildren().add(passField);
    hBox4.getChildren().add(backBtn);
    vBox.getChildren().addAll(hBox,hBox1,hBox2,hBox3,logBtn,hBox4);
    
    logScene=new Scene(vBox,450,240);
    logScene.setOnKeyPressed(e->{
            if (e.getCode() == KeyCode.ESCAPE) {
                backBtn.fire();
            }
    });
    logScene.getStylesheets().addAll("Light.css");
return logScene;}


    
    
    
}
