/*
===============================================================
RobotBoundaryLogic.java
implements the business logic  

===============================================================
*/
package antonio.franzese.iss2021_resumableb.wenv;

import antonio.franzese.iss2021_resumableb.interaction.MsgRobotUtil;
import antonio.franzese.iss2021_resumableb.supports.IssCommSupport;
import mapRoomKotlin.mapUtil;

public class RobotBoundaryLogic {
    private final IssCommSupport rs;

    private int stepNum = 1;
    private boolean boundaryWalkDone = false;
    private boolean started = false;
    private boolean usearil = false;
    private final int moveInterval = 1000;
    private final RobotMovesInfo robotInfo;
    //public enum robotLang {cril, aril}    //todo

    public RobotBoundaryLogic(IssCommSupport support, boolean usearil, boolean doMap) {
        rs = support;
        this.usearil = usearil;
        robotInfo = new RobotMovesInfo(doMap);
        robotInfo.showRobotMovesRepresentation();
    }

    public void doBoundaryStop() {
        rs.request(usearil ? MsgRobotUtil.hMsg : MsgRobotUtil.haltMsg);
        delay(moveInterval); //to reduce the robot move rate
    }

    public void doBoundaryGoon() {
        rs.request(usearil ? MsgRobotUtil.wMsg : MsgRobotUtil.forwardMsg);
        delay(moveInterval); //to reduce the robot move rate
    }

    public void startResume() {
        if (started)
            doBoundaryGoon();

        else {
            doBoundaryInit();
            started = true;
        }

    }

    public synchronized String doBoundaryInit() {
        System.out.println("RobotBoundaryLogic | doBoundary rs=" + rs + " usearil=" + usearil);
        //rs.request( usearil ? MsgRobotUtil.wMsg : MsgRobotUtil.forwardMsg  );
        //The reply to the request is sent by WEnv after the wtime defined in issRobotConfig.txt  
        //delay(moveInterval ); //to reduce the robot move rate
        System.out.println(mapUtil.getMapRep());
        while (!boundaryWalkDone) {
            try {
                wait();
                //System.out.println("RobotBoundaryLogic | RESUMES " );
                rs.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        started = false;
        return robotInfo.getMovesRepresentationAndClean();
    }

    public void updateMovesRep(String move) {
        robotInfo.updateRobotMovesRepresentation(move);
    }

    //Business logic in RobotBoundaryLogic
    protected synchronized void boundaryStep(String move, boolean obstacle) {
        if (stepNum <= 4) {
            if (move.equals("turnLeft")) {
                updateMovesRep("l");
                //showRobotMovesRepresentation();
                if (stepNum == 4) {
                    boundaryWalkDone = true;
                    notify(); //to resume the main
                    return;
                }
                stepNum++;
                doBoundaryGoon();
                return;
            }
            //the move is moveForward
            if (obstacle) {
                rs.request(usearil ? MsgRobotUtil.lMsg : MsgRobotUtil.turnLeftMsg);
                delay(moveInterval); //to reduce the robot move rate
            }
            if (!obstacle) {
                updateMovesRep("w");
                doBoundaryGoon();
            }
            robotInfo.showRobotMovesRepresentation();
        } else { //stepNum > 4
            System.out.println("RobotBoundaryLogic | boundary ENDS");
        }
    }

    protected void delay(int dt) {
        try {
            Thread.sleep(dt);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
