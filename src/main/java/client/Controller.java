package client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TextArea displayMsgArea;
    @FXML
    private TextArea displayTotalUsersOnlineArea;
    @FXML
    private TextField sendMsgTextField;
    @FXML
    private Label userCountLabel;

    private String senderMsg;
    private String loginUsername;
    private ClientServer clientServer;
    private static String incomingMsg = "\n";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayTotalUsersOnlineArea.setEditable(false);
        displayMsgArea.setEditable(false);
    }

    public void init(ClientServer clientServer) {
        this.clientServer = clientServer;
    }

    // Function to set the send Msg text
    @FXML
    public void setSendBtn() {
        senderMsg = sendMsgTextField.getText();
        String messageHeader = "msgHeader:=>";
        String myConnectionId = clientServer.getConnectionID();
        String stringToSend = messageHeader + myConnectionId + "=>concat<=" + senderMsg;
        clientServer.writeMessage(stringToSend);
        sendMsgTextField.setText("");
    }

    public void createLoginDialog() {
        // Create dialog.
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Login");
        dialog.setHeaderText("Please insert your Username");


        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create  username and password
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField username = new TextField();
        username.setPromptText("Username");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);

        // disable login when no username is entered
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Enable Login Button if there's username
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        //  focus on the username field by default.
        Platform.runLater(() -> username.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return username.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        if(result.isPresent()) {
            loginUsername = result.get();
            System.out.println(loginUsername);
        } else {
            createLoginDialog();
        }
    }

    // Function to return username
    public String getLoginUsername() {
        return this.loginUsername;
    }

    public void updateOnlineUser(List<String> username) {
        Platform.runLater(()->{
            String usernameToDisplay = "";
            for(int i = 1; i<username.size(); i++) {
                usernameToDisplay = usernameToDisplay + "\n" + username.get(i);
            }
            userCountLabel.setText(Integer.toString(username.size() - 1));
            displayTotalUsersOnlineArea.setText(usernameToDisplay);
        });
    }

    public void displayMsg(String username, String msg) {
        String msgConfig = username + ": " + msg + "\n";
        incomingMsg = incomingMsg + msgConfig;
        displayMsgArea.setText(incomingMsg);
    }
}
