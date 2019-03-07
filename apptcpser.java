/*
 *Esta clase esta encargada de recibir archivos multimedia de un cliente y reproducira el contenido al terminar.
 *Funcionamiento: java apptcpser -d <directorio para guardar imágenes> -p<puerto que usara el servidor>
 *Otros comandos: l (letrq ele) debe listar los archivos que posee en su directorio.
 *                q (quit) debe terminar el programa.
 */


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * @since 05/03/2019
 * @author German Daniel 
 * @vesion 1.0
 */
public class apptcpser {

    public static void main(String[] arg) throws IOException {
        int puerto = 0;
        String directorio = "";
        DataInputStream lectorMensajes;
        DataOutputStream salidaMensajes;
        ServerSocket socketServer = null;
        Socket socketCliente = new Socket();
        boolean apagar = true;

    
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

    
        Scanner entrada = new Scanner(System.in);

        if (socketServer == null && puerto != 0) {
            socketServer = new ServerSocket(puerto);

        }

        while (apagar) {

            System.out.println("Se Incio El Servidor DanielTCP");
            
               
                while(true){
                     System.out.println("MENU : \n"+"* q (quit) \n"+"* c (continue) \n"+"* l (listFile) \n");
                    String en = entrada.next();
                      if (en.equals("q")) {
                    socketServer.close();
                    System.exit(0);
                } else {
                    if (en.equals("l")) {
                        EjecutarComando("dir " + directorio);
                    }else{
                        if(en.equals("c")){
                            System.out.println("Eseperando conexion ...");
                            System.out.println("");
                           break;
                        }
                    }
                }
                }
          
            try {
                socketCliente = socketServer.accept();//sirve para detectar cuando un cliente se conecte.
                System.out.println("SE CONECTO UN CLIENTE");
                System.out.println("Se esta recibiendo un recurso multimedia");
                long startTime = System.currentTimeMillis();
                salidaMensajes = new DataOutputStream(socketCliente.getOutputStream());//se inicializan los flujos de entrda y salida
                lectorMensajes = new DataInputStream(socketCliente.getInputStream());
                //---------------------------------------
                String directorioD = directorio+"\\video.mp4";
                FileOutputStream escritorArchivo = new FileOutputStream(directorioD);
                int bytesLeidos = 0;
                byte[] h = new byte[1024];
               
                
                while ((bytesLeidos = lectorMensajes.read(h)) != -1) {
                    escritorArchivo.write(h, 0, bytesLeidos);
                    
                }
                escritorArchivo.flush();
                escritorArchivo.close();
                
                
                System.out.println("Se termino la Transmisión");
                long endTime = System.currentTimeMillis() - startTime;
                System.out.println("Tiempo de Transmision : "+ endTime/1e9 +" Segundos");
                System.out.println("Directorio del Multimedia recibido : "+ directorioD);
                System.out.println("");

                
                System.out.println("MENU : \n"+"* q (quit) \n"+"* r (play) \n"+"* l (listFile) \n");   
                
              String  en = entrada.next();

                if (en.equals("q")) {
                    socketServer.close();
                    System.exit(0);
                } else {
                    if (en.equals("l")) {
                        EjecutarComando("dir " + directorio);
                        socketCliente.close();
                    }else{
                        if(en.equals("r")){
                            EjecutarComando("start " + directorioD);
                            socketCliente.close();
                        }
                    }
                }

            } catch (Exception error) {
                System.out.println(error.getMessage());//en caso de que no funcione notificara el por que?
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
