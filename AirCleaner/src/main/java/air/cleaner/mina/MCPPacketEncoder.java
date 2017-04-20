package air.cleaner.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import air.cleaner.model.MCPPacket;

public class MCPPacketEncoder extends ProtocolEncoderAdapter {

	@Override
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		 
		if(message instanceof MCPPacket){
			IoBuffer buffer = IoBuffer.allocate(19).setAutoExpand(true);
			byte[] items = ((MCPPacket) message).toByte();
			buffer.put(items);
			buffer.flip();
			out.write(buffer);
			out.flush();
		}
		
	}

}
