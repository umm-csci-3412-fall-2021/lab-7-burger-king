package segmentedfilesystem;

import java.io.IOException;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

public class FileRetriever {

        InetAddress serverAddress;
        int port;
        PacketFactory factory;
        HashMap<Byte, FileBuilder> fileBuilders;
        int numFiles = 3;

        public FileRetriever(String server, int port) throws UnknownHostException {
                this.serverAddress = InetAddress.getByName(server);
                this.port = port;
                factory = new PacketFactory();
                fileBuilders = new HashMap<Byte, FileBuilder>(numFiles);

	}

	public void downloadFiles() throws IOException, ConnectException{
                /**
                Socket sock = new Socket(server, port);
                InputStream sockIn = sock.getInputStream();
                OuputStream sockOut = sock.getOutputStream();
                */

                DatagramSocket sock = new DatagramSocket();
                sock.connect(serverAddress, port);
                DatagramPacket packetOut = new DatagramPacket(new byte[1], 1, serverAddress, port);
                sock.send(packetOut);
                
                byte[] byteBufferIn = new byte[1028]; 
                DatagramPacket packetIn = new DatagramPacket(byteBufferIn, 1028);
                while(true){
                        System.out.println("Loop started!");
                        sock.receive(packetIn);
                        System.out.println("Packet received!");
                        Packet parsedIn = factory.buildPacket(packetIn);
                        addPacketToBuilder(parsedIn);
                        System.out.println("Packet added!");
                        if(fileBuilders.size() == numFiles && allBuildersFinished()) {
                                sock.close();
                                break;
                        }
                }


                
        // Do all the heavy lifting here.
        // This should
        //   * Connect to the server
        //   * Download packets in some sort of loop
        //   * Handle the packets as they come in by, e.g.,
        //     handing them to some PacketManager class
        // Your loop will need to be able to ask someone
        // if you've received all the packets, and can thus
        // terminate. You might have a method like
        // PacketManager.allPacketsReceived() that you could
        // call for that, but there are a bunch of possible
        // ways.
        }

        public void addPacketToBuilder(Packet in) throws IOException {
                FileBuilder toAddTo = fileBuilders.putIfAbsent(in.getFileID(), new FileBuilder());
                if(toAddTo == null){
                        toAddTo = fileBuilders.get(in.getFileID());
                }

                toAddTo.addPacket(in);
                
        }

        public boolean allBuildersFinished(){
                for(FileBuilder b:fileBuilders.values()){
                        if(!b.isFinished()){
                                return false;
                        }
                }
                return true;
        }

}
