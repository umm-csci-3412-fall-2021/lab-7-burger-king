package segmentedfilesystem;

public class HeaderPacket extends Packet{
    String fileName;

    public String getFileName(){
        return fileName;
    }
}