package t120b180.mq.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Helper for object serialization/deserialization.<br/>
 * <br/>
 * Static members are thread safe, instance members are not.
 */
public class SerializationUtil {
	/**
	 * Serialize given object to byte array.
	 * @param obj Object to serialize.
	 * @return Resulting byte array.
	 * @throws IOException 
	 */
	public static byte[] serialize(Object obj) throws IOException {
		//validate inputs
		assert obj != null : "Argument 'obj' is null.";
		
		//create output streams
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
		
		//
		try {
			//serialize
			objectStream.writeObject(obj);
			
			//get result
			return byteStream.toByteArray();
		}
		finally {
			//release resources
			objectStream.flush();
			objectStream.close();
			
			byteStream.flush();
			byteStream.close();
		}
	}
	
	/**
	 * Deserialize givn byte array into object.
	 * @param bytes Byte array to deserialize.
	 * @return Resulting object
	 * @throws IOException
	 */
	public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
		//validate inputs
		assert bytes != null : "Argument 'bytes' is null.";
		
		//create input streams
		ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
		ObjectInputStream objectStream = new ObjectInputStream(byteStream);
		
		//
		try {
			//deserialize
			return objectStream.readObject();
		}
		finally {
			//release resources
			objectStream.close();
			byteStream.close();
		}
	}
}
