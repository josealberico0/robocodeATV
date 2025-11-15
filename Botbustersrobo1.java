package robo1;

import robocode.*;
import java.awt.Color;
import robocode.util.Utils;

public class Botbustersrobo1 extends AdvancedRobot {

    private int direcao = 1;

    public void run() {
        setColors(Color.red, Color.black, Color.red);
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);

        // Método run(): executa o comportamento principal do robô (loop principal)
        // Aqui ele gira o radar constantemente procurando inimigos
        while (true) {
            turnRadarRight(360);
        }
    }

    // onScannedRobot(): chamado automaticamente quando o radar detecta outro robô
    public void onScannedRobot(ScannedRobotEvent e) {
        double anguloAbsoluto = getHeadingRadians() + e.getBearingRadians();
        double distancia = e.getDistance();

        // Mira o canhão na direção do inimigo
        double giroCanhao = Utils.normalRelativeAngle(anguloAbsoluto - getGunHeadingRadians());
        setTurnGunRightRadians(giroCanhao);

        // Movimento circular (orbital) em torno do inimigo
        setTurnRight(e.getBearing() + 90 - (15 * direcao));
        setAhead(150 * direcao);

        // Ajusta a potência do tiro conforme a distância do inimigo
        double potencia = Math.min(400 / distancia, 3);
        if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 10)
            setFire(potencia);

        // Mantém o radar travado no inimigo
        double giroRadar = Utils.normalRelativeAngle(anguloAbsoluto - getRadarHeadingRadians());
        setTurnRadarRightRadians(2 * giroRadar);
    }

    // onHitByBullet(): chamado quando o robô é atingido por uma bala
    public void onHitByBullet(HitByBulletEvent e) {
        // Muda de direção para tentar confundir o inimigo
        direcao *= -1;
        setAhead(100 * direcao);
    }

    // onHitWall(): chamado quando o robô colide com uma parede
    public void onHitWall(HitWallEvent e) {
        // Recuar e inverter a direção ao bater na parede
        direcao *= -1;
        setBack(100);
    }
}
