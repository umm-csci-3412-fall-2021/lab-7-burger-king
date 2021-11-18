package segmentedfilesystem;

import static org.junit.Assert.*;

import java.net.DatagramPacket;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class PacketFactoryTest {

    DatagramPacket testingPacket;
    byte[] packetByteBuffer;
    PacketFactory factory = new PacketFactory();

    @Before
    public void setupDatagramPacket(){
        packetByteBuffer = new byte[1028];
        Arrays.fill(packetByteBuffer, (byte) 0);
        testingPacket = new DatagramPacket(packetByteBuffer, 1028);
    }

    @Test
    public void setClassCorrectly(){
        Packet testResult;

        testResult = factory.buildPacket(testingPacket);
        assertTrue(testResult instanceof HeaderPacket);

        packetByteBuffer[0] = (byte) 1;
        assertTrue(testResult instanceof DataPacket);
        assertFalse(((DataPacket) testResult).isFinal);

        packetByteBuffer[0] = (byte) 3;
        assertTrue(testResult instanceof DataPacket);
        assertTrue(((DataPacket) testResult).isFinal);
    }

    
}