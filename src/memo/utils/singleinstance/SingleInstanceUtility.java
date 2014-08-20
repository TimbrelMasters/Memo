package memo.utils.singleinstance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *  Helps you manage new instances of application through ServerSocket
 * Require some improvements about use-case:
 * registerInstance -> setPort -> registerInstance
 * @author Pisarik
 */
public class SingleInstanceUtility {
    /**
     * Must be high socket number and not busied by other programs
     */
    private static int SINGLE_INSTANCE_SOCKET_PORT;

    /**
     * Must end with newline and be unique for the same application
     */
    private static String SINGLE_INSTANCE_SHARED_KEY;

    private static NewInstanceListener subListener;

    private static ServerSocket serverSocket;
    private static final Thread instanceListenerThread;

    static{
        //default static values
        SINGLE_INSTANCE_SOCKET_PORT = 45261;
        SINGLE_INSTANCE_SHARED_KEY = "&&NewInstance&&\n";

        //handler requests from new instances
        instanceListenerThread = new Thread(() -> {
            boolean socketClosed = false;

            while (!socketClosed) {
                if (serverSocket.isClosed()) {
                    socketClosed = true;
                } else {
                    try {
                        Socket client = serverSocket.accept();
                        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

                        String message = input.readLine();
                        if (SINGLE_INSTANCE_SHARED_KEY.trim().equals(message.trim())) {
                            fireNewInstance();
                        }

                        input.close();
                        client.close();
                    } catch (IOException e) {
                        socketClosed = true;
                    }
                }
            }
        });
    }

    /**
     * Registers this instance of the application.
     *
     * @return true if first instance, false if not.
     */
    public static boolean registerInstance() {
        // returnValueOnError should be true if lenient (allows app to run on network error) or false if strict.
        boolean returnValueOnError = true;
        // try to open serverSocket
        // if success, listen to socket for new instance message, return true
        // if unable to open, connect to existing and send new instance message, return false
        try {
            serverSocket = new ServerSocket(SINGLE_INSTANCE_SOCKET_PORT, 10, InetAddress.getByAddress(new byte[] {127, 0, 0, 1}));

            instanceListenerThread.start();
        } catch (UnknownHostException e) {
            return returnValueOnError;
        } catch (IOException e) { //isn't first instance
            try {
                Socket clientSocket = new Socket(InetAddress.getByAddress(new byte[] {127, 0, 0, 1}), SINGLE_INSTANCE_SOCKET_PORT);
                OutputStream output = clientSocket.getOutputStream();

                output.write(SINGLE_INSTANCE_SHARED_KEY.getBytes());

                output.close();
                clientSocket.close();
                return false;
            } catch (UnknownHostException e1) {
                System.err.println(e.getMessage() + e);
                return returnValueOnError;
            } catch (IOException e1) {
                System.err.println(e1.getMessage() + e1);
                return returnValueOnError;
            }
        }
        return true;
    }

    public static void closeInstance() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("Error while closing the socket");
            }
        }
        if (instanceListenerThread.isAlive()){
            instanceListenerThread.stop();
        }
    }

    private static void fireNewInstance(){
        subListener.onNewInstance();
    }

    public static int getPort() {
        return SINGLE_INSTANCE_SOCKET_PORT;
    }

    /**
     *
      * @param port must to have high order number(port can be from 1 to 65 536)
     *             to avoid collisions between other applications and be unique
     *             for the same applications.
     */
    public static void setPort(int port) {
        SingleInstanceUtility.SINGLE_INSTANCE_SOCKET_PORT = port;
    }

    public static String getSharedKey() {
        return SINGLE_INSTANCE_SHARED_KEY;
    }

    /**
     *
     * @param sharedKey must end with newline and be unique for the same applications.
     */
    public static void setSharedKey(String sharedKey) {
        SingleInstanceUtility.SINGLE_INSTANCE_SHARED_KEY = sharedKey;
    }

    public static NewInstanceListener getNewInstanceListener() {
        return subListener;
    }

    public static void setNewInstanceListener(NewInstanceListener listener) {
        SingleInstanceUtility.subListener = listener;
    }

}


