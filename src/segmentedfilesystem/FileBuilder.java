package segmentedfilesystem;

import segmentedfilesystem.Packet.*;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileBuilder {

    FileBuilderStatus status;
    String fileName;
    PriorityQueue<DataPacket> packetQueue = new PriorityQueue<DataPacket>(new PacketComparator());
    int nextPacketNumber;
    FileOutputStream writer;
    

    //PacketFactory factory = new PacketFactory();

    

    public boolean isFinished() {
        return (status == FileBuilderStatus.COMPLETE || status == FileBuilderStatus.ERROR);
    }

    public FileBuilder(){
        status = FileBuilderStatus.AWAITING_NAME;
        nextPacketNumber = 0;
    }

    public void addPacket(HeaderPacket hp) throws FileNotFoundException, IOException{
        fileName = hp.getFileName();
        writer = new FileOutputStream(fileName);
        status = FileBuilderStatus.WRITING;
        writeFromQueue();
    }

    public void addPacket(DataPacket dp) throws IOException{
        if(status == FileBuilderStatus.WRITING && dp.getPacketNumber() == nextPacketNumber){
            writer.write(dp.getPacketBody());
            if(dp.isFinal){
                status = FileBuilderStatus.COMPLETE;
            } else {
                writeFromQueue();
            }
        } else {
            packetQueue.offer(dp);
        }
    }

    private void writeFromQueue() throws IOException{
        while(true){
            DataPacket nextDP = packetQueue.peek();
            if(nextDP != null && nextDP.getPacketNumber() == nextPacketNumber){
                writer.write(nextDP.getPacketBody());
                if(nextDP.isFinal){
                    status = FileBuilderStatus.COMPLETE;
                    break;
                } else {
                    nextPacketNumber++;
                }
            } else {
                break;
            }
        }
    }

    public enum FileBuilderStatus {AWAITING_NAME, WRITING, COMPLETE, ERROR};

    private class PacketComparator implements Comparator<DataPacket> {

        @Override
        public int compare(DataPacket o1, DataPacket o2) {
            return o1.getPacketNumber() - o2.getPacketNumber();
        }
    }
}
