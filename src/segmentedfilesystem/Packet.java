package segmentedfilesystem;
public abstract class Packet {
    byte fileID;

    public byte getFileID(){
        return fileID;
    }
    


    public class HeaderPacket extends Packet{
        String fileName;

        public String getFileName(){
            return fileName;
        }
    }
    
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


}
