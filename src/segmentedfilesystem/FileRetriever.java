package segmentedfilesystem;

import java.util.HashMap;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class FileRetriever {

        InetAddress serverAddress;
        int port;
        PacketFactory factory;
        HashMap<Byte, FileBuilder> fileBuilders;
        int numFiles = 3;

	public FileRetriever(String server, int port) {
                this.serverAddress = InetAddress.getByName(server);
                this.port = port;
                factory = new PacketFactory();
                fileBuilders = new HashMap<Byte, FileBuilder>();

	}

	public void downloadFiles() throws IOException, ConnectException{
                /**
                Socket sock = new Socket(server, port);
                InputStream sockIn = sock.getInputStream();
                OuputStream sockOut = sock.getOutputStream();
                */

                DatagramSocket sock = new DatagramSocket(port, serverAddress);
                sock.connect(server, port);
                DatagramPacket packetOut = new DatagramPacket(new byte[1], serverAddress, port);
                sock.send(packetOut);
                
                byte[] byteBufferIn = new byte[1028]; 
                DatagramPacket packetIn = new DatagramPacket(byteBufferIn, 1028);
                while(true){
                        packetIn = sock.receive();
                        addPacketToBuilder(packetIn);
                        if(fileBuilders.size == numFiles && allBuildersFinished()){
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

}
