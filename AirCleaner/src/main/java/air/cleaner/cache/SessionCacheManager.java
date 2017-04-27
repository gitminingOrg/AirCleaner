package air.cleaner.cache;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class SessionCacheManager {
	public static final String SESSION_ = "session."; 
	public static Logger LOG = LoggerFactory.getLogger(SessionCacheManager.class);
	
	private Map<String, IoSession> sessionMap = new HashMap<String, IoSession>();

	public IoSession getSession(long deviceID){
		String key = SESSION_+deviceID;
		IoSession session = (IoSession) sessionMap.get(key);
		if (session == null) {
			return null;
		}else if (! session.isConnected() || session.isClosing()) {
			LOG.error("client has already been disconnected:  "+ deviceID);
			removeSession(deviceID);
			return null;
		}
		return session;
	}
	
	public boolean updateSession(long deviceID, IoSession session){
		String key = SESSION_+deviceID;
		if (sessionMap.get(key) != null) {
			sessionMap.replace(key, session);
		}else{
			sessionMap.put(key, session);
		}
		return true;
	}
	
	public boolean removeSession(long deviceID){
		IoSession session =  sessionMap.remove(deviceID);
		if (session != null) {
			return true;
		}
		return false;
	}
}
