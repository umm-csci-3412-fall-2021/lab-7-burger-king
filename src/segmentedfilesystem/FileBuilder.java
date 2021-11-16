package segmentedfilesystem;

import Java.util.PriorityQueue;
import Java.util.FileOutputStream;

public class FileBuilder {

    FileBuilderStatus status;
    String fileName;
    PriorityQueue<DataPacket> packetQueue = new PriorityQueue<DataPacket>(new PacketComparator());
    int nextPacketNumber;
    FileOutputStream writer;
    

    //PacketFactory factory = new PacketFactory();

    

    public boolean isFinished() {
        return (status == COMPLETE || status == ERROR);
    }

    public FileBuilder(){
        status = AWAITING_NAME;
        nextPacketNumber = 0;
    };

    public void addPacket(HeaderPacket hp){
        fileName = hp.getFileName();
        writer = new FileOutputStream(fileName);
        status = WRITING;
        writeFromQueue();
    }

    public void addPacket(DataPacket dp){
        if(status == WRITING && dp.getPacketNumber() == nextPacketNumber){
            writer.write(dp.getPacketBody());
            if(dp.isFinal()){
                status = COMPLETE;
            } else {
                writeFromQueue();
            }
        } else {
            packetQueue.offer(dp);
        }
    }

    private void writeFromQueue(){
        while(true){
            DataPacket nextDP = packetQueue.peek()
            if(nextDP != null && nextDP.getPacketNumber == nextPacketNumber){
                writer.write(nextDP.getPacketBody);
                if(nextDP.isFinal){
                    status = COMPLETE;
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
        public boolean compare(DataPacket o1, DataPacket o2) {
            return o1.getPacketNumber() - o2.getPacketNumber();
        }
    }
}
