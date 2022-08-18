# David Ricardo Otálora Bernal

# ARSW_Laboratorio1
#Parte I - Introducción a Hilos en Java

De acuerdo con lo revisado en las lecturas, complete las clases CountThread, para que las mismas definan el ciclo de vida de un hilo que imprima por pantalla los números entre A y B.

Complete el método main de la clase CountMainThreads para que:
1) Cree 3 hilos de tipo CountThread, asignándole al primero el intervalo [0..99], al segundo [99..199], y al tercero [200..299].

public class CountThread extends Thread{
    private int A;
    private int B;
    public CountThread(int A, int B){
        this.A=A;
        this.B=B;
    }

    public void run(){
        for (A=0; A<B; A++){
            System.out.println(A);
        }
    }
}


2) Inicie los 3 threads con start(). Corra y mire el resultado en pantalla.

Como podemos ver los hilos no estan sincronizados, se ejecutan los 3 hilos en paralelo pero sin ningun orden, por ende la salida de un hilo se vera interrumpida por la salida de otro hilo.

![image](https://user-images.githubusercontent.com/46855679/185294125-2f0917de-9c8f-4b1b-8416-34fa0935a731.png)

3) Cambie el incio con 'start()' por 'run()'. Cómo cambia la salida?, por qué?.

A diferencia de start que crea un nuevo hilo, run ejecuta un mismo hilo, asi que no ejecutara un proximo run hasta que la anterior ejecucion no haya finalizado, por eso aqui si los hilos estan sincronizados.

![image](https://user-images.githubusercontent.com/46855679/185294530-211709be-a757-450b-9784-a1b218ecdcb8.png)

Parte II.I Para discutir
Para encontrar el número minimo de ocurrencias requeridas (la variable BLACK_LIST_ALARM_COUNT) podemos simplemente crear una variable global, esta sera visible ne los threads.

    for (int j = rangeBegin; j < rangeEnd && number_servers < BLACK_LIST_ALARM_COUNT; j++) {
            checkedListsCount++;
            if (skds.isInBlackListServer(j, ipaddress)) {
                blackListOcurrences.add(j);
                numbers_servers++;
            }
        }
        
El posible error seria que en caso de una iteraccion mutua entre los diferentes threads  con esta variable, podria alterar el resultado de la variable de manera incorrecta, y podria conllevar a un mal funcionamiento del programa.

Para evitar esto podriamos usar la palabra reservada "synchronized" lo que permite el uso mutuo entre todos los threads que usen la variable.


Parte III - Evaluación de Desempeño

Funcionamiento con 1 Thread
El uso del PC es muy bajo en este caso, tan solo de 0.3%

![image](https://user-images.githubusercontent.com/46855679/185296739-20a60ccb-75ea-43b5-8e44-d77f2c56bfbf.png)

Funcionamiento con 8 Threads (núcleos de nuestro PC)

![image](https://user-images.githubusercontent.com/46855679/185296755-2b4a3f89-a988-4701-b89b-b31d78342615.png)

Aquí el uso del pc es del 4.3%, es un uso todavía bajo pero el tiempo de reducción es bastante significante.

![image](https://user-images.githubusercontent.com/46855679/185296788-812746a1-02c7-4854-83d3-c4170facc91b.png)

Funcionamiento con 16 Threads (doble de núcleos de nuestro PC)
El uso de la CPU aumenta a un 5% con una pequeña diferencia en el tiempo.

![image](https://user-images.githubusercontent.com/46855679/185296806-e181bd14-b133-4996-9c26-2373e1f5214d.png)

Funcionamiento con 50 y 100 Threads
El uso del CPU aumenta hasta el 4.5%, sin embargo, es demasiado rápido para poderlo detectar, podemos decir que aunque el uso del CPU debe aumentar considerablemente, el proceso es muy corto para poder hacerle un análisis en VisualVM.

![image](https://user-images.githubusercontent.com/46855679/185296827-9572d44b-d746-4e1c-b05a-4959fa7c36a2.png)

En la siguiente gráfica observamos el tiempo de ejecución según el número de hilos usados, lógicamente existe un límite en el tiempo, en nuestro caso es el límite cuando el tiempo T=2, entre más hilos usemos, mas será cerca de ejecutarse en dos segundos.

![image](https://user-images.githubusercontent.com/46855679/185296967-de767872-e2cc-4a02-93c1-40f1a85c9a38.png)

Parte IV - Ejercicio Black List Search

Según la ley de Amdahls:

![image](https://user-images.githubusercontent.com/46855679/185297021-de2c8406-66a3-4f6f-b945-c696a642deb9.png)
, donde S(n) es el mejoramiento teórico del desempeño, P la fracción paralelizable del algoritmo, y n el número de hilos, a mayor n, mayor debería ser dicha mejora. Por qué el mejor desempeño no se logra con los 500 hilos?, cómo se compara este desempeño cuando se usan 200?.
  
  RTA: Porque existe un limite de tiempo del procesador, asi se realicen mas subprocesos el limite de tiempo sera el mismo, por eso como vemos en la grafica, asi apliquemos mas threads, el tiempo siempre va acercarse a un valor especifico.

Cómo se comporta la solución usando tantos hilos de procesamiento como núcleos comparado con el resultado de usar el doble de éste?.

  RTA: Existe una enorme diferencia entre usar un hilo y según los núcleos del procesador (en este caso 8 hilos), si aplicamos el doble (16) claramente hay una diferencia también, pero no es tan notoria pues a partir de 8 que es la cantidad de núcleos de nuestro pc, los tiempos se aproximaran cada vez mas a un numero.
  
De acuerdo con lo anterior, si para este problema en lugar de 100 hilos en una sola CPU se pudiera usar 1 hilo en cada una de 100 máquinas hipotéticas, la ley de Amdahls se aplicaría mejor?. Si en lugar de esto se usaran c hilos en 100/c máquinas distribuidas (siendo c es el número de núcleos de dichas máquinas), se mejoraría?. Explique su respuesta.

  •	RTA: En la gráfica podemos observar que a partir de 40 hilos aproximadamente el cambio de esto es, en ese orden de ideas, si existieran cada proceso por maquina con sus núcleos, no habría mucha diferencia, ya que sigue el tiempo en que se usa el procesador de igual manera como si tuviera mas núcleos


