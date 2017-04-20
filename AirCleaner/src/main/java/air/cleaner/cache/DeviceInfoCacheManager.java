package air.cleaner.cache;

import net.spy.memcached.MemcachedClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import air.cleaner.model.DeviceInfo;

@Repository
public class DeviceInfoCacheManager {
	public static final String DEVICE_ = "device."; 
	@Autowired
	private MemcachedClient memcachedClient;
	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

	public DeviceInfo getDeviceInfo(long deviceID){
		String key = DEVICE_+deviceID;
		DeviceInfo deviceInfo = (DeviceInfo) memcachedClient.get(key);
		return deviceInfo;
	}
	
	public boolean updateDevice(DeviceInfo deviceInfo){
		if (deviceInfo.getDeviceID() < 0) {
			return false;
		}
		String key = DEVICE_+deviceInfo.getDeviceID();
		memcachedClient.replace(key, 0, deviceInfo);
		return true;
	}
}