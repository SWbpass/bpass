package com.swhackathon.bpass.beacon;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import android.util.Log;

import org.altbeacon.beacon.AltBeacon;
import org.altbeacon.beacon.simulator.BeaconSimulator;

import org.altbeacon.beacon.Beacon;

/**
 * Created by Matt Tyler on 4/18/14.
 */
public class TimedBeaconSimulator implements BeaconSimulator {
    protected static final String TAG = "TimedBeaconSimulator";
    private List<Beacon> beacons;

    /**
     * You may simulate detection of beacons by creating a class like this in your project.
     * This is especially useful for when you are testing in an Emulator or on a device without BluetoothLE capability.
     *
     * Uncomment the lines in BeaconReferenceApplication starting with:
     *     // If you wish to test beacon detection in the Android Emulator, you can use code like this:
     * Then set USE_SIMULATED_BEACONS = true to initialize the sample code in this class.
     * If using a Bluetooth incapable test device (i.e. Emulator), you will want to comment
     * out the verifyBluetooth() in MonitoringActivity.java as well.
     *
     * Any simulated beacons will automatically be ignored when building for production.

     * 프로젝트에서 이와 같은 클래스를 생성하여 비콘 감지를 시뮬레이션 할 수 있습니다.
     * 이것은 에뮬레이터 또는 BluetoothLE 기능이없는 장치에서 테스트 할 때 특히 유용합니다.
     *
     * BeaconReferenceApplication에서 다음으로 시작하는 줄의 주석 처리를 제거합니다.
     * // Android Emulator에서 비콘 감지를 테스트하려면 다음과 같은 코드를 사용할 수 있습니다.
     * 그런 다음 USE_SIMULATED_BEACONS = true를 설정하여이 클래스의 샘플 코드를 초기화합니다.
     * 블루투스 기능이없는 테스트 장치 (예 : 에뮬레이터)를 사용하는 경우 댓글을 달고 싶을 것입니다.
     * MonitoringActivity.java의 verifyBluetooth ()도 확인하세요.
     *
     * 시뮬레이션 된 비콘은 생산을 위해 제작할 때 자동으로 무시됩니다.
     **/
    public boolean USE_SIMULATED_BEACONS = false;

    /**
     *  Creates empty beacons ArrayList.
     */
    public TimedBeaconSimulator(){
        beacons = new ArrayList<Beacon>();
    }

    /**
     * Required getter method that is called regularly by the Android Beacon Library.
     * Any beacons returned by this method will appear within your test environment immediately.
     *
     *  Android Beacon Library에서 정기적으로 호출하는 필수 getter 메소드.
     * 이 메서드에서 반환 된 모든 비콘은 테스트 환경에 즉시 표시됩니다.
     */
    public List<Beacon> getBeacons(){
        return beacons;
    }

    /**
     * Creates simulated beacons all at once.
     * 시뮬레이션 된 비콘을 한 번에 생성합니다.
     */
    public void createBasicSimulatedBeacons(){
        Log.d(TAG,": first_createTimedSimulatedBeacons");
        if (USE_SIMULATED_BEACONS) {
            Beacon beacon1 = new AltBeacon.Builder().setId1("DF7E1C79-43E9-44FF-886F-1D1F7DA6997A")
                    .setId2("1").setId3("1").setRssi(-55).setTxPower(-55).build();
            Beacon beacon2 = new AltBeacon.Builder().setId1("DF7E1C79-43E9-44FF-886F-1D1F7DA6997A")
                    .setId2("1").setId3("2").setRssi(-55).setTxPower(-55).build();
            Beacon beacon3 = new AltBeacon.Builder().setId1("DF7E1C79-43E9-44FF-886F-1D1F7DA6997A")
                    .setId2("1").setId3("3").setRssi(-55).setTxPower(-55).build();
            Beacon beacon4 = new AltBeacon.Builder().setId1("DF7E1C79-43E9-44FF-886F-1D1F7DA6997A")
                    .setId2("1").setId3("4").setRssi(-55).setTxPower(-55).build();
            beacons.add(beacon1);
            beacons.add(beacon2);
            beacons.add(beacon3);
            beacons.add(beacon4);
        }
    }


    private ScheduledExecutorService scheduleTaskExecutor;


    /**
     * Simulates a new beacon every 10 seconds until it runs out of new ones to add.
     * 추가 할 새 비콘이 다 떨어질 때까지 10 초마다 새 비콘을 시뮬레이션합니다.
     */
    public void createTimedSimulatedBeacons(){
        Log.d(TAG,": second_createTimedSimulatedBeacons");
        if (USE_SIMULATED_BEACONS){
            beacons = new ArrayList<Beacon>();
            Beacon beacon1 = new AltBeacon.Builder().setId1("DF7E1C79-43E9-44FF-886F-1D1F7DA6997A")
                    .setId2("1").setId3("1").setRssi(-55).setTxPower(-55).build();
            Beacon beacon2 = new AltBeacon.Builder().setId1("DF7E1C79-43E9-44FF-886F-1D1F7DA6997A")
                    .setId2("1").setId3("2").setRssi(-55).setTxPower(-55).build();
            Beacon beacon3 = new AltBeacon.Builder().setId1("DF7E1C79-43E9-44FF-886F-1D1F7DA6997A")
                    .setId2("1").setId3("3").setRssi(-55).setTxPower(-55).build();
            Beacon beacon4 = new AltBeacon.Builder().setId1("DF7E1C79-43E9-44FF-886F-1D1F7DA6997A")
                    .setId2("1").setId3("4").setRssi(-55).setTxPower(-55).build();
            beacons.add(beacon1);
            beacons.add(beacon2);
            beacons.add(beacon3);
            beacons.add(beacon4);

            final List<Beacon> finalBeacons = new ArrayList<Beacon>(beacons);

            //Clearing beacons list to prevent all beacons from appearing immediately.`
            //These will be added back into the beacons list from finalBeacons later.
            // 모든 비콘이 즉시 나타나지 않도록 비콘 목록을 지 웁니다.
            // 나중에 finalBeacons에서 beacons 목록에 다시 추가됩니다.
            beacons.clear();

            scheduleTaskExecutor= Executors.newScheduledThreadPool(5);

            // This schedules an beacon to appear every 10 seconds:
            scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    try{
                        //putting a single beacon back into the beacons list.
                        if (finalBeacons.size() > beacons.size())
                            beacons.add(finalBeacons.get(beacons.size()));
                        else
                            scheduleTaskExecutor.shutdown();

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }, 0, 10, TimeUnit.SECONDS);
        }
    }

}