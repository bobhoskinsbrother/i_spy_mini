package uk.co.itstherules;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.ds.ipcam.IpCamDeviceRegistry;
import com.github.sarxos.webcam.ds.ipcam.IpCamDriver;
import com.github.sarxos.webcam.ds.ipcam.IpCamMode;

import java.net.MalformedURLException;

public final class TankWebcamPanelBuilder {

    public TankWebcamPanelBuilder() {
        Webcam.setDriver(new IpCamDriver());
    }

    public TankWebcamPanelBuilder url(String url) throws MalformedURLException {
        IpCamDeviceRegistry.register("Tank", url, IpCamMode.PUSH);
        return this;
    }

    public WebcamPanel build() {
        WebcamPanel panel = new WebcamPanel(Webcam.getWebcams().get(0));
        panel.setFPSLimit(1);
        return panel;
    }
}
