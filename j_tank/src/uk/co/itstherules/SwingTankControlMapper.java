package uk.co.itstherules;

import uk.co.itstherules.wifi.TankRemoteControl;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static java.awt.event.KeyEvent.*;
import static uk.co.itstherules.wifi.TankRemoteControl.Movement.*;

public class SwingTankControlMapper implements KeyListener {

    private final TankRemoteControl remoteControl;

    public SwingTankControlMapper(TankRemoteControl remoteControl) {
        this.remoteControl = remoteControl;
    }

    public void keyTyped(KeyEvent e) {  }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case VK_W:
                remoteControl.move(FORWARD);
                break;
            case VK_A:
                remoteControl.move(LEFT);
                break;
            case VK_D:
                remoteControl.move(RIGHT);
                break;
            case VK_S:
                remoteControl.move(BACK);
                break;
            case VK_R:
                remoteControl.move(CAMERA_UP);
                break;
            case VK_F:
                remoteControl.move(CAMERA_DOWN);
                break;
            default:
                remoteControl.move(RESET);
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == VK_R || e.getKeyCode() == VK_F) {
            remoteControl.move(CANCEL_CAMERA_MOVE);
        } else {
            remoteControl.move(RESET);
        }
    }
}
