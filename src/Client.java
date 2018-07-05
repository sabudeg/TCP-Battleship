import java.io.*;
import java.net.*;
import java.util.Scanner;

class Client {

    int konum;
    public static Server serverobject = new Server();
    public static Client clientInside = new Client();


    char[] boardClient = new char[10];
    static char[] clientOpponent = new char[10];


    public static void main(String[] args) throws Exception {
        serverobject.welcome();
        clientInside.fillBoardClient();
        clientInside.clientTurn();
    }


    void fillBoardClient() {
        for (int i = 1; i < 4; i++) {
            System.out.print("Place " + i + ". ship: ");
            Scanner input = new Scanner(System.in);
            int target = input.nextInt();
            if (boardClient[target - 1] == 'S') {
                System.out.println("This place is already filled.");
                i--;
            } else boardClient[target - 1] = 'S';
        }

        printBoardClient();
    }

    void printBoardClient() {
        System.out.print("Your Board:     ");
        for (char index : boardClient) {
            System.out.print("-" + index + "- ");
        }
        System.out.println();
    }


    public void hitClientBoard(int x) {
        konum = x - 1;

        if (boardClient[konum] == 'S') {
            boardClient[konum] = 'X';
            //checkServerOpponent(x);
            System.out.println("Server hit: " + x);
        } else System.out.println("Server missed.");

        isOver();

        printBoardClient();
    }


    public void isOver() {
        boolean contains = false;
        for (char c : boardClient) {
            if (c == 'S') {
                contains = true;
                break;
            }
        }
        if (!contains) {
            System.out.println("GAME OVER! SERVER WON.");
            System.exit(0);
        }
    }


    void clientTurn() throws Exception {
        String input;
        String serverHit;
        Socket clientSocket = new Socket("localhost", 6666);
        try {
            while (true) {
                BufferedReader inputClient = new BufferedReader(new InputStreamReader(System.in));
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                System.out.print("Hit ServerBoard:  ");
                input = inputClient.readLine();
                outToServer.writeBytes(input + '\n');


                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                serverHit = inFromServer.readLine();
                int konum = Integer.parseInt(serverHit);
                hitClientBoard(konum);
            }
        } catch (SocketException se) {
            System.out.println("GAME OVER! YOU WON.");
            System.exit(0);
        }
    }
}