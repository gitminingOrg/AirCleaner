package air.cleaner.cache;

import net.spy.memcached.MemcachedClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import air.cleaner.model.CleanerStatus;

@Repository
public class CleanerStatusCacheManager {
	@Autowired
	private MemcachedClient memcachedClient;
	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

	/**
	 * get cleaner status 
	 * @param cleanerID
	 * @return
	 */
	public CleanerStatus getCleanerStatus(long cleanerID){
		String key = "status."+cleanerID;
		CleanerStatus status = (CleanerStatus) memcachedClient.get(key);
		return status;
	}
	
	/**
	 * update cleaner status in memcached
	 * @param cleanerStatus
	 * @return
	 */
	public boolean updateCleanerStatus(CleanerStatus cleanerStatus){
		long deviceID = cleanerStatus.getDeviceID();
		if (deviceID < 0) {
			return false;
		}
		String key = "status."+deviceID;
		memcachedClient.replace(key, 0, cleanerStatus);
		return true;
	}
}
