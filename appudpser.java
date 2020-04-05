/*
 *Esta clase esta encargada de recibir archivos multimedia de un cliente utilizando el protocolo udp  reproducira el contenido al terminar.
 *Funcionamiento: java apptcpser -d <directorio para guardar imágenes> -p<puerto que usara el servidor>
 *Otros comandos: l (letrq ele) debe listar los archivos que posee en su directorio.
 *                q (quit) debe terminar el programa.
 */

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner;

/**
 *
 * @author danie
 */
public class appudpser {

    public static void main(String[] arg) {
        String directorio = "";
        int puerto = 0;
        DatagramSocket ds = null;
        byte[] receive = new byte[65535];
        String directorioD = "";

        Scanner entrada = new Scanner(System.in);

        int i = 0;

        do {

            switch (arg[i]) {
                case "-d":
                    directorio = arg[i + 1];
                    break;

                case "-p":
                    puerto = Integer.parseInt(arg[i + 1]);
                    break;
            }
            i++;

        } while (i < arg.length);

        if (directorio != "" && puerto != 0) {
            try {
                System.out.println("Se Incio Servidor udpDaniel");
                ds = new DatagramSocket(puerto);
            } catch (Exception e) {
                System.out.println("Error al Crear el datagram sokect : " + e.getMessage());
                return;
            }

        }

        DatagramPacket DpReceive = null;
        int p = 0;
        int tam = 0;
        int y=0;
long startTime=0;
            try {

        while (true) {

                DpReceive = new DatagramPacket(receive, receive.length);
                startTime = System.currentTimeMillis();

                ds.receive(DpReceive);
                System.out.println("Se conecto un cliente UDP");
                System.out.println("Se esta recibiendo un recurso multimedia");

                if (p == 0 && data(receive).toString().indexOf("t") == 0) {
                    String[] h = data(receive).toString().split("t");
                    System.out.println("Tamaño del archivo recibido : " + h[1]);
                    tam = Integer.parseInt(h[1]);
                    receive = new byte[tam];
                    p++;
                } else {

                    directorioD = directorio + "\\video.mp4";
                    FileOutputStream escritorArchivo = new FileOutputStream(directorioD);
                    escritorArchivo.write(receive);
                    escritorArchivo.flush();
                    escritorArchivo.close();
                    p = 0;
                    receive = new byte[65535];
                }

                y++;
               

            if(y==2){
                break;
            }
        
           
        
        
        
        }
        
         System.out.println("Se termino la Transmisión");
                long endTime = System.currentTimeMillis() - startTime;
                System.out.println("Tiempo de Transmision : " + endTime / 1e9 + " Segundos");
                System.out.println("Directorio del Multimedia recibido : " + directorioD);
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
                            EjecutarComando("start " + directorioD);

                        }
                    }
                }
        
            } catch (Exception e) {
                System.out.println("Error en la transmision datagram sokect : " + e.getMessage());
                return;
            }

        
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    public static StringBuilder data(byte[] a) {
        if (a == null) {
            return null;
        }
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0) {
            ret.append((char) a[i]);
            i++;
        }
        return ret;
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
