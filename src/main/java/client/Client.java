package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Юзверь
 */
public class Client {
    private BufferedReader in;
    private Socket socket;
    private PrintWriter out;


    // Запрашивает у юзера ник и организовывает обмен сообщениями с сервером
    public Client(String ip, int port) {
        Scanner scanner = new Scanner(System.in);

        try {
                //Коннектимся к серверу и подключаем потоки I/O для сообщений
                socket = new Socket(ip, port);
                in     = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out    = new PrintWriter(socket.getOutputStream(), true);

                System.out.println("Введите свой ник");
                out.println(scanner.nextLine());

                // Запускаем мессенджер
                Messenger messenger = new Messenger();
                messenger.start();

                // отправляет все что введет юзер на сервак
                // пока не введет условие выхода "exit"
                String str = "";
                while (!str.equals("exit")){
                        str = scanner.nextLine();
                        out.println(str);
                }
                messenger.setStop();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Закрываем потоки и сокет
                    try {
                             in.close();
                             out.close();
                             socket.close();
                    } catch (Exception ex) {
                         ex.printStackTrace();
                    }
        }

    }

    public void login(){



    }

    /**
     * Создает дополнительную нить для связи с консолью
     * класс отправляет сообщения с сервера в консоль.
     * setStop - выключатель
     */
    private class Messenger extends Thread{

        private boolean stopped;

        /**
         * Прекращает пересылку сообщений
         */
        public void setStop(){
            stopped = true;
        }

        /**
         * Считывает все сообщения с сервера
         *  и печатает их в консоль
         */
        @Override
        public void run() {

            try {
                    while (!stopped){
                        String str = in.readLine();
                        System.out.println(str);
                    }
            } catch (IOException e){
                System.out.println("message error");
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        // создаем єкземпляр юзера (ип,порт)
        Client client = new Client("127.0.0.1", 8283);
    }
}
