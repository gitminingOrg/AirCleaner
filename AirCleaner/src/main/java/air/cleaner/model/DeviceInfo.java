package air.cleaner.model;

import java.io.Serializable;

import air.cleaner.annotation.Command;
import air.cleaner.utils.Constant;

public class DeviceInfo implements Serializable{

	private static final long serialVersionUID = -8263670463998041046L;
	
	private long deviceID;
	@Command(id=0x01, name=Constant.SERVER_IP,length=0x14)
	private String serverIP;
	@Command(id=0x02, name=Constant.SERVER_PORT,length=0x02)
	private int serverPort;
	@Command(id=0x03, name=Constant.HEARTBEAT_GAP,length=0x02)
	private int heartbeatGap;
	@Command(id=0x09, name=Constant.CYCLE,length=0x01)
	private int cycle;
	@Command(id = 0x0A, name = Constant.FIRMWARE, length = 0x14)
	private String firmware;
	@Command(id = 0x0B, name = Constant.HARDWARE, length = 0x14)
	private String hardware;
	public long getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(long deviceID) {
		this.deviceID = deviceID;
	}
	public String getServerIP() {
		return serverIP;
	}
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	public int getHeartbeatGap() {
		return heartbeatGap;
	}
	public void setHeartbeatGap(int heartbeatGap) {
		this.heartbeatGap = heartbeatGap;
	}

	public String getFirmware() {
		return firmware;
	}
	public void setFirmware(String firmware) {
		this.firmware = firmware;
	}
	public String getHardware() {
		return hardware;
	}
	public void setHardware(String hardware) {
		this.hardware = hardware;
	}
	public int getServerPort() {
		return serverPort;
	}
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	public int getCycle() {
		return cycle;
	}
	public void setCycle(int cycle) {
		this.cycle = cycle;
	}

}
