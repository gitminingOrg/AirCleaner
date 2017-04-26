package air.cleaner.device.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import air.cleaner.annotation.Command;
import air.cleaner.cache.CleanerStatusCacheManager;
import air.cleaner.cache.DeviceInfoCacheManager;
import air.cleaner.cache.SessionCacheManager;
import air.cleaner.model.CleanerStatus;
import air.cleaner.model.DeviceInfo;
import air.cleaner.model.HeartbeatMCPPacket;
import air.cleaner.model.MCPPacket;
import air.cleaner.utils.ByteUtil;
import air.cleaner.utils.MethodUtil;

@Service
public class DeviceReceiveService {
	public static Logger LOG = LoggerFactory.getLogger(DeviceReceiveService.class);
	
	@Autowired
	private CleanerStatusCacheManager cleanerStatusCacheManager;
	
	public void setCleanerStatusCacheManager(
			CleanerStatusCacheManager cleanerStatusCacheManager) {
		this.cleanerStatusCacheManager = cleanerStatusCacheManager;
	}
	
	@Autowired
	private DeviceInfoCacheManager deviceInfoCacheManager;
	public void setDeviceInfoCacheManager(
			DeviceInfoCacheManager deviceInfoCacheManager) {
		this.deviceInfoCacheManager = deviceInfoCacheManager;
	}
	
	@Autowired
	private SessionCacheManager sessionCacheManager;
	public void setSessionCacheManager(SessionCacheManager sessionCacheManager) {
		this.sessionCacheManager = sessionCacheManager;
	}
	
	/**
	 * update cleaner status in cache
	 * @param packet MCPPacket from device
	 */
	public void updateCacheCleanerStatus(HeartbeatMCPPacket packet){
		CleanerStatus cleanerStatus = packet.packetToCleanerStatus();
		boolean update = cleanerStatusCacheManager.updateCleanerStatus(cleanerStatus);
		if (!update) {
			LOG.warn("update cleaner status cache failed");
		}else {
			LOG.info("update succeed");
		}
	}
	
	/**
	 * update device info in cache
	 * @param packet
	 */
	public void updateCacheDeviceInfo(MCPPacket packet){
		int command = ByteUtil.byteArrayToInt(packet.getCID());
		long deviceID = ByteUtil.byteArrayToLong(packet.getUID());
		DeviceInfo deviceInfo = deviceInfoCacheManager.getDeviceInfo(deviceID);
		
		if (deviceInfo == null) {
			//////init device
			deviceInfo = new DeviceInfo();
		}
		Field[] fields = DeviceInfo.class.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Command.class)) {
				Command anno = field.getAnnotation(Command.class);
				if (anno.id() == command) {
					String methodName = MethodUtil.setFieldGetMethod(anno.name());
					if (field.getGenericType().getTypeName().equals("int")) {
						int value = ByteUtil.byteArrayToInt(packet.getDATA());
						try {
							Method method = deviceInfo.getClass().getDeclaredMethod(methodName, int.class);
							method.invoke(deviceInfo, value);
							deviceInfoCacheManager.updateDevice(deviceInfo);
						} catch (Exception e) {
							LOG.error("no such set method <int>" + methodName, e);
						}
					}else if (field.getGenericType().getTypeName().equals("java.lang.String")) {
						String value = ByteUtil.byteToServer(packet.getDATA());
						try {
							Method method = deviceInfo.getClass().getDeclaredMethod(methodName, String.class);
							method.invoke(deviceInfo, value);
							deviceInfoCacheManager.updateDevice(deviceInfo);
						} catch (Exception e) {
							LOG.error("no such set method <String>" + methodName, e);
						}
					}
					break;
				}
			}
		}
	}
	
	/**
	 * update a single attribute among cleaner status
	 * @param packet
	 */
	public void updateSingleCacheCleanerStatus(MCPPacket packet){
		int command = ByteUtil.byteArrayToInt(packet.getCID());
		LOG.info("控制指令为："+command);
		long deviceID = ByteUtil.byteArrayToLong(packet.getUID());
		CleanerStatus cleanerStatus = cleanerStatusCacheManager.getCleanerStatus(deviceID);
		
		if (cleanerStatus == null) {
			//////init status
		}
		Field[] fields = CleanerStatus.class.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Command.class)) {
				Command anno = field.getAnnotation(Command.class);
				if (anno.id() == command) {
					String methodName = MethodUtil.setFieldGetMethod(anno.name());
					if (field.getGenericType().getTypeName().equals("int")) {
						int value = ByteUtil.byteArrayToInt(packet.getDATA());
						try {
							Method method = cleanerStatus.getClass().getDeclaredMethod(methodName, int.class);
							method.invoke(cleanerStatus, value);
							cleanerStatusCacheManager.updateCleanerStatus(cleanerStatus);
						} catch (Exception e) {
							LOG.error("no such set method <int>" + methodName, e);
						}
					}else if (field.getGenericType().getTypeName().equals("java.lang.String")) {
						String value = ByteUtil.byteToServer(packet.getDATA());
						try {
							Method method = cleanerStatus.getClass().getDeclaredMethod(methodName, String.class);
							method.invoke(cleanerStatus, value);
							cleanerStatusCacheManager.updateCleanerStatus(cleanerStatus);
						} catch (Exception e) {
							LOG.error("no such set method <String>" +methodName, e);
						}
					}else if (field.getGenericType().getTypeName().equals("long")) {
						long value = ByteUtil.byteArrayToLong(packet.getDATA());
						try {
							Method method = cleanerStatus.getClass().getDeclaredMethod(methodName, long.class);
							method.invoke(cleanerStatus, value);
							cleanerStatusCacheManager.updateCleanerStatus(cleanerStatus);
						} catch (Exception e) {
							LOG.error("no such set method <long>" +methodName, e);
						}
					}
					break;
				}
			}
		}
	}
	
	public CleanerStatus getCleanerStatus(long deviceID){
		return cleanerStatusCacheManager.getCleanerStatus(deviceID);
	}
	
	/**
	 * get device info from cache... attention ! first update!
	 * @param deviceID
	 * @return
	 */
	public DeviceInfo getDeviceInfo(long deviceID){
		//////// u should 1st update
		return deviceInfoCacheManager.getDeviceInfo(deviceID);
	}
}
