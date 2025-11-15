package robo2;
import robocode.*;
import java.awt.*;
import java.util.Random;

public class Botbustersrobo2 extends AdvancedRobot {
    Random rand = new Random();               // Gerador de números aleatórios para movimentos imprevisíveis
    boolean movingForward = true;             // Controla se o robô está avançando ou recuando
    double previousEnemyEnergy = 100;         // Armazena a energia anterior do inimigo para detectar tiros

    public void run() {
        // Define cores do robô: corpo, arma e radar
        setColors(Color.orange, Color.yellow, Color.green);

        // Impede que o movimento do radar e da arma afetem a rotação do corpo
        setAdjustRadarForGunTurn(true);
        setAdjustGunForRobotTurn(true);

        // Loop principal do robô (executa enquanto estiver vivo)
        while (true) {
            setTurnGunRight(360);   // Gira a arma 360° continuamente para procurar inimigos
            doZigZag();             // Faz movimento em zig-zag para dificultar tiros inimigos
            execute();              // Executa ações pendentes (movimento/rotação)
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        // Calcula quanto de energia o inimigo perdeu
        double changeInEnergy = previousEnemyEnergy - e.getEnergy();

        // Detecta se o inimigo atirou (normalmente perde de 0.1 a 3 de energia)
        if (changeInEnergy > 0 && changeInEnergy <= 3) {
            // Esquiva lateral ao detectar tiro
            setTurnRight(90 - e.getBearing());
            setAhead(150);
        }

        // Se a energia do robô estiver baixa, prioriza evasão
        if (getEnergy() < 30) {
            setTurnRight(90 - e.getBearing());
            setAhead(200);
        } else {
            // Caso contrário, atira no inimigo
            // Quanto menor a distância, maior o poder do tiro (até 3)
            double power = Math.min(3.0, 400 / e.getDistance());
            setFire(power);
        }

        // Atualiza energia do inimigo para a próxima checagem
        previousEnemyEnergy = e.getEnergy();
    }

    public void doZigZag() {
        // Movimento imprevisível alternando entre avançar e recuar
        if (movingForward) {
            setAhead(100 + rand.nextInt(100));         // Avança distância aleatória
            setTurnRight(rand.nextInt(60) - 30);       // Pequena rotação aleatória
        } else {
            setBack(100 + rand.nextInt(100));          // Recuo aleatório
            setTurnRight(rand.nextInt(60) - 30);       // Pequena rotação aleatória
        }

        // Alterna direção para o próximo ciclo
        movingForward = !movingForward;
    }

    public void onHitWall(HitWallEvent e) {
        // Ao bater na parede, recua um pouco
        setBack(50);
        // Gira aleatoriamente entre 90° e 180° para evitar travar na parede
        setTurnRight(90 + rand.nextInt(90));
    }
}