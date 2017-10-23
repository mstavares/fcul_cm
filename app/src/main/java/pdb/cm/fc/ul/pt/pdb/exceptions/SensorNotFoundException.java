package pdb.cm.fc.ul.pt.pdb.exceptions;

import android.content.Context;
import android.content.Intent;

import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.activities.exceptions.SensorNotFoundActivity;

public class SensorNotFoundException extends Exception {

    public SensorNotFoundException(String message) {
        super(message);
    }

    public void showDialogError(Context context) {
        context.startActivity(new Intent(context, SensorNotFoundActivity.class)
                .putExtra(SensorNotFoundActivity.EXTRA_TITLE, context.getString(R.string.error_sensor_not_found_title))
                .putExtra(SensorNotFoundActivity.EXTRA_TITLE, getMessage())
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        );
    }

}
