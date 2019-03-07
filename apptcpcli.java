

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

/*
* Esta clase esta encarga de simular un cliente en tcp donde es el encargado de enviar un archivo multimedia a un servidor
* Funcionamiento : java apptcpcli -d <directorio con archivos de imagenes> -i <ip del servidor>  -p<puerto del servidor>
* Otros comandos: l (letrq ele) debe listar los archivos que posee en su directorio.
*                 q (quit) debe terminar el programa.
 */
/**
 * @since 05/03/2019
 * @author German Daniel Rojas 1018.505.981
 * @vesion 1.0
 */
public class apptcpcli {

    public static void main(String[] arg) {
        Scanner entrada = new Scanner(System.in);
        String directorio = "";
        String nombreA = "";
        String host = "";
        int puerto = 0;
        DataInputStream lectorMensajes;
        DataOutputStream salidaMensajes;
        Socket socketCliente = null;

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

            try {
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

                if (puerto != 0 && host != "" && directorio != "") {

                    socketCliente = new Socket(host, puerto);
                    System.out.println("Se inicio la transferencia del Contenido");
                    long startTime = System.currentTimeMillis();
                    salidaMensajes = new DataOutputStream(socketCliente.getOutputStream());
                    File video = new File(directorio + "\\" + nombreA);

                    byte[] archivoBytes = new byte[(int) video.length()];

                    FileInputStream fis = new FileInputStream(video);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    bis.read(archivoBytes, 0, archivoBytes.length);
                    bis.close();

                    salidaMensajes.write(archivoBytes, 0, archivoBytes.length);
                    salidaMensajes.flush();
                    salidaMensajes.close();
                    
                     long endTime = System.currentTimeMillis() - startTime;
                System.out.println("Tiempo de Transmisión : "+ endTime/1e9 +" Segundos");
                System.out.println("Directorio del Multimedia  : "+ directorio+"\\"+nombreA);
                System.out.println("");

                    System.out.println("MENU : \n" + "* q (quit) \n" + "* r (play) \n" + "* l (listFile) \n");

                    String en = entrada.next();

                    if (en.equals("q")) {
                        System.exit(0);
                    } else {
                        if (en.equals("l")) {
                            EjecutarComando("dir " + directorio);
                            socketCliente.close();
                        } else {
                            if (en.equals("r")) {
                                EjecutarComando("start " + directorio + "\\" + nombreA);
                                socketCliente.close();
                            }
                        }
                    }
                } else {

                }
            } catch (Exception error) {
                System.out.println("Error en la conexion del cliente : " + error.getMessage());
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
