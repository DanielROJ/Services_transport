
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

/*
* Esta clase esta encarga de simular un cliente en udp donde es el encargado de enviar un archivo multimedia a un servidor
* Funcionamiento : java apptcpcli -d <directorio con archivos de imagenes> -i <ip del servidor>  -p<puerto del servidor>
* Otros comandos: l (letrq ele) debe listar los archivos que posee en su directorio.
*                 q (quit) debe terminar el programa.
*                 e <nombre archivo> 
 */
/**
 *
 * @author danie
 */
public class appudpcli {

    public static void main(String[] arg) throws SocketException, IOException {
        Scanner entrada = new Scanner(System.in);
        String directorio = "";
        String nombreA = "";
        String host = "";
        int puerto = 0;

        int i = 0;

        do {

            switch (arg[i]) {
                case "-d":
                    directorio = arg[i + 1];
                    break;

                case "-i":
                    host = arg[i + 1];
                    break;

                case "-p":
                    puerto = Integer.parseInt(arg[i + 1]);
                    break;
            }
            i++;

        } while (i < arg.length);

        while (true) {
            System.out.println("MENU : \n" + "* q (quit) \n" + "* e (insert name file) \n" + "* l (listFile) \n");

            String en = entrada.next();

            if (en.equals("q")) {
                System.exit(0);
            } else {
                if (en.equals("l")) {
                    EjecutarComando("dir " + directorio);
                } else {
                    if (en.equals("e")) {
                        System.out.println("Escriba el nombre del archivo");
                        nombreA = entrada.next();
                        break;
                    }
                }
            }
        }

        DatagramSocket ds = new DatagramSocket();
        byte[] buf = null;
        InetAddress d = InetAddress.getByName(host);
        System.out.println("IP ESTABLECIDA : " + d.getHostAddress());

        System.out.println("Se inicio la transferencia del Contenido");
        long startTime = System.currentTimeMillis();
        File video = new File(directorio + "\\" + nombreA);

        for (int h = 0; h < 2; h++) {
            if (h == 0) {
                System.out.println("Se envia peso de bytes del archivo");
                String tam = "t" + video.length();
                buf = tam.getBytes();
                DatagramPacket DpSend = new DatagramPacket(buf, buf.length, d, puerto);
                ds.send(DpSend);

            } else {
                System.out.println("Se envia el archivo");
                FileInputStream fis = new FileInputStream(video);
                BufferedInputStream bis = new BufferedInputStream(fis);
                bis.read(buf, 0, buf.length);
                bis.close();
                DatagramPacket DpSend = new DatagramPacket(buf, buf.length, d, puerto);
                ds.send(DpSend);

            }
        }
        long endTime = System.currentTimeMillis() - startTime;
        System.out.println("Finalizo Transferencia");
        System.out.println("Tiempo de Transmisión : " + endTime / 1e9 + " Segundos");
        System.out.println("Directorio del Multimedia  : " + directorio + "\\" + nombreA);
        System.out.println("");

        System.out.println("MENU : \n" + "* q (quit) \n" + "* r (play) \n" + "* l (listFile) \n");

        String en = entrada.next();

        if (en.equals("q")) {
            System.exit(0);
        } else {
            if (en.equals("l")) {
                EjecutarComando("dir " + directorio);
            } else {
                if (en.equals("r")) {
                    EjecutarComando("start " + directorio + "\\" + nombreA);
                }
            }
        }

    }

    public static void EjecutarComando(String comando) {

        try {
            Process pro = Runtime.getRuntime().exec("cmd /c " + comando);
            InputStreamReader input = new InputStreamReader(pro.getInputStream());
            BufferedReader lector = new BufferedReader(input);

            while (lector.readLine() != null) {
                System.out.println(lector.readLine());
            }

        } catch (Exception e) {
            System.out.println("ERROR AL EJECUTAR COMANDO CMD : " + e.getMessage());
        }

    }

}
