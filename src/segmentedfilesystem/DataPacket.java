package segmentedfilesystem;

public class DataPacket extends Packet{
    public boolean isFinal;

    int packetNumber;

    public int getPacketNumber(){
        return packetNumber;
    }

    byte[] packetBody;

    public byte[] getPacketBody(){
        return packetBody;
    }
}