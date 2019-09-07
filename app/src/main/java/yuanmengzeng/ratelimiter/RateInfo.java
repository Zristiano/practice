//package yuanmengzeng.ratelimiter;
//
//import java.nio.ByteBuffer;
//
//final class RateInfo {
//	/**
//	 * when structure of this class changes, the CURRENT_VERSION must increase by 1.
//	 * This field is used to check consistency between zookeeper's rate node and this class.
//	 */
//	public static final int CURRENT_VERSION = 1;
//	/**
//	 * This field must be stored in the first 4 slots when the class is transferred to byte[]
//	 */
//	int version;
//
//	/**
//	 * The time when the next request (no matter its size) will be granted. After granting a request,
//	 * this is pushed further in the future. Large requests push this further than small requests.
//	 */
//	long nextFreeTicketMicros = 0;
//
//	/**
//	 * number of available token after the latest request
//	 */
//	double storedToken = 0;
//
//	/**
//	 * number of token can be acquired within 1 second
//	 */
//	double tokenPerSecond = 0;
//
//	/**
//	 * the timestamp of the latest setting of {@link RateInfo#tokenPerSecond}
//	 */
//	long latestTpsMicros = 0;
//
//	// version: 4 bytes; nextFreeTicketMicros: 8 bytes
//	// storedToken: 8 bytes; tokenPerSecond: 8 bytes
//	// latestTpsMicros: 8 bytes
//	private static final int BYTE_SIZE = 36;
//
//	public static RateInfo fromByte(byte[] data){
//		if (data==null || data.length < 4){
//			return null;
//		}
//		RateInfo rateInfo = new RateInfo();
//		ByteBuffer byteBuffer = ByteBuffer.allocate(data.length);
//		byteBuffer.put(data);
//		rateInfo.version = byteBuffer.getInt(0);
//		if (rateInfo.version!=CURRENT_VERSION) {
//			return rateInfo;
//		}
//		rateInfo.nextFreeTicketMicros = byteBuffer.getLong(4);
//		rateInfo.storedToken = byteBuffer.getDouble(12);
//		rateInfo.tokenPerSecond = byteBuffer.getDouble(20);
//		rateInfo.latestTpsMicros = byteBuffer.getLong(28);
//		return rateInfo;
//	}
//
//	public static byte[] toByte(RateInfo rateInfo){
//		ByteBuffer byteBuffer = ByteBuffer.allocate(BYTE_SIZE);
//		byteBuffer.putInt(rateInfo.version);
//		byteBuffer.putLong(rateInfo.nextFreeTicketMicros);
//		byteBuffer.putDouble(rateInfo.storedToken);
//		byteBuffer.putDouble(rateInfo.tokenPerSecond);
//		byteBuffer.putLong(rateInfo.latestTpsMicros);
//		return byteBuffer.array();
//	}
//
//	@Override
//	public String toString() {
//		StringBuilder sb = new StringBuilder();
//		sb.append("{ ");
//		sb.append("version:").append(version).append(", ");
//		sb.append("nextFreeTicketMicros:").append(nextFreeTicketMicros).append(", ");
//		sb.append("storedToken:").append(storedToken).append(", ");
//		sb.append("tokenPerSecond:").append(tokenPerSecond).append(", ");
//		sb.append("latestTpsMicros:").append(latestTpsMicros);
//		sb.append(" }");
//		return sb.toString();
//	}
//}
