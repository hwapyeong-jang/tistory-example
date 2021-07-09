package socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TcpClientExample {
    /*
    여기는 Application Layer 다.
    아래에서 설명하는 Handshake 는 connect 와 close 명령이 수행될 경우
    OS Layer 에서 어떻게 처리하는 지 설명한 것이다.
     */
    public static void main(String[] args) {
        try (Socket socket = new Socket()) {
            /**
             * Connect 3-Way-Handshake
             *
             * (1) Client (상태 CLOSED -> SYN_SENT) SYN 전송
             * Synchronize Sequence Number 임의의 수 (1111) 생성
             * Synchronize Sequence Number: 1111
             * Acknowledgement Number: 0
             *
             * (2) Server (상태 LISTEN -> SYN_RECEIVED) SYN, ACK 전송
             * Synchronize Sequence Number 임의의 수 (2222) 생성
             * Synchronize Sequence Number: 2222
             * Acknowledgement Number: 1112 (Client 가 보낸 Synchronize Sequence Number + 1)
             *
             * (3) Client (상태 SYN_SENT -> ESTABLISHED) ACK 전송
             * Synchronize Sequence Number: 1112 (Server 가 보낸 Acknowledgement Number)
             * Acknowledgement Number: 2223 (Server 가 보낸 Synchronize Sequence Number + 1)
             * Server 측 상태 변경 - SYN_RECEIVED -> ESTABLISHED
             */
            socket.connect(new InetSocketAddress("localhost", 5000));

            /**
             * Try-with-resources 방식이므로 close 명시하지 않아도 됨.
             *
             * Close 4-Way-Handshake
             *
             * (1) Client (상태 ESTABLISHED -> FIN_WAIT_1) Finish Flag 전송
             * Synchronize Sequence Number: 1112
             * Acknowledgement Number: 2223
             *
             * (2) Server (상태 ESTABLISHED -> CLOSE_WAIT) Acknowledgement Number 전송
             * Synchronize Sequence Number: 2223 (Client 가 보낸 Acknowledgement Number)
             * Acknowledgement Number: 1113 (Client 가 보낸 Synchronize Sequence Number + 1)
             * Client 측 상태 변경 - FIN_WAIT_1 -> FIN_WAIT_2
             *
             * (3) Server (상태 CLOSE_WAIT -> LAST_ACK) Finish Flag 전송
             * Synchronize Sequence Number: 2223
             * Acknowledgement Number: 1113
             * 단계 (2), (3) 과정에서 추가로 데이터가 전송된다면 데이터 크기만큼 Synchronize Sequence Number 증가
             * Client 측 상태 변경 - FIN_WAIT_2 -> TIME_WAIT
             *
             * (4) Client (상태 TIME_WAIT -> CLOSED) Acknowledgement Number 전송
             * Synchronize Sequence Number: 1113 (Server 가 보낸 Acknowledgement Number)
             * Acknowledgement Number: 2224 (Server 가 보낸 Synchronize Sequence Number + 1)
             * Server 측 상태 변경 - LAST_ACK -> CLOSED
             */
            socket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
