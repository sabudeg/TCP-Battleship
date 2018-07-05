import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server {

    int konum;
    char [] boardServer = new char[10];
    char [] serverOpponent =new char[10];


    public static Client clientobject = new Client();
    public static Server serverInside = new Server();

    public static void main(String[] args) throws Exception
    {
        serverInside.welcome();
        serverInside.fillBoardServer();
        serverInside.serverTurn();
    }

    void welcome(){
        System.out.println("        --- BATTLESHIP GAME ---");
        System.out.println("Place your ships with the numbers between 1-10");
    }

    void fillBoardServer(){
        for (int i=1; i<4; i++) {
            System.out.print("Place "+i+". ship: ");
            Scanner input = new Scanner(System.in);
            int index = input.nextInt();
            if (boardServer[index-1] == 'S') {
                System.out.println("This place is already filled.");
                i--;
            }
            else boardServer[index-1] = 'S';
        }

        printBoardServer();
    }

    void printBoardServer(){
        System.out.print("Your Board:     ");
        for (char index:boardServer) {
            System.out.print("-" +index+ "- " );
        }
        System.out.println();
    }


    public void hitServerBoard(int x)
    {
        konum = x-1;
        if (boardServer[konum] == 'S') {
            boardServer[konum] = 'X';
            System.out.println("Client hit: " + x);
        }

        else System.out.println("Client missed.");

        isOver();

        printBoardServer();
    }

    public void isOver(){
        boolean contains = false;
        for (char c : boardServer) {
            if (c == 'S') {
                contains = true;
                break;
            }
        }

        if (!contains) {
            System.out.println("GAME OVER! CLIENT WON.");
            System.exit(0);
        }
    }

    void serverTurn() throws Exception {
        String clientHit;
        String input;
        ServerSocket serverSocket = new ServerSocket(6666);
        Socket connectionSocket = serverSocket.accept();
        if(connectionSocket.isConnected()){
            System.out.println("***Client connected***");
        }
        try {
        while (true) {
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            System.out.println("Waiting for client to hit...");
            clientHit = inFromClient.readLine();
            int konum1 =  Integer.parseInt(clientHit);
            hitServerBoard(konum1);


            BufferedReader inputServer = new BufferedReader(new InputStreamReader(System.in));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            System.out.print("Hit ClientBoard:  ");
            input = inputServer.readLine();
            outToClient.writeBytes(input + '\n');
        }
        } catch (SocketException se) {
            System.out.println("GAME OVER! YOU WON.");
            System.exit(0);
        }
    }
}
