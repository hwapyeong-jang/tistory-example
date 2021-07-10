package socket;

import java.io.IOException;
import java.net.*;

public class UdpClientExample {

    public static void main(String[] args) throws UnknownHostException, SocketException, IOException {
        byte[] bytes = new byte[]{'T','E','S','T'};
        InetAddress inetAddress = InetAddress.getByName("localhost");
        DatagramSocket datagramSocket = new DatagramSocket();
        DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length, inetAddress, 5000);
        datagramSocket.send(datagramPacket);
    }
}
