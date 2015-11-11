package uk.co.itstherules;

import com.github.sarxos.webcam.WebcamPanel;
import uk.co.itstherules.wifi.TankRemoteControl;

import javax.swing.*;
import java.net.MalformedURLException;

public final class TankApp {

    private static final String URL = "http://10.10.1.1:8196";


    public static void main(String[] args) throws MalformedURLException {
        WebcamPanel panel = setupWebcamPanel();
        JFrame frame = setupFrame(panel);
        setupActionsForKeys(frame);
    }

    private static JFrame setupFrame(WebcamPanel panel) {
        JFrame frame = new JFrame("I Spy Tank");
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return frame;
    }

    private static WebcamPanel setupWebcamPanel() throws MalformedURLException {
        return new TankWebcamPanelBuilder().url(URL).build();
    }

    private static void setupActionsForKeys(JFrame frame) {
        final TankRemoteControl tankRemoteControl = new TankRemoteControl();
        final SwingTankControlMapper swingTankControlMapper = new SwingTankControlMapper(tankRemoteControl);
        frame.addKeyListener(swingTankControlMapper);
    }

}