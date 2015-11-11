package uk.co.itstherules.wifi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.Executors;

public class TankRemoteControl {

    public static final String TANK_IP_ADDRESS = "10.10.1.1";
    public static final int TANK_PORT = 8150;
    private Socket commandSocket;
    private BufferedReader input;
    private boolean isCurrentlyReading;
    private DataOutputStream output;
    private int reconnectAttempts = 0;

    public enum Movement {
        LEFT("1221"),
        RIGHT("1122"),
        BACK("1222"),
        FORWARD("1121"),
        RESET("1020"),
        CAMERA_UP("31"),
        CANCEL_CAMERA_MOVE("30"),
        CAMERA_DOWN("32");
        private final byte[] movement;

        Movement(String movement) {
            this.movement = movement.getBytes();
        }

        byte[] command() {
            return movement;
        }
    }

    private class RemoteControlReader implements Runnable {

        private final BufferedReader input;

        public RemoteControlReader(BufferedReader input) {
            this.input = input;
        }

        public void run() {
            while (TankRemoteControl.this.isCurrentlyReading) {
                char[] buf = new char[100];
                try {
                    int current = input.read(buf);
                    if (current > 0) {
                        buf[current] = '\u0000';
                        System.out.print(String.valueOf(buf, 0, current));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public TankRemoteControl() {
        commandSocket = null;
        input = null;
        output = null;
        isCurrentlyReading = false;
    }

    public void connect() throws IllegalStateException {
        if (!isConnected()) {
            try {
                System.out.println("Connecting to: " + TANK_IP_ADDRESS + ":" + TANK_PORT);
                commandSocket = new Socket(TANK_IP_ADDRESS, TANK_PORT);
                input = new BufferedReader(new InputStreamReader(commandSocket.getInputStream()));
                output = new DataOutputStream(commandSocket.getOutputStream());
                isCurrentlyReading = true;
                Executors.newSingleThreadExecutor().execute(new RemoteControlReader(input));
                reconnectAttempts = 0;
            } catch (NumberFormatException | IOException e) {
                System.err.println("Cannot connect to the tank, retrying in a second");
                System.err.println(e.getMessage());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
                if (reconnectAttempts > 5) {
                    throw new IllegalStateException("Cannot connect after 5 attempts");
                } else {
                    reconnect();
                    reconnectAttempts += 1;
                }
            }
        }
    }

    public void close() {
        if (isConnected()) {
            try {
                isCurrentlyReading = false;
                commandSocket.close();
                if (input != null) {
                    input.close();
                    input = null;
                }
                if (output != null) {
                    output.close();
                    output = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        return commandSocket != null && commandSocket.isConnected();
    }

    public void move(Movement movement) {
        byte[] command = movement.command();
        try {
            if (!isConnected()) {
                reconnect();
            }
            writeCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeCommand(byte[] command) throws IOException {output.write(command);}

    public void reconnect() {
        close();
        connect();
    }

}
