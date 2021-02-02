package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Loader extends Application{

//	static {
//		NativeMain.main(new String[0]);
//	}
	
    @Override
	public void start(final Stage primaryStage) throws Exception{
        final Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("LMDB Viewer");
        primaryStage.setScene(new Scene(root, 600, 400));

        final Image appIcon = new Image("file:icon.png");
        primaryStage.getIcons().add(appIcon);

        primaryStage.show();
    }


    public static void main(final String[] args) {

//    	NativeMain.main(args);
    	
//    	LibTest.check();
//    	JffiTest.lookupTypeInfo(NativeType.POINTER);
    	
    	System.out.println("Hello World!");
        launch(args);
    }


}

