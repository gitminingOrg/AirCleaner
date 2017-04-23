package air.cleaner.cache;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Repository;

@Repository
public class SessionCacheManager {
	public static final String SESSION_ = "session."; 
	
	private Map<String, IoSession> sessionMap = new HashMap<String, IoSession>();

	public IoSession getSession(long deviceID){
		String key = SESSION_+deviceID;
		IoSession session = (IoSession) sessionMap.get(key);
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
}
