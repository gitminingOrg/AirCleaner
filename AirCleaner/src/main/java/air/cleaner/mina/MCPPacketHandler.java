package air.cleaner.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import air.cleaner.device.service.DeviceReceiveService;
import air.cleaner.model.MCPPacket;
import air.cleaner.model.HeartbeatMCPPacket;

public class MCPPacketHandler extends IoHandlerAdapter{
	public static Logger LOG = LoggerFactory.getLogger(MCPPacketHandler.class);
	
	@Autowired
	private DeviceReceiveService deviceReceiveService;
	public void setDeviceReceiveService(DeviceReceiveService deviceReceiveService) {
		this.deviceReceiveService = deviceReceiveService;
	}

	@Override
	public void messageReceived(IoSession session, Object message){
	
		System.out.println("received message :"+message);
		if(message instanceof MCPPacket){
			if (message instanceof HeartbeatMCPPacket) {
				//receive heartbeat, update status
				LOG.debug("received status message :"+message);
				HeartbeatMCPPacket packet = (HeartbeatMCPPacket) message;
				deviceReceiveService.updateCacheCleanerStatus(packet);
				//send return packet
				packet.setLEN(new byte[]{0x01});
				packet.setDATA(new byte[]{0x00});
				session.write(packet);
			}
		}else{
			LOG.error("unrecognized message" + message);
		}
		
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		super.messageSent(session, message);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionClosed(session);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		// TODO Auto-generated method stub
		super.sessionIdle(session, status);
	}
}
