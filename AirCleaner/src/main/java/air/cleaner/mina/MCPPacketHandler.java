package air.cleaner.mina;

import java.util.Set;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import air.cleaner.cache.SessionCacheManager;
import air.cleaner.device.service.DeviceReceiveService;
import air.cleaner.model.HeartbeatMCPPacket;
import air.cleaner.model.MCPPacket;
import air.cleaner.utils.ByteUtil;

import com.google.common.collect.ImmutableSet;

public class MCPPacketHandler extends IoHandlerAdapter{
	public static Logger LOG = LoggerFactory.getLogger(MCPPacketHandler.class);
	
	@Autowired
	private DeviceReceiveService deviceReceiveService;
	public void setDeviceReceiveService(DeviceReceiveService deviceReceiveService) {
		this.deviceReceiveService = deviceReceiveService;
	}
	@Autowired
	private SessionCacheManager sessionCacheManager;
    public void setSessionCacheManager(SessionCacheManager sessionCacheManager) {
		this.sessionCacheManager = sessionCacheManager;
	}
	public static Set<Integer> deviceSet = new ImmutableSet.Builder<Integer>().add(0x01).add(0x02).add(0x03).add(0x09).add(0xFE).add(0xFF).build();
    public static Set<Integer> statusSet = new ImmutableSet.Builder<Integer>().add(0x04).add(0x05).add(0x06).add(0x07).add(0x08).add(0x0A).build();

	@Override
	public void messageReceived(IoSession session, Object message){
	
		LOG.debug("received message :"+message);
		if(message instanceof MCPPacket){
			//update session of device in cache
			long deviceID = ByteUtil.byteArrayToLong(((MCPPacket) message).getUID());
			sessionCacheManager.updateSession(deviceID, session);
			
			if (message instanceof HeartbeatMCPPacket) {
				//receive heartbeat, update status
				LOG.debug("received status message :"+message);
				HeartbeatMCPPacket packet = (HeartbeatMCPPacket) message;
				deviceReceiveService.updateCacheCleanerStatus(packet);
				//send return packet
				packet.setLEN(new byte[]{0x01});
				packet.setDATA(new byte[]{0x00});
				packet.calCRC();
				session.write(packet);
			}else{
				//classify device info command and cleaner status command
				int command = ByteUtil.byteArrayToInt(((MCPPacket) message).getCID());
				if(deviceSet.contains(command)){
					deviceReceiveService.updateCacheDeviceInfo((MCPPacket)message);
					LOG.info("new message of device info" + message);
				}else if(statusSet.contains(command)){
					deviceReceiveService.updateSingleCacheCleanerStatus((MCPPacket)message);
					LOG.info("new message of cleaner status" + message);
				}else{
					LOG.error("unrecognized cid" + message);
				}
			}
		}else{
			LOG.error("unrecognized message" + message);
		}
		
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		LOG.info("Message sending .......... " +message);
		if (session.isClosing()) {
			LOG.error("Client has already been disconnected");
			return;
		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		LOG.debug("session closed" + session);
		CloseFuture closeFuture = session.close(true);
		closeFuture.addListener(new IoFutureListener<IoFuture>() {
			@Override
			public void operationComplete(IoFuture future) {
				if(future instanceof CloseFuture){
					((CloseFuture) future).setClosed();
					LOG.info("sessionClosed" + future.getSession());
				}
			}
		});
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		LOG.error("message deliever exception caught", cause);
		super.exceptionCaught(session, cause);
	}
}
