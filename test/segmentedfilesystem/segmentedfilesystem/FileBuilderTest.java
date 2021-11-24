package segmentedfilesystem;

import static org.junit.Assert.*;
//import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class FileBuilderTest {

    HeaderPacket hp;
    DataPacket body0;
    DataPacket body1;
    DataPacket bodyFinal;
    FileBuilder testingBuilder;

    @Before
    public void setupNamePacket(){
//        HeaderPacket hp = mock(HeaderPacket.class);
// Mockito is absolutely the right tool to use here; however, there isn't a clear package manager we can use to get it.
        
        hp = new HeaderPacket();
        hp.fileName = "TestingName";
    }

    @Before
    public void setupBodyPackets(){

        body0 = new DataPacket();
        body0.isFinal = false;
        body0.packetNumber = 0;
        body0.packetBody = "Firstly, ".getBytes();

        body1 = new DataPacket();
        body1.isFinal = false;
        body1.packetNumber = 1;
        body1.packetBody = "secondly, ".getBytes();

        bodyFinal = new DataPacket();
        bodyFinal.isFinal = true;
        bodyFinal.packetNumber = 2;
        bodyFinal.packetBody = "and finally.".getBytes();
    }

    @Before
    public void setupBuilder(){
        testingBuilder = new FileBuilder();
    }

    @Test
    public void takesFileName(){
        assertEquals(FileBuilder.FileBuilderStatus.AWAITING_NAME, testingBuilder.status);
        assertFalse(testingBuilder.isFinished());
        try{
            testingBuilder.addPacket(hp);
            assertEquals("TestingName", testingBuilder.fileName);
            assertEquals(FileBuilder.FileBuilderStatus.WRITING, testingBuilder.status);
        } catch (Exception e) {
            fail();
        }
        
    }

    @Test
    public void takesFirstPacket(){
        try{
            testingBuilder.addPacket(body0);
            assertEquals(0, testingBuilder.nextPacketNumber);
            assertEquals(FileBuilder.FileBuilderStatus.AWAITING_NAME, testingBuilder.status);
            assertEquals(1, testingBuilder.packetQueue.size());
        } catch (Exception e){
            fail();
        }
    }

    @Test
    public void writesOnePacket(){
        try {
            testingBuilder.addPacket(hp);
            testingBuilder.addPacket(body0);
            assertEquals(1, testingBuilder.nextPacketNumber);
            assertEquals(FileBuilder.FileBuilderStatus.WRITING, testingBuilder.status);
            assertEquals(0, testingBuilder.packetQueue.size());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void writesPacketDelayed(){
        try {
            testingBuilder.addPacket(body0);
            testingBuilder.addPacket(hp);
            assertEquals(1, testingBuilder.nextPacketNumber);
            assertEquals(FileBuilder.FileBuilderStatus.WRITING, testingBuilder.status);
            assertEquals(0, testingBuilder.packetQueue.size());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void holdsLaterPackets(){
        try{
            testingBuilder.addPacket(hp);
            testingBuilder.addPacket(body1);
            testingBuilder.addPacket(bodyFinal);
            assertEquals(0, testingBuilder.nextPacketNumber);
            assertEquals(2, testingBuilder.packetQueue.size());
        } catch (Exception e){
            fail();
        }
    }

    @Test
    public void writesQueuedPackets(){
        try{
            testingBuilder.addPacket(hp);
            testingBuilder.addPacket(body1);
            testingBuilder.addPacket(body0);
            assertEquals(2, testingBuilder.nextPacketNumber);
            assertEquals(0, testingBuilder.packetQueue.size());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void writesMultipleQueued(){
        try{
            testingBuilder.addPacket(hp);
            testingBuilder.addPacket(body1);
            testingBuilder.addPacket(bodyFinal);
            testingBuilder.addPacket(body0);
            assertEquals(FileBuilder.FileBuilderStatus.COMPLETE, testingBuilder.status);
            assertTrue(testingBuilder.isFinished());
            assertEquals(0, testingBuilder.packetQueue.size());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void writesAllQueuedFromName(){
        try{
            testingBuilder.addPacket(body1);
            testingBuilder.addPacket(bodyFinal);
            testingBuilder.addPacket(body0);
            testingBuilder.addPacket(hp);
            assertEquals(FileBuilder.FileBuilderStatus.COMPLETE, testingBuilder.status);
            assertTrue(testingBuilder.isFinished());
            assertEquals(0, testingBuilder.packetQueue.size());
        } catch (Exception e) {
            fail();
        }
    }
}
