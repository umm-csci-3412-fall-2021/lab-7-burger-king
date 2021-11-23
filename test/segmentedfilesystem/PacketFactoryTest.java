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
        testResult = factory.buildPacket(testingPacket);
        assertTrue(testResult instanceof DataPacket);
        assertFalse(((DataPacket) testResult).isFinal);

        packetByteBuffer[0] = (byte) 3;
        testResult = factory.buildPacket(testingPacket);
        assertTrue(testResult instanceof DataPacket);
        assertTrue(((DataPacket) testResult).isFinal);
    }

    @Test
    public void setFileId(){
        Packet testResult;

        packetByteBuffer[1] = (byte) 3;

        testResult = factory.buildPacket(testingPacket);
        assertEquals((byte) 3, testResult.getFileID());
    }

    @Test
    public void setFileName(){
        Packet testResult;

        String testName = "Test fileName";
        byte[] testNameBytes = testName.getBytes();
        
        for(int i = 0; i < testNameBytes.length; i++){
            packetByteBuffer[i + 2] = testNameBytes[i];
        }

        testResult = factory.buildPacket(testingPacket);
        assertTrue(testResult instanceof HeaderPacket);
        assertEquals(testName, ((HeaderPacket) testResult).getFileName());

        String veryLongTestName = "This is an extremely and probably unrealistically long filename which will almost certainly take multiple bytes to store";
        byte[] veryLongTestBytes = veryLongTestName.getBytes();

        for(int i = 0; i < testNameBytes.length; i++){
            packetByteBuffer[i + 2] = veryLongTestBytes[i];
        }

        testResult = factory.buildPacket(testingPacket);
        assertTrue(testResult instanceof HeaderPacket);
        assertEquals(veryLongTestName, ((HeaderPacket) testResult).getFileName());
    }

    @Test
    public void setFullPacketBody(){
        Packet testResult;

        packetByteBuffer[0] = (byte) 1;
        byte[] testBody = new byte[1024];
        for(int i = 0; i < 1024; i++){
            testBody[i] = (byte) (i%254);
            packetByteBuffer[i + 4] = (byte) (i%254);
        }

        testResult = factory.buildPacket(testingPacket);
        assertTrue(testResult instanceof DataPacket);
        assertArrayEquals(testBody, ((DataPacket) testResult).getPacketBody());
    }
    
}