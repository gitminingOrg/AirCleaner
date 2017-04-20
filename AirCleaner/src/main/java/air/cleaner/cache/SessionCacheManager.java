package air.cleaner.cache;

import net.spy.memcached.MemcachedClient;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SessionCacheManager {
	public static final String SESSION_ = "session."; 
	@Autowired
	private MemcachedClient memcachedClient;
	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}
	public IoSession getSession(long deviceID){
		String key = SESSION_+deviceID;
		IoSession session = (IoSession) memcachedClient.get(key);
		return session;
	}
	
	public boolean updateSession(long deviceID, IoSession session){
		String key = SESSION_+deviceID;
		if (memcachedClient.get(key) != null) {
			memcachedClient.touch(key, 0);
		}else{
			memcachedClient.add(key, 0, session);
		}
		return true;
	}
}
