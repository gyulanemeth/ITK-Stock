package hu.ppke.itk.itkStock.client.watcherClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 * This class contains useful functions for sending objects through a
 * <code>nio</code> channel.
 */
public abstract class SerializationTools {

    /**
     * Converts an object to a byte array.
     *
     * @param o
     *            the object to be converted.
     * @return the object represented in bytes.
     */
    public static byte[] objectToBytes(Object o) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        byte[] bytes = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(o);
            bytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bytes;

    }

    /**
     * Convert a byte array to an object.
     *
     * @param bytes
     *            the byte array to be converted.
     * @return the represented object.
     */
    public static Object bytesToObject(byte[] bytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        Object o = null;
        try {
            in = new ObjectInputStream(bis);
            o = in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return o;
    }

}
