package segmentedfilesystem;

import java.net.DatagramPacket;
import java.util.Arrays;

public class PacketFactory {

    public PacketFactory(){};

    public Packet buildPacket(DatagramPacket input){
        Packet newPacket;

        byte[] packetData = input.getData();
        int inputLength = input.getLength();
        byte statusByte = packetData[0];

        if((statusByte & 1) == 0){
            newPacket = new HeaderPacket();
            ((HeaderPacket) newPacket).fileName = new String(packetData, 2, inputLength - 2);
        } else {
            newPacket = new DataPacket();
            ((DataPacket) newPacket).isFinal = ((statusByte >> 1) & 1) == 1;
            ((DataPacket) newPacket).packetNumber = (Byte.toUnsignedInt(packetData[2]) * 256) + Byte.toUnsignedInt(packetData[3]);
            ((DataPacket) newPacket).packetBody = Arrays.copyOfRange(packetData, 4, inputLength - 1);
        }
        newPacket.fileID = packetData[1];
        return newPacket;
    }
    
}
