package pdb.cm.fc.ul.pt.pdb.sensors.accelerometer;



public interface AccelerometerListener {

    interface onSensorChanged extends AccelerometerListener {
        void onSensorChanged(float xAccel, float yAccel, float zAccel);
    }

    interface onShake extends AccelerometerListener {
        void onShake();
    }

}
