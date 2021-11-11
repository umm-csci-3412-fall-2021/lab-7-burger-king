package segmentedfilesystem;
public abstract class Packet {
    byte fileID;

    public byte getFileID(){
        return fileID;
    }
    


    protected class HeaderPacket extends Packet{
        protected String fileName;

        public String getFileName(){
            return fileName;
        }
    }
    
    protected class DataPacket extends Packet{
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
