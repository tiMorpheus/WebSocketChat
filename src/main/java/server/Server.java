package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Cервер
 */
public class Server {
    //список коннекшенов в специальной потокобезопасной обёртке
    private List<Connection> connections =
            Collections.synchronizedList(new ArrayList<Connection>());
    private ServerSocket server;

    public static final int PORT = 8283; // Server port

    /**
     * Конструктор создаёт сервер. Затем для каждого подключения создаётся
     * объект Connection и добавляет его в список подключений.
     */
    public Server() {
        System.out.println("Server start");
        try {
            server = new ServerSocket(PORT);
            System.out.println("waiting for a users...");
            while (true) {
                Socket socket = server.accept();

                // Создаём объект Connection и добавляем его в список
                Connection con = new Connection(socket);
                connections.add(con);

                // Инициализирует нить и запускает метод run(),
                // которая выполняется одновременно с остальной программой
                con.start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
    }

    /**
     * Закрывает все потоки всех соединений а также серверный сокет
     */
    private void closeAll() {
        try {
            server.close();

            // Перебор всех Connection и вызов метода close() для каждого. Блок
            // synchronized {} необходим для правильного доступа к одним данным
            // их разных нитей
            synchronized(connections) {
                for(Connection c : connections) {
                    c.close();
                }
            }
        } catch (Exception e) {
            System.err.println("Потоки не были закрыты!");
        }
    }

    /**
     * Класс содержит данные, относящиеся к конкретному подключению:
     * <ul>
     * <li>имя пользователя</li>
     * <li>сокет</li>
     * <li>входной поток BufferedReader</li>
     * <li>выходной поток PrintWriter</li>
     * </ul>
     * Расширяет Thread и в методе run() получает информацию от пользователя и
     * пересылает её другим
     */
    private class Connection extends Thread {

        private BufferedReader in;
        private PrintWriter out;
        private Socket socket;

        private String name = "";

        /**
         * Инициализирует поля объекта и получает имя пользователя
         *
         * @param socket
         *            сокет, полученный из server.accept()
         */
        public Connection(Socket socket) {
            this.socket = socket;

            try {
                in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

            } catch (IOException e) {
                e.printStackTrace();
                close();
            }
        }

        /**
         * Запрашивает имя пользователя и ожидает от него сообщений. При
         * получении каждого сообщения, оно вместе с именем пользователя
         * пересылается всем остальным.
         *
         * @see java.lang.Thread#run()
         */
        @Override
        public void run() {
            try {
                name = in.readLine();
                // Отправляем всем клиентам сообщение о том, что зашёл новый пользователь
                synchronized(connections) {
                    for(Connection c : connections) {
                        c.out.println(name + " connected~~~~~~~~~~~");
                    }
                }

                String str = "";
                while (true) {
                    str = in.readLine();
                    if(str.equals("exit")) break;

                    // Отправляем всем клиентам очередное сообщение
                    synchronized(connections) {
                        for(Connection c : connections) {
                            c.out.println(name + ": " + str);
                        }
                    }
                }

                synchronized(connections) {

                    for (Connection c : connections){
                        c.out.println(name + " has left~~~~~~~~~~~");
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                close();
            }
        }

        /**
         * Закрывает входной и выходной потоки и сокет
         */
        public void close() {
            try {
                in.close();
                out.close();
                socket.close();

                // Если больше не осталось соединений, закрываем всё, что есть и
                // завершаем работу сервера
                connections.remove(this);
                if (connections.size() == 0) {
                    Server.this.closeAll();
                    System.exit(0);
                }
            } catch (Exception e) {
                System.err.println("Потоки не были закрыты!");
            }
        }
    }

    // Запуск сервера
    public static void main(String[] args) {
        Server server = new Server();
    }
}